package com.dulno.core.user.statistic;

import com.dulno.core.database.*;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class UserAppOpenDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "user_app_open";

  public static UserAppOpenDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("id", DatabaseDataType.UUID,
      DatabaseColumn.Type.PRIMARY_KEY));
    columns.add(DatabaseColumn.create("date", DatabaseDataType.BIGINT));
    columns.add(DatabaseColumn.create("version", DatabaseDataType.TEXT));
    var table = new UserAppOpenDatabaseTable(connection, keyspace, TABLE_NAME,
      columns);
    table.createIfNotExists();
    return table;
  }

  private UserAppOpenDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns,
      UserAppOpenDatabaseTableTransformation.create());
  }

  public CompletableFuture<Void> insertUserAppOpen(long date, String version) {
    return generateAvailableUserAppOpenId()
      .thenCompose(appOpenId -> insert(DatabaseRow.of(appOpenId, date, version)));
  }

  public CompletableFuture<Void> insertUserAppOpen(
    UUID appOpenId, long date, String version
  ) {
    return insert(DatabaseRow.of(appOpenId, date, version));
  }

  public CompletableFuture<UUID> generateAvailableUserAppOpenId() {
    var futureResponse = new CompletableFuture<UUID>();
    var id = UUID.randomUUID();
    userAppOpenExists(id).thenApply(exists -> exists ?
      generateAvailableUserAppOpenId().thenApply(futureResponse::complete) :
      CompletableFuture.completedFuture(futureResponse.complete(id)));
    return futureResponse;
  }

  public CompletableFuture<Boolean> userAppOpenExists(UUID appOpenId) {
    return exists(appOpenId);
  }

  public CompletableFuture<Void> deleteUserAppOpen(UUID appOpenId) {
    return delete(appOpenId);
  }

  public CompletableFuture<List<UserAppOpen>> findAllUserAppOpenings() {
    return selectAllRows().thenApply(rows ->
      rows.stream().map(UserAppOpen::of).toList());
  }
}