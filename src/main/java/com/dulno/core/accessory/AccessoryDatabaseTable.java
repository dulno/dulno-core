package com.dulno.core.accessory;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class AccessoryDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "accessory";

  public static AccessoryDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("id", DatabaseDataType.UUID,
      DatabaseColumn.Type.PRIMARY_KEY));
    columns.add(DatabaseColumn.create("partner", DatabaseDataType.UUID));
    columns.add(DatabaseColumn.create("accessory", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("price", DatabaseDataType.DOUBLE));
    columns.add(DatabaseColumn.create("purchase_date", DatabaseDataType.BIGINT));
    var table = new AccessoryDatabaseTable(connection, keyspace, TABLE_NAME, columns);
    table.createIfNotExists();
    table.initializeViews();
    return table;
  }

  private DatabaseTable partnerView;

  private AccessoryDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  private void initializeViews() {
    partnerView = createMaterializedViewIfNotExists("partner_view", "partner",
      DatabaseColumn.Type.PARTITION_KEY);
  }

  public CompletableFuture<Void> insertAccessory(Accessory accessory) {
    return insertAccessory(accessory.id(), accessory.partnerId(),
      accessory.accessoryId(), accessory.price(), accessory.purchaseDate());
  }

  public CompletableFuture<Void> insertAccessory(
    UUID id, UUID partnerId, String accessoryId, double price, long purchaseDate
  ) {
    return insert(DatabaseRow.of(id, partnerId, accessoryId, price, purchaseDate));
  }

  public CompletableFuture<UUID> generateAvailableAccessoryId() {
    var futureResponse = new CompletableFuture<UUID>();
    var id = UUID.randomUUID();
    accessoryExists(id).thenApply(exists -> exists ?
      generateAvailableAccessoryId().thenApply(futureResponse::complete) :
      CompletableFuture.completedFuture(futureResponse.complete(id)));
    return futureResponse;
  }

  public CompletableFuture<Void> deleteAccessory(UUID accessoryId) {
    return delete(accessoryId);
  }

  public CompletableFuture<Boolean> accessoryExists(UUID accessoryId) {
    return exists(accessoryId);
  }

  public CompletableFuture<Accessory> findAccessory(UUID accessoryId) {
    return selectRow(accessoryId).thenApply(row -> Accessory.of(row, this));
  }

  public CompletableFuture<List<Accessory>> findAccessoriesOfPartner(UUID partnerId) {
    return partnerView.selectRows(DatabaseCondition.of("partner", partnerId))
      .thenApply(rows -> rows.stream().map(row -> Accessory.of(row, partnerView))
        .toList());
  }
}


