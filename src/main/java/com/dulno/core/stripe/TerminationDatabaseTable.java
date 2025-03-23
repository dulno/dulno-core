package com.dulno.core.stripe;

import com.dulno.core.database.*;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class TerminationDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "stripe_termination";

  public static TerminationDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("partner", DatabaseDataType.UUID,
      DatabaseColumn.Type.PRIMARY_KEY));
    return new TerminationDatabaseTable(connection, keyspace, TABLE_NAME, columns);
  }

  private TerminationDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  public CompletableFuture<Void> insertTermination(UUID partnerId) {
    return insert(DatabaseRow.of(partnerId));
  }

  public CompletableFuture<Void> deleteTermination(UUID partnerId) {
    return delete(partnerId);
  }

  public CompletableFuture<Boolean> terminationExists(UUID partnerId) {
    return exists(partnerId);
  }
}
