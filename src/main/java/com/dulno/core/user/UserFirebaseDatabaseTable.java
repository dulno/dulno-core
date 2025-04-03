package com.dulno.core.user;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class UserFirebaseDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "user_firebase";

  public static UserFirebaseDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("user", DatabaseDataType.UUID,
      DatabaseColumn.Type.PARTITION_KEY));
    columns.add(DatabaseColumn.create("device", DatabaseDataType.UUID,
      DatabaseColumn.Type.CLUSTERING_KEY));
    columns.add(DatabaseColumn.create("identifier", DatabaseDataType.TEXT));
    var table = new UserFirebaseDatabaseTable(connection, keyspace, TABLE_NAME,
      columns);
    table.createIfNotExists();
    return table;
  }

  private UserFirebaseDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  public CompletableFuture<Void> insertUserFirebase(
    UUID userId, UUID deviceId, String identifier
  ) {
    return insert(DatabaseRow.of(userId, deviceId, identifier));
  }

  public CompletableFuture<Void> updateUserFirebase(
    UUID userId, UUID deviceId, String content
  ) {
    return update(DatabaseCondition.of("user", userId, "device", deviceId),
      DatabaseRow.of(userId, deviceId, content));
  }

  public CompletableFuture<Void> deleteUserFirebase(
    UUID userId, UUID deviceId
  ) {
    return delete(DatabaseCondition.of("user", userId, "device", deviceId));
  }

  public CompletableFuture<String> findUserFirebaseIdentifier(UUID userId) {
    return selectRow(DatabaseCondition.of("user", userId))
      .thenApply(row -> row.findCell(2).stringValue());
  }

  public CompletableFuture<String> findUserDeviceFirebaseIdentifier(
    UUID userId, UUID deviceId
  ) {
    return selectRow(DatabaseCondition.of("user", userId, "device", deviceId))
      .thenApply(row -> row.findCell(2).stringValue());
  }
}