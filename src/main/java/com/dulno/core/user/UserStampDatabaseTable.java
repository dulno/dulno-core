package com.dulno.core.user;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class UserStampDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "user_stamp";

  public static UserStampDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("user", DatabaseDataType.UUID,
      DatabaseColumn.Type.PARTITION_KEY));
    columns.add(DatabaseColumn.create("stamp", DatabaseDataType.UUID,
      DatabaseColumn.Type.CLUSTERING_KEY));
    columns.add(DatabaseColumn.create("value", DatabaseDataType.INT));
    var table = new UserStampDatabaseTable(connection, keyspace, TABLE_NAME, columns);
    table.createIfNotExists();
    return table;
  }

  private UserStampDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  public CompletableFuture<Void> insertUserStamp(
    UUID userId, UUID stampId, int value
  ) {
    return insert(DatabaseRow.of(userId, stampId, value));
  }

  public CompletableFuture<Void> updateUserStampValue(
    UUID userId, UUID stampId, int value
  ) {
    return update(DatabaseCondition.of("user", userId, "stamp", stampId),
      DatabaseRow.of(userId, stampId, value));
  }

  public void deleteUserStamp(UUID userId, UUID stampId) {
    delete(DatabaseCondition.of("user", userId, "stamp", stampId));
  }

  public CompletableFuture<List<UserStamp>> findUserStamps(UUID userId) {
    return selectRows(DatabaseCondition.of("user", userId))
      .thenApply(rows -> rows.stream().map(UserStamp::of).toList());
  }

  public CompletableFuture<Integer> findUserStampValue(UUID userId, UUID stampId) {
    return selectRow(DatabaseCondition.of("user", userId, "stamp", stampId))
      .thenApply(row -> row.findCell(2).integerValue());
  }
}

