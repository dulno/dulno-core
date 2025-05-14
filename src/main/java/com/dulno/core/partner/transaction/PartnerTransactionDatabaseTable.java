package com.dulno.core.partner.transaction;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class PartnerTransactionDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "partner_transaction";

  public static PartnerTransactionDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("partner", DatabaseDataType.UUID,
      DatabaseColumn.Type.PARTITION_KEY));
    columns.add(DatabaseColumn.create("id", DatabaseDataType.UUID,
      DatabaseColumn.Type.CLUSTERING_KEY));
    columns.add(DatabaseColumn.create("card", DatabaseDataType.UUID));
    columns.add(DatabaseColumn.create("item", DatabaseDataType.UUID));
    columns.add(DatabaseColumn.create("value", DatabaseDataType.DOUBLE));
    columns.add(DatabaseColumn.create("time", DatabaseDataType.BIGINT));
    var table = new PartnerTransactionDatabaseTable(connection, keyspace,
      TABLE_NAME, columns);
    table.createIfNotExists();
    return table;
  }

  private PartnerTransactionDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns,
      PartnerTransactionDatabaseTableTransformation.create());
  }

  public CompletableFuture<Void> insertPartnerTransaction(
    PartnerTransaction transaction
  ) {
    return insertPartnerTransaction(transaction.partnerId(),
      transaction.transactionId(), transaction.cardId(), transaction.itemId(),
      transaction.value(), transaction.time());
  }

  public CompletableFuture<Void> insertPartnerTransaction(
    UUID partnerId, UUID transactionId, UUID cardId, UUID itemId, double value,
    long time
  ) {
    return insert(DatabaseRow.of(partnerId, transactionId, cardId, itemId,
      value, time));
  }

  public CompletableFuture<Void> deletePartnerTransaction(
    UUID partnerId, UUID transactionId
  ) {
    return delete(DatabaseCondition.of("partner", partnerId, "id", transactionId));
  }

  public CompletableFuture<Void> deletePartnerTransactions(UUID partnerId) {
    return delete(DatabaseCondition.of("partner", partnerId));
  }

  public CompletableFuture<UUID> generateAvailablePartnerTransactionId(
    UUID partnerId
  ) {
    var futureResponse = new CompletableFuture<UUID>();
    var id = UUID.randomUUID();
    partnerTransactionExists(partnerId, id).thenApply(exists -> exists ?
      generateAvailablePartnerTransactionId(partnerId)
        .thenApply(futureResponse::complete) :
      CompletableFuture.completedFuture(futureResponse.complete(id)));
    return futureResponse;
  }

  public CompletableFuture<Boolean> partnerTransactionExists(
    UUID partnerId, UUID transactionId
  ) {
    return exists(DatabaseCondition.of("partner", partnerId, "id", transactionId));
  }

  public CompletableFuture<PartnerTransaction> findPartnerTransaction(
    UUID partnerId, UUID transactionId
  ) {
    return selectRow(DatabaseCondition.of("partner", partnerId, "id", transactionId))
      .thenApply(PartnerTransaction::of);
  }

  public CompletableFuture<List<PartnerTransaction>> findPartnerTransactions(
    UUID partnerId
  ) {
    return selectRows(DatabaseCondition.of("partner", partnerId))
      .thenApply(rows -> rows.stream().map(PartnerTransaction::of).toList());
  }
}
