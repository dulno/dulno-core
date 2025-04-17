package com.dulno.core.item;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class ItemDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "item";

  public static ItemDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("id", DatabaseDataType.UUID,
      DatabaseColumn.Type.PRIMARY_KEY));
    columns.add(DatabaseColumn.create("partner", DatabaseDataType.UUID));
    columns.add(DatabaseColumn.create("card", DatabaseDataType.UUID));
    columns.add(DatabaseColumn.create("card_type", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("content", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("last_update", DatabaseDataType.BIGINT));
    var table = new ItemDatabaseTable(connection, keyspace, TABLE_NAME, columns);
    table.createIfNotExists();
    table.initializeViews();
    return table;
  }

  private DatabaseTable partnerView;

  private ItemDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  private void initializeViews() {
    partnerView = createMaterializedViewIfNotExists("partner_view", "partner",
      DatabaseColumn.Type.PARTITION_KEY);
  }

  public CompletableFuture<Void> insertItem(Item item) {
    return insertItem(item.itemId(), item.partnerId(), item.cardId(),
      item.content(), item.lastUpdate());
  }

  public CompletableFuture<Void> insertItem(
    UUID itemId, UUID partnerId, UUID cardId, String content, long lastUpdate
  ) {
    return insert(DatabaseRow.of(itemId, partnerId, cardId, content, lastUpdate));
  }

  public CompletableFuture<Void> updateItem(Item item) {
    return updateItem(item.itemId(), item.partnerId(), item.cardId(),
      item.content(), item.lastUpdate());
  }

  public CompletableFuture<Void> updateItem(
    UUID itemId, UUID partnerId, UUID cardId, String content, long lastUpdate
  ) {
    return update(itemId, DatabaseRow.of(itemId, partnerId, cardId, content,
      lastUpdate));
  }

  public CompletableFuture<UUID> generateAvailableItemId() {
    var futureResponse = new CompletableFuture<UUID>();
    var id = UUID.randomUUID();
    itemExists(id).thenApply(exists -> exists ?
      generateAvailableItemId().thenApply(futureResponse::complete) :
      CompletableFuture.completedFuture(futureResponse.complete(id)));
    return futureResponse;
  }

  public CompletableFuture<Void> deleteItem(UUID itemId) {
    return delete(itemId);
  }

  public CompletableFuture<Boolean> itemExists(UUID itemId) {
    return exists(itemId);
  }

  public CompletableFuture<Item> findItem(UUID itemId) {
    return selectRow(itemId).thenApply(row -> Item.of(row, this));
  }

  public CompletableFuture<List<Item>> findItemsOfPartner(UUID partnerId) {
    return partnerView.selectRows(DatabaseCondition.of("partner", partnerId))
      .thenApply(rows -> rows.stream().map(row -> Item.of(row, partnerView)).toList());
  }
}

