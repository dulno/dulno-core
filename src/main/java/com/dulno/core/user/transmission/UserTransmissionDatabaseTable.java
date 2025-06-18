package com.dulno.core.user.transmission;

import com.dulno.core.database.*;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class UserTransmissionDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "user_transmission";

  public static UserTransmissionDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("id", DatabaseDataType.UUID,
      DatabaseColumn.Type.PRIMARY_KEY));
    columns.add(DatabaseListColumn.create("items", DatabaseDataType.UUID));
    var table = new UserTransmissionDatabaseTable(connection, keyspace,
      TABLE_NAME, columns);
    table.createIfNotExists();
    return table;
  }

  private UserTransmissionDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  public CompletableFuture<Void> insertUserTransmission(
    UUID id, List<UUID> items
  ) {
    return insert(DatabaseRow.of(id, items), "USING TTL " + (60 * 5));
  }

  public CompletableFuture<UUID> generateAvailableTransmissionId() {
    var futureResponse = new CompletableFuture<UUID>();
    var id = UUID.randomUUID();
    userTransmissionExists(id).thenApply(exists -> exists ?
      generateAvailableTransmissionId().thenApply(futureResponse::complete) :
      CompletableFuture.completedFuture(futureResponse.complete(id)));
    return futureResponse;
  }

  public CompletableFuture<Boolean> userTransmissionExists(UUID id) {
    return exists(id);
  }

  public CompletableFuture<Void> deleteUserTransmission(UUID id) {
    return delete(id);
  }

  public CompletableFuture<List<UUID>> findUserTransmission(UUID id) {
    return selectRow(id).thenApply(row -> row.findCell(1).listValue());
  }
}