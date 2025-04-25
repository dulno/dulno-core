package com.dulno.core.stamp;

import com.dulno.core.database.*;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class StampSetupDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "stamp_setup";

  public static StampSetupDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("stamp", DatabaseDataType.UUID,
      DatabaseColumn.Type.PRIMARY_KEY));
    columns.add(DatabaseColumn.create("setup_token", DatabaseDataType.TEXT));
    var table = new StampSetupDatabaseTable(connection, keyspace,
      TABLE_NAME, columns);
    table.createIfNotExists();
    return table;
  }

  private StampSetupDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  public void insertStampSetup(UUID stampId, String token) {
    insert(DatabaseRow.of(stampId, token), "USING TTL " + (60 * 5));
  }

  public CompletableFuture<Boolean> stampSetupExists(UUID stampId) {
    return exists(stampId);
  }

  public void deleteStampSetup(UUID stampId) {
    delete(stampId);
  }

  public CompletableFuture<String> findStampSetupToken(UUID stampId) {
    return selectRow(stampId).thenApply(row ->
      row.findCell(1).stringValue());
  }
}
