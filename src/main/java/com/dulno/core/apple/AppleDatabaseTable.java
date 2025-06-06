package com.dulno.core.apple;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class AppleDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "apple";

  public static AppleDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("subject", DatabaseDataType.TEXT,
      DatabaseColumn.Type.PRIMARY_KEY));
    columns.add(DatabaseColumn.create("email", DatabaseDataType.TEXT));
    var table = new AppleDatabaseTable(connection, keyspace, TABLE_NAME,
      columns);
    table.createIfNotExists();
    return table;
  }

  private AppleDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  public CompletableFuture<Void> insertAppleSubject(String subject, String email) {
    return insert(DatabaseRow.of(subject, email));
  }

  public CompletableFuture<Void> deleteAppleSubject(String subject) {
    return delete(DatabaseCondition.of("subject", subject));
  }

  public CompletableFuture<Boolean> appleSubjectExists(String subject) {
    return exists(DatabaseCondition.of("subject", subject));
  }

  public CompletableFuture<String> findAppleSubjectEmail(String subject) {
    return selectRow(DatabaseCondition.of("subject", subject))
      .thenApply(row -> row.findCell(1).stringValue());
  }
}
