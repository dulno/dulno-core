package com.dulno.core.stripe;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class StripeDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "stripe";

  public static StripeDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("account", DatabaseDataType.TEXT,
      DatabaseColumn.Type.PRIMARY_KEY));
    columns.add(DatabaseColumn.create("partner", DatabaseDataType.UUID));
    columns.add(DatabaseColumn.create("member", DatabaseDataType.UUID));
    columns.add(DatabaseColumn.create("subscription", DatabaseDataType.TEXT));
    return new StripeDatabaseTable(connection, keyspace, TABLE_NAME, columns);
  }

  private StripeDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  public CompletableFuture<Void> insertStripeAccount(
    String accountId, UUID partnerId, UUID memberId, String subscriptionId
  ) {
    return insert(DatabaseRow.of(accountId, partnerId, memberId, subscriptionId));
  }

  public CompletableFuture<Void> updateStripeAccount(
    String accountId, UUID partnerId, UUID memberId, String subscriptionId
  ) {
    return update(accountId, DatabaseRow.of(accountId, partnerId, memberId,
      subscriptionId));
  }

  public CompletableFuture<Void> deleteStripeAccount(String accountId) {
    return delete(accountId);
  }

  public CompletableFuture<Boolean> stripeAccountExists(String accountId) {
    return exists(accountId);
  }

  public CompletableFuture<Boolean> stripeAccountExistsByPartner(UUID partnerId) {
    return exists(DatabaseCondition.of("partner", partnerId));
  }

  public CompletableFuture<StripeAccount> findStripeAccount(String accountId) {
    return selectRow(accountId).thenApply(StripeAccount::of);
  }

  public CompletableFuture<StripeAccount> findStripeAccountByPartner(UUID partnerId) {
    return selectRow(DatabaseCondition.of("partner", partnerId))
      .thenApply(StripeAccount::of);
  }
}

