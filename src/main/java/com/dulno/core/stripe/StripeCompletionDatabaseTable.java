package com.dulno.core.stripe;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class StripeCompletionDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "stripe_completion";

  public static StripeCompletionDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("member", DatabaseDataType.UUID,
      DatabaseColumn.Type.PARTITION_KEY));
    columns.add(DatabaseColumn.create("completion_token", DatabaseDataType.TEXT,
      DatabaseColumn.Type.CLUSTERING_KEY));
    columns.add(DatabaseColumn.create("confirmed", DatabaseDataType.BOOLEAN));
    return new StripeCompletionDatabaseTable(connection, keyspace, TABLE_NAME, columns);
  }

  private StripeCompletionDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  public void insertStripeCompletion(
    UUID memberId, String completionToken
  ) {
    insert(DatabaseRow.of(memberId, completionToken, false),
      "USING TTL " + (60 * 60));
  }

  public CompletableFuture<Void> confirmStripeCompletion(
    UUID memberId, String completionToken
  ) {
    var condition = DatabaseCondition.of("member", memberId,
      "completion_token", completionToken);
    return update(condition, DatabaseRow.of(memberId, completionToken, true),
      "USING TTL " + (60 * 60));
  }

  public void deleteStripeCompletion(UUID memberId, String completionToken) {
    delete(DatabaseCondition.of("member", memberId,
      "completion_token", completionToken));
  }

  public CompletableFuture<Boolean> stripeCompletionExists(
    UUID memberId, String completionToken
  ) {
    return exists(DatabaseCondition.of("member", memberId,
      "completion_token", completionToken));
  }

  public CompletableFuture<Boolean> isStripeCompletionConfirmed(
    UUID memberId, String completionToken
  ) {
    return selectRow(DatabaseCondition.of("member", memberId,
      "completion_token", completionToken))
      .thenApply(row -> row.findCell(2).booleanValue());
  }
}

