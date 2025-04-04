package com.dulno.core.user;

import com.dulno.core.database.*;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class UserVerificationDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "user_verification";

  public static UserVerificationDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("user", DatabaseDataType.UUID,
      DatabaseColumn.Type.PRIMARY_KEY));
    columns.add(DatabaseColumn.create("email", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("code", DatabaseDataType.TEXT));
    var table = new UserVerificationDatabaseTable(connection, keyspace,
      TABLE_NAME, columns);
    table.createIfNotExists();
    return table;
  }

  private UserVerificationDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  public CompletableFuture<Void> insertUserVerification(
    UUID userId, String email, String code
  ) {
    return insert(DatabaseRow.of(userId, email, code),
      "USING TTL " + (60 * 60 * 24));
  }

  public CompletableFuture<Boolean> userVerificationExists(UUID userId) {
    return exists(userId);
  }

  public CompletableFuture<Void> deleteUserVerification(UUID userId) {
    return delete(userId);
  }

  public CompletableFuture<UserVerification> findUserVerification(UUID userId) {
    return selectRow(userId).thenApply(UserVerification::of);
  }
}