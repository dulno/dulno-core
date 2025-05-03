package com.dulno.core.user.statistic;

import com.dulno.core.database.*;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class UserJoinDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "user_join";

  public static UserJoinDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("id", DatabaseDataType.UUID,
      DatabaseColumn.Type.PRIMARY_KEY));
    columns.add(DatabaseColumn.create("date", DatabaseDataType.BIGINT));
    var table = new UserJoinDatabaseTable(connection, keyspace, TABLE_NAME,
      columns);
    table.createIfNotExists();
    return table;
  }

  private UserJoinDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  public CompletableFuture<Void> insertUserJoin(long date) {
    return generateAvailableUserJoinId()
      .thenCompose(joinId -> insert(DatabaseRow.of(joinId, date)));
  }

  public CompletableFuture<Void> insertUserJoin(
    UUID joinId, long date
  ) {
    return insert(DatabaseRow.of(joinId, date));
  }

  public CompletableFuture<UUID> generateAvailableUserJoinId() {
    var futureResponse = new CompletableFuture<UUID>();
    var id = UUID.randomUUID();
    userJoinExists(id).thenApply(exists -> exists ?
      generateAvailableUserJoinId().thenApply(futureResponse::complete) :
      CompletableFuture.completedFuture(futureResponse.complete(id)));
    return futureResponse;
  }

  public CompletableFuture<Boolean> userJoinExists(UUID joinId) {
    return exists(joinId);
  }

  public CompletableFuture<Void> deleteUserJoin(UUID joinId) {
    return delete(joinId);
  }

  public CompletableFuture<List<Long>> findAllUserJoins() {
    return selectAllRows()
      .thenApply(rows -> rows.stream().map(row -> row.findCell(0).longValue())
        .toList());
  }
}