package com.dulno.core.user;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class UserDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "user";

  public static UserDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("id", DatabaseDataType.UUID,
      DatabaseColumn.Type.PRIMARY_KEY));
    columns.add(DatabaseColumn.create("authentication_key", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("email", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("language", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("compliant", DatabaseDataType.BOOLEAN));
    columns.add(DatabaseColumn.create("newsletter", DatabaseDataType.BOOLEAN));
    columns.add(DatabaseColumn.create("accession", DatabaseDataType.BIGINT));
    var table = new UserDatabaseTable(connection, keyspace, TABLE_NAME, columns);
    table.createIfNotExists();
    table.initializeViews();
    return table;
  }

  private DatabaseTable emailView;

  private UserDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  private void initializeViews() {
    emailView = createMaterializedViewIfNotExists("email_view", "email",
      DatabaseColumn.Type.PARTITION_KEY);
  }

  public CompletableFuture<Void> insertUser(User user) {
    return insertUser(user.id(), user.authenticationKey(), user.email(),
      user.language(), user.compliant(), user.newsletter(), user.accession());
  }

  public CompletableFuture<Void> insertUser(
    UUID id, String authenticationKey, String email, String language,
    boolean compliant, boolean newsletter, long accession
  ) {
    return insert(DatabaseRow.of(id, authenticationKey, email.toLowerCase(),
      language, compliant, newsletter, accession));
  }

  public CompletableFuture<Void> updateUser(User user) {
    return update(user.id(), DatabaseRow.of(user.id(), user.authenticationKey(),
      user.email().toLowerCase(), user.language(), user.compliant(),
      user.newsletter(), user.accession()));
  }

  public CompletableFuture<UUID> generateAvailableUserId() {
    var futureResponse = new CompletableFuture<UUID>();
    var id = UUID.randomUUID();
    userExists(id).thenApply(exists -> exists ?
      generateAvailableUserId().thenApply(futureResponse::complete) :
      CompletableFuture.completedFuture(futureResponse.complete(id)));
    return futureResponse;
  }

  public CompletableFuture<Boolean> userExists(UUID userId) {
    return exists(userId);
  }

  public CompletableFuture<Boolean> userExists(String email) {
    return emailView.exists(DatabaseCondition.of("email", email.toLowerCase()));
  }

  public CompletableFuture<Void> deleteUser(UUID userId) {
    return delete(userId);
  }

  public CompletableFuture<User> findUser(UUID userId) {
    return selectRow(userId).thenApply(row -> User.of(row, this));
  }

  public CompletableFuture<User> findUser(String email) {
    return emailView.selectRow(DatabaseCondition.of("email", email.toLowerCase()))
      .thenApply(row -> User.of(row, emailView));
  }
}

