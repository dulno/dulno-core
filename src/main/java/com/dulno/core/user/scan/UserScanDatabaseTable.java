package com.dulno.core.user.scan;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class UserScanDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "user_scan";

  public static UserScanDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("user", DatabaseDataType.UUID,
      DatabaseColumn.Type.PARTITION_KEY));
    columns.add(DatabaseColumn.create("scan", DatabaseDataType.UUID,
      DatabaseColumn.Type.CLUSTERING_KEY));
    var table = new UserScanDatabaseTable(connection, keyspace, TABLE_NAME, columns);
    table.createIfNotExists();
    return table;
  }

  private UserScanDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  public CompletableFuture<Void> insertUserScan(UUID userId, UUID scanId) {
    return insert(DatabaseRow.of(userId, scanId));
  }

  public CompletableFuture<Void> deleteUserScan(UUID userId, UUID scanId) {
    return delete(DatabaseCondition.of("user", userId, "scan", scanId));
  }

  public CompletableFuture<Boolean> userScanExists(UUID userId, UUID scanId) {
    return exists(DatabaseCondition.of("user", userId, "scan", scanId));
  }

  public CompletableFuture<List<UUID>> findUserScans(UUID userId) {
    return selectRows(DatabaseCondition.of("user", userId))
      .thenApply(rows -> rows.stream().map(row -> row.findCell(1).uuidValue())
        .toList());
  }
}