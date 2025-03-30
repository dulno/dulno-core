package com.dulno.core.partner;

import com.dulno.core.database.*;
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
      DatabaseColumn.Type.PRIMARY_KEY));
    columns.add(DatabaseColumn.create("logo", DatabaseDataType.BLOB));
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

  public CompletableFuture<Void> insertPartnerLogo(UUID partnerId, byte[] logo) {
    return insert(DatabaseRow.of(partnerId, ByteBuffer.wrap(logo)));
  }

  public CompletableFuture<Void> updatePartnerLogo(UUID partnerId, byte[] logo) {
    return update(partnerId, DatabaseRow.of(partnerId, ByteBuffer.wrap(logo)));
  }

  public CompletableFuture<Void> deletePartnerLogo(UUID partnerId) {
    return delete(partnerId);
  }

  public CompletableFuture<byte[]> findPartnerLogo(UUID partnerId) {
    return selectRow(partnerId).thenApply(row -> row.findCell(1).blobValue().array());
  }
}