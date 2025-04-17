package com.dulno.core.partner.analysis;

import com.datastax.oss.driver.shaded.guava.common.collect.Maps;
import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class PartnerCardUsageDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "partner_card_usage";

  public static PartnerCardUsageDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("partner", DatabaseDataType.UUID,
      DatabaseColumn.Type.PARTITION_KEY));
    columns.add(DatabaseColumn.create("state", DatabaseDataType.TEXT,
      DatabaseColumn.Type.CLUSTERING_KEY));
    columns.add(DatabaseColumn.create("number", DatabaseDataType.COUNTER));
    var table = new PartnerCardUsageDatabaseTable(connection, keyspace,
      TABLE_NAME, columns);
    table.createIfNotExists();;
    return table;
  }

  private PartnerCardUsageDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  public CompletableFuture<Void> addPartnerCardUsage(
    UUID partnerId, PartnerCardState state
  ) {
    return updatePartnerCardUsage(partnerId, state, 1);
  }

  public CompletableFuture<Void> removePartnerCardUsage(
    UUID partnerId, PartnerCardState state
  ) {
    return updatePartnerCardUsage(partnerId, state, -1);
  }

  private CompletableFuture<Void> updatePartnerCardUsage(
    UUID partnerId, PartnerCardState state, long numberAddition
  ) {
    var condition = DatabaseCondition.of("partner", partnerId,
      "state", state.toString());
    return updateCounter(condition, DatabaseRow.of(partnerId, state.toString(),
      numberAddition));
  }

  public CompletableFuture<Void> deletePartnerCardUsage(
    UUID partnerId, PartnerCardState state
  ) {
    return delete(DatabaseCondition.of("partner", partnerId,
      "state", state.toString()));
  }

  public CompletableFuture<Void> deletePartnerCardUsages(UUID partnerId) {
    return delete(DatabaseCondition.of("partner", partnerId));
  }

  public CompletableFuture<Boolean> partnerCardUsageExists(
    UUID partnerId, PartnerCardState state
  ) {
    return exists(DatabaseCondition.of("partner", partnerId,
      "state", state.toString()));
  }

  public CompletableFuture<Long> findPartnerCardUsageNumber(
    UUID partnerId, PartnerCardState state
  ) {
    var condition = DatabaseCondition.of("partner", partnerId,
      "state", state.toString());
    return selectRow(condition).thenApply(row -> row.findCell(2).longValue());
  }

  public CompletableFuture<Map<PartnerCardState, Long>> findPartnerCardUsages(
    UUID partnerId
  ) {
    return selectRows(DatabaseCondition.of("partner", partnerId))
      .thenApply(this::assemblyPartnerCardUsages);
  }

  private Map<PartnerCardState, Long> assemblyPartnerCardUsages(
    List<DatabaseRow> rows
  ) {
    var states = Maps.<PartnerCardState, Long>newHashMap();
    for (DatabaseRow row : rows) {
      states.put(PartnerCardState.valueOf(row.findCell(1).stringValue()),
        row.findCell(2).longValue());
    }
    return states;
  }
}