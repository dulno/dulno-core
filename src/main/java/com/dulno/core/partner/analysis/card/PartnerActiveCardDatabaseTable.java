package com.dulno.core.partner.analysis.card;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class PartnerActiveCardDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "partner_active_card";

  public static PartnerActiveCardDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("partner", DatabaseDataType.UUID,
      DatabaseColumn.Type.PARTITION_KEY));
    columns.add(DatabaseColumn.create("id", DatabaseDataType.UUID,
      DatabaseColumn.Type.CLUSTERING_KEY));
    columns.add(DatabaseColumn.create("change", DatabaseDataType.INT));
    columns.add(DatabaseColumn.create("date", DatabaseDataType.BIGINT));
    var table = new PartnerActiveCardDatabaseTable(connection, keyspace, TABLE_NAME,
      columns);
    table.createIfNotExists();
    return table;
  }

  private PartnerActiveCardDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  public CompletableFuture<Void> insertPartnerActiveCard(
    UUID partnerId, int change, long date
  ) {
    return generateAvailablePartnerActiveCardId(partnerId)
      .thenCompose(activeCardId -> insert(DatabaseRow.of(partnerId,
        activeCardId, change, date)));
  }

  public CompletableFuture<Void> insertPartnerActiveCard(
    UUID partnerId, UUID activeCardId, int change, long date
  ) {
    return insert(DatabaseRow.of(partnerId, activeCardId, change, date));
  }

  public CompletableFuture<UUID> generateAvailablePartnerActiveCardId(
    UUID partnerId
  ) {
    var futureResponse = new CompletableFuture<UUID>();
    var id = UUID.randomUUID();
    partnerActiveCardExists(partnerId, id).thenApply(exists -> exists ?
      generateAvailablePartnerActiveCardId(partnerId)
        .thenApply(futureResponse::complete) :
      CompletableFuture.completedFuture(futureResponse.complete(id)));
    return futureResponse;
  }

  public CompletableFuture<Boolean> partnerActiveCardExists(
    UUID partnerId, UUID activeCardId
  ) {
    return exists(DatabaseCondition.of("partner", partnerId, "id", activeCardId));
  }

  public CompletableFuture<Void> deletePartnerActiveCards(UUID partnerId) {
    return delete(DatabaseCondition.of("partner", partnerId));
  }

  public CompletableFuture<List<PartnerActiveCardEntry>> findPartnerActiveCards(
    UUID partnerId
  ) {
    return selectRows(DatabaseCondition.of("partner", partnerId))
      .thenApply(rows -> rows.stream().map(PartnerActiveCardEntry::of).toList());
  }
}