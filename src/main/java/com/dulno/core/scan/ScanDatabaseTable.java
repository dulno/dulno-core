package com.dulno.core.scan;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class ScanDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "scan";

  public static ScanDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("id", DatabaseDataType.UUID,
      DatabaseColumn.Type.PARTITION_KEY));
    columns.add(DatabaseColumn.create("picc", DatabaseDataType.TEXT,
      DatabaseColumn.Type.CLUSTERING_KEY));
    columns.add(DatabaseColumn.create("cmac", DatabaseDataType.TEXT,
      DatabaseColumn.Type.CLUSTERING_KEY));
    columns.add(DatabaseColumn.create("user", DatabaseDataType.UUID));
    columns.add(DatabaseColumn.create("stamp", DatabaseDataType.UUID));
    columns.add(DatabaseColumn.create("card", DatabaseDataType.UUID));
    columns.add(DatabaseColumn.create("partner", DatabaseDataType.UUID));
    columns.add(DatabaseColumn.create("time", DatabaseDataType.BIGINT));
    var table = new ScanDatabaseTable(connection, keyspace, TABLE_NAME, columns);
    table.createIfNotExists();
    table.initializeViews();
    return table;
  }

  private DatabaseTable piccCmacView;

  private ScanDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  private void initializeViews() {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("picc", DatabaseDataType.TEXT,
      DatabaseColumn.Type.PARTITION_KEY));
    columns.add(DatabaseColumn.create("cmac", DatabaseDataType.TEXT,
      DatabaseColumn.Type.CLUSTERING_KEY));
    columns.add(DatabaseColumn.create("id", DatabaseDataType.UUID,
      DatabaseColumn.Type.CLUSTERING_KEY));
    piccCmacView = createMaterializedViewIfNotExists("picc_cmac_view", columns);
  }

  public CompletableFuture<Void> insertScan(Scan scan) {
    return insertScan(scan.id(), scan.userId(), scan.stampId(), scan.cardId(),
      scan.partnerId(), scan.picc(), scan.cmac(), scan.time());
  }

  public CompletableFuture<Void> insertScan(
    UUID id, UUID userId, UUID stampId, UUID cardId, UUID partnerId, String picc,
    String cmac, long time
  ) {
    return insert(DatabaseRow.of(id, userId, stampId, cardId, partnerId, picc,
      cmac, time));
  }

  public CompletableFuture<UUID> generateAvailableScanId() {
    var futureResponse = new CompletableFuture<UUID>();
    var id = UUID.randomUUID();
    scanExists(id).thenApply(exists -> exists ?
      generateAvailableScanId().thenApply(futureResponse::complete) :
      CompletableFuture.completedFuture(futureResponse.complete(id)));
    return futureResponse;
  }

  public CompletableFuture<Boolean> scanExists(UUID scanId) {
    return exists(DatabaseCondition.of("id", scanId));
  }

  public CompletableFuture<Boolean> scanExists(String picc, String cmac) {
    return piccCmacView.exists(DatabaseCondition.of("picc", picc, "cmac", cmac));
  }

  public CompletableFuture<Void> deleteScan(UUID scanId) {
    return delete(DatabaseCondition.of("id", scanId));
  }

  public CompletableFuture<Scan> findScan(UUID scanId) {
    return selectRow(DatabaseCondition.of("id", scanId))
      .thenApply(row -> Scan.of(row, this));
  }

  public CompletableFuture<Scan> findScan(String picc, String cmac) {
    return piccCmacView.selectRow(DatabaseCondition.of("picc", picc, "cmac", cmac))
      .thenApply(row -> Scan.of(row, piccCmacView));
  }
}