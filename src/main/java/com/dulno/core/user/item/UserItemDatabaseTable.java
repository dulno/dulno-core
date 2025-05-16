package com.dulno.core.user.item;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class UserItemDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "user_item";

  public static UserItemDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("user", DatabaseDataType.UUID,
      DatabaseColumn.Type.PARTITION_KEY));
    columns.add(DatabaseColumn.create("item", DatabaseDataType.UUID,
      DatabaseColumn.Type.CLUSTERING_KEY));
    var table = new UserItemDatabaseTable(connection, keyspace, TABLE_NAME, columns);
    table.createIfNotExists();
    table.initializeViews();
    return table;
  }

  private DatabaseTable itemView;

  private UserItemDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  private void initializeViews() {
    itemView = createMaterializedViewIfNotExists("item_view", "item",
      DatabaseColumn.Type.PARTITION_KEY);
  }

  public CompletableFuture<Void> insertUserItem(UUID userId, UUID itemId) {
    return insert(DatabaseRow.of(userId, itemId));
  }

  public CompletableFuture<Void> deleteUserItem(UUID userId, UUID itemId) {
    return delete(DatabaseCondition.of("user", userId, "item", itemId));
  }

  public CompletableFuture<Boolean> userItemExists(UUID userId, UUID itemId) {
    return exists(DatabaseCondition.of("user", userId, "item", itemId));
  }

  public CompletableFuture<Boolean> userItemExists(UUID itemId) {
    return itemView.exists(DatabaseCondition.of("item", itemId));
  }

  public CompletableFuture<UUID> findItemUser(UUID itemId) {
    return itemView.selectRow(DatabaseCondition.of("item", itemId))
      .thenApply(row -> row.findCell(1).uuidValue());
  }

  public CompletableFuture<List<UUID>> findUserItems(UUID userId) {
    return selectRows(DatabaseCondition.of("user", userId))
      .thenApply(rows -> rows.stream().map(row -> row.findCell(1).uuidValue())
        .toList());
  }
}

