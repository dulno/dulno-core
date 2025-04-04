package com.dulno.core.partner;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class PartnerLogoDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "partner_logo";

  public static PartnerLogoDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("partner", DatabaseDataType.UUID,
      DatabaseColumn.Type.PARTITION_KEY));
    columns.add(DatabaseColumn.create("id", DatabaseDataType.UUID,
      DatabaseColumn.Type.CLUSTERING_KEY));
    columns.add(DatabaseColumn.create("content", DatabaseDataType.BLOB));
    var table = new PartnerLogoDatabaseTable(connection, keyspace, TABLE_NAME,
      columns);
    table.createIfNotExists();
    return table;
  }

  private PartnerLogoDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  public CompletableFuture<Void> insertPartnerLogo(
    UUID partnerId, UUID logoId, byte[] content
  ) {
    return insert(DatabaseRow.of(partnerId, logoId, ByteBuffer.wrap(content)));
  }

  public CompletableFuture<UUID> generateAvailablePartnerLogoId(UUID partnerId) {
    var futureResponse = new CompletableFuture<UUID>();
    var id = UUID.randomUUID();
    partnerLogoExists(partnerId, id).thenApply(exists -> exists ?
      generateAvailablePartnerLogoId(partnerId).thenApply(futureResponse::complete) :
      CompletableFuture.completedFuture(futureResponse.complete(id)));
    return futureResponse;
  }

  public CompletableFuture<Boolean> partnerLogoExists(UUID partnerId, UUID logoId) {
    return exists(DatabaseCondition.of("partner", partnerId, "id", logoId));
  }

  public CompletableFuture<Void> deletePartnerLogo(UUID partnerId) {
    return delete(DatabaseCondition.of("partner", partnerId));
  }

  public CompletableFuture<Void> deletePartnerLogo(UUID partnerId, UUID logoId) {
    return delete(DatabaseCondition.of("partner", partnerId, "id", logoId));
  }

  public CompletableFuture<UUID> findPartnerLogoId(UUID partnerId) {
    return selectRowColumns(DatabaseCondition.of("partner", partnerId),
      Lists.newArrayList(findColumnByName("id")))
      .thenApply(row -> row.findCell(0).uuidValue());
  }

  public CompletableFuture<PartnerLogo> findPartnerLogo(UUID partnerId) {
    return selectRow(DatabaseCondition.of("partner", partnerId))
      .thenApply(PartnerLogo::of);
  }
}