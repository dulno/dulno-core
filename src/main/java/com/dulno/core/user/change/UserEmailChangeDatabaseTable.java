package com.dulno.core.user.change;

import com.dulno.core.database.*;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class UserEmailChangeDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "user_email_change";

  public static UserEmailChangeDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("user", DatabaseDataType.UUID,
      DatabaseColumn.Type.PRIMARY_KEY));
    columns.add(DatabaseColumn.create("email", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("code", DatabaseDataType.TEXT));
    var table = new UserEmailChangeDatabaseTable(connection, keyspace,
      TABLE_NAME, columns);
    table.createIfNotExists();
    return table;
  }

  private UserEmailChangeDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  public CompletableFuture<Void> insertUserEmailChange(
    UUID userId, String email, String code
  ) {
    return insert(DatabaseRow.of(userId, email, code),
      "USING TTL " + (60 * 60 * 24));
  }

  public CompletableFuture<Boolean> userEmailChangeExists(UUID userId) {
    return exists(userId);
  }

  public CompletableFuture<Void> deleteUserEmailChange(UUID userId) {
    return delete(userId);
  }

  public CompletableFuture<UserEmailChange> findUserEmailChange(UUID userId) {
    return selectRow(userId).thenApply(UserEmailChange::of);
  }
}