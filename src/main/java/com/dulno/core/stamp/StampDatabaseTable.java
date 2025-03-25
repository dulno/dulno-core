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
    columns.add(DatabaseColumn.create("uid", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("master_key", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("name", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("assigned_card", DatabaseDataType.UUID));
    columns.add(DatabaseColumn.create("creation", DatabaseDataType.BIGINT));
    var table = new StampDatabaseTable(connection, keyspace, TABLE_NAME, columns);
    table.createIfNotExists();
    table.initializeViews();
    return table;
  }

  private DatabaseTable uidView;

  private StampDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  private void initializeViews() {
    uidView = createMaterializedViewIfNotExists("uid_view", "uid");
  }

  public CompletableFuture<Void> insertStamp(Stamp stamp) {
    return insertStamp(stamp.id(), stamp.uid(), stamp.masterKey(),
      stamp.name(), stamp.assignedCard(), stamp.creation());
  }

  public CompletableFuture<Void> insertStamp(
    UUID id, String uid, String masterKey, String name, UUID assignedCard,
    long creation
  ) {
    return insert(DatabaseRow.of(id, uid, masterKey, name, assignedCard, creation));
  }

  public CompletableFuture<Void> changeStampName(UUID stampId, String newName) {
    return findStamp(stampId).thenCompose(stamp -> changeStampName(stamp, newName));
  }

  private CompletableFuture<Void> changeStampName(Stamp stamp, String newName) {
    stamp.changeName(newName);
    return updateStamp(stamp);
  }

  public CompletableFuture<Void> changeStampAssignedCard(
    UUID stampId, UUID newAssignedCard
  ) {
    return findStamp(stampId).thenCompose(stamp ->
      changeStampLanguage(stamp, newAssignedCard));
  }

  private CompletableFuture<Void> changeStampLanguage(
    Stamp stamp, UUID newAssignedCard
  ) {
    stamp.changeAssignedCard(newAssignedCard);
    return updateStamp(stamp);
  }

  private CompletableFuture<Void> updateStamp(Stamp stamp) {
    return update(stamp.id(), DatabaseRow.of(stamp.id(), stamp.uid(),
      stamp.masterKey(), stamp.name(), stamp.assignedCard(), stamp.creation()));
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
    return selectRow(stampId).thenApply(Stamp::of);
  }

  public CompletableFuture<Stamp> findStamp(String stampUid) {
    return uidView.selectRow(DatabaseCondition.of("uid", stampUid))
      .thenApply(Stamp::of);
  }
}
