package com.dulno.core.partner.report;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public final class PartnerReportDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "partner_report";

  public static PartnerReportDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("report", DatabaseDataType.UUID,
      DatabaseColumn.Type.PRIMARY_KEY));
    columns.add(DatabaseColumn.create("partner", DatabaseDataType.UUID));
    columns.add(DatabaseColumn.create("message", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("status", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("creation", DatabaseDataType.BIGINT));
    var table = new PartnerReportDatabaseTable(connection, keyspace, TABLE_NAME,
      columns);
    table.createIfNotExists();
    table.createIndexIfNotExists("status");
    return table;
  }

  private PartnerReportDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  public CompletableFuture<Void> insertPartnerReport(PartnerReport report) {
    return insertPartnerReport(report.reportId(), report.partnerId(),
      report.message(), report.status().toString(), report.creation());
  }

  public CompletableFuture<Void> insertPartnerReport(
    UUID reportId, UUID partnerId, String message, String status, long creation
  ) {
    return insert(DatabaseRow.of(reportId, partnerId, message, status, creation));
  }

  public CompletableFuture<Void> updatePartnerReport(PartnerReport report) {
    var condition = DatabaseCondition.of("report", report.reportId());
    return update(condition, DatabaseRow.of(report.reportId(), report.partnerId(),
      report.message(), report.status().toString(), report.creation()));
  }

  public CompletableFuture<UUID> generateAvailablePartnerReportId() {
    var futureResponse = new CompletableFuture<UUID>();
    var id = UUID.randomUUID();
    partnerReportExists(id).thenApply(exists -> exists ?
      generateAvailablePartnerReportId().thenApply(futureResponse::complete) :
      CompletableFuture.completedFuture(futureResponse.complete(id)));
    return futureResponse;
  }

  public CompletableFuture<Void> deletePartnerReport(UUID reportId) {
    return delete(DatabaseCondition.of("report", reportId));
  }

  public CompletableFuture<Boolean> partnerReportExists(UUID reportId) {
    return exists(DatabaseCondition.of("report", reportId));
  }

  public CompletableFuture<PartnerReport> findPartnerReport(UUID reportId) {
    return selectRow(DatabaseCondition.of("report", reportId))
      .thenApply(PartnerReport::of);
  }

  public CompletableFuture<List<PartnerReport>> findOpenReports() {
    var condition = DatabaseCondition.of("status",
      PartnerReport.Status.OPEN.toString());
    return selectRows(condition)
      .thenApply(rows -> rows.stream().map(PartnerReport::of)
        .collect(Collectors.toList()));
  }

  public CompletableFuture<Long> countPendingReports() {
    var condition = DatabaseCondition.of("status",
      PartnerReport.Status.OPEN.toString());
    return count(condition);
  }
}
