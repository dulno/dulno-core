package com.dulno.core.partner.analysis.stamp;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class PartnerOpenStampDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "partner_open_stamp";

  public static PartnerOpenStampDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("partner", DatabaseDataType.UUID,
      DatabaseColumn.Type.PARTITION_KEY));
    columns.add(DatabaseColumn.create("id", DatabaseDataType.UUID,
      DatabaseColumn.Type.CLUSTERING_KEY));
    columns.add(DatabaseColumn.create("change", DatabaseDataType.INT));
    columns.add(DatabaseColumn.create("date", DatabaseDataType.BIGINT));
    var table = new PartnerOpenStampDatabaseTable(connection, keyspace, TABLE_NAME,
      columns);
    table.createIfNotExists();
    return table;
  }

  private PartnerOpenStampDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  public CompletableFuture<Void> insertPartnerOpenStamp(
    UUID partnerId, int change, long date
  ) {
    return generateAvailablePartnerOpenStampId(partnerId)
      .thenCompose(openStampId -> insert(DatabaseRow.of(partnerId,
        openStampId, change, date)));
  }

  public CompletableFuture<Void> insertPartnerOpenStamp(
    UUID partnerId, UUID openStampId, int change, long date
  ) {
    return insert(DatabaseRow.of(partnerId, openStampId, change, date));
  }

  public CompletableFuture<UUID> generateAvailablePartnerOpenStampId(
    UUID partnerId
  ) {
    var futureResponse = new CompletableFuture<UUID>();
    var id = UUID.randomUUID();
    partnerOpenStampExists(partnerId, id).thenApply(exists -> exists ?
      generateAvailablePartnerOpenStampId(partnerId)
        .thenApply(futureResponse::complete) :
      CompletableFuture.completedFuture(futureResponse.complete(id)));
    return futureResponse;
  }

  public CompletableFuture<Boolean> partnerOpenStampExists(
    UUID partnerId, UUID openStampId
  ) {
    return exists(DatabaseCondition.of("partner", partnerId, "id", openStampId));
  }

  public CompletableFuture<Void> deletePartnerOpenStamps(UUID partnerId) {
    return delete(DatabaseCondition.of("partner", partnerId));
  }

  public CompletableFuture<List<PartnerOpenStampEntry>> findPartnerOpenStamps(
    UUID partnerId
  ) {
    return selectRows(DatabaseCondition.of("partner", partnerId))
      .thenApply(rows -> rows.stream().map(PartnerOpenStampEntry::of).toList());
  }
}
