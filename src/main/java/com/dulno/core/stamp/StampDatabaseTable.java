package com.dulno.core.stamp;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class StampDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "stamp";

  public static StampDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("id", DatabaseDataType.UUID,
      DatabaseColumn.Type.PRIMARY_KEY));
    columns.add(DatabaseColumn.create("partner", DatabaseDataType.UUID));
    columns.add(DatabaseColumn.create("uid", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("master_key", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("name", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("type", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("state", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("assigned_card", DatabaseDataType.UUID));
    columns.add(DatabaseColumn.create("assignment_role", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("creation", DatabaseDataType.BIGINT));
    var table = new StampDatabaseTable(connection, keyspace, TABLE_NAME, columns);
    table.createIfNotExists();
    table.initializeViews();
    return table;
  }

  private DatabaseTable uidView;
  private DatabaseTable partnerView;

  private StampDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  private void initializeViews() {
    uidView = createMaterializedViewIfNotExists("uid_view", "uid",
      DatabaseColumn.Type.PARTITION_KEY);
    partnerView = createMaterializedViewIfNotExists("partner_view", "partner",
      DatabaseColumn.Type.PARTITION_KEY);
  }

  public CompletableFuture<Void> insertStamp(Stamp stamp) {
    return insertStamp(stamp.id(), stamp.partnerId(), stamp.uid(),
      stamp.masterKey(), stamp.name(), stamp.type().toString(),
      stamp.state().toString(), stamp.assignedCard(),
      stamp.assignmentRole().toString(), stamp.creation());
  }

  public CompletableFuture<Void> insertStamp(
    UUID id, UUID partnerId, String uid, String masterKey, String name,
    String type, String state, UUID assignedCard, String assignmentRole,
    long creation
  ) {
    return insert(DatabaseRow.of(id, partnerId, uid, masterKey, name, type, state,
      assignedCard, assignmentRole, creation));
  }

  public CompletableFuture<Void> updateStamp(Stamp stamp) {
    return update(stamp.id(), DatabaseRow.of(stamp.id(), stamp.partnerId(),
      stamp.uid(), stamp.masterKey(), stamp.name(), stamp.type().toString(),
      stamp.state().toString(), stamp.assignedCard(),
      stamp.assignmentRole().toString(), stamp.creation()));
  }

  public CompletableFuture<UUID> generateAvailableStampId() {
    var futureResponse = new CompletableFuture<UUID>();
    var id = UUID.randomUUID();
    stampExists(id).thenApply(exists -> exists ?
      generateAvailableStampId().thenApply(futureResponse::complete) :
      CompletableFuture.completedFuture(futureResponse.complete(id)));
    return futureResponse;
  }

  public CompletableFuture<Boolean> stampExists(UUID stampId) {
    return exists(stampId);
  }

  public CompletableFuture<Boolean> stampExists(String stampUid) {
    return uidView.exists(DatabaseCondition.of("uid", stampUid));
  }

  public CompletableFuture<Void> deleteStamp(UUID stampId) {
    return delete(stampId);
  }

  public CompletableFuture<Stamp> findStamp(UUID stampId) {
    return selectRow(stampId).thenApply(row -> Stamp.of(row, this));
  }

  public CompletableFuture<Stamp> findStamp(String stampUid) {
    return uidView.selectRow(DatabaseCondition.of("uid", stampUid))
      .thenApply(row -> Stamp.of(row, uidView));
  }

  public CompletableFuture<List<Stamp>> findStampsOfPartner(UUID partnerId) {
    return partnerView.selectRows(DatabaseCondition.of("partner", partnerId))
      .thenApply(rows -> rows.stream().map(row -> Stamp.of(row, partnerView))
        .toList());
  }
}
