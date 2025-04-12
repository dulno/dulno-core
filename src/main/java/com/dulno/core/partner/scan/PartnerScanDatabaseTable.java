package com.dulno.core.partner.scan;

import com.dulno.core.database.*;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class PartnerScanDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "partner_scan";

  public static PartnerScanDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("partner", DatabaseDataType.UUID,
      DatabaseColumn.Type.PRIMARY_KEY));
    columns.add(DatabaseColumn.create("scans", DatabaseDataType.COUNTER));
    var table = new PartnerScanDatabaseTable(connection, keyspace, TABLE_NAME,
      columns);
    table.createIfNotExists();;
    return table;
  }

  private PartnerScanDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  public CompletableFuture<Void> addPartnerScan(UUID partnerId) {
    return updatePartnerScans(partnerId, 1);
  }

  private CompletableFuture<Void> updatePartnerScans(
    UUID partnerId, long scansAddition
  ) {
    return updateCounter(partnerId, DatabaseRow.of(partnerId, scansAddition));
  }

  public CompletableFuture<Void> deletePartnerScans(UUID partnerId) {
    return delete(partnerId);
  }

  public CompletableFuture<Boolean> partnerScansExists(UUID partnerId) {
    return exists(partnerId);
  }

  public CompletableFuture<Long> findPartnerScans(UUID partnerId) {
    return selectRow(partnerId).thenApply(row -> row.findCell(1).longValue());
  }
}
