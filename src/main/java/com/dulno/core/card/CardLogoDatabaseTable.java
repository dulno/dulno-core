package com.dulno.core.card;

import com.dulno.core.database.*;
import com.google.common.collect.Lists;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class CardLogoDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "card_logo";

  public static CardLogoDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("card", DatabaseDataType.UUID,
      DatabaseColumn.Type.PRIMARY_KEY));
    columns.add(DatabaseColumn.create("logo", DatabaseDataType.BLOB));
    var table = new CardLogoDatabaseTable(connection, keyspace, TABLE_NAME,
      columns);
    table.createIfNotExists();
    return table;
  }

  private CardLogoDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  public CompletableFuture<Void> insertCardLogo(UUID cardId, byte[] logo) {
    return insert(DatabaseRow.of(cardId, ByteBuffer.wrap(logo)));
  }

  public CompletableFuture<Void> updateCardLogo(UUID cardId, byte[] logo) {
    return update(cardId, DatabaseRow.of(cardId, ByteBuffer.wrap(logo)));
  }

  public CompletableFuture<Void> deleteCardLogo(UUID cardId) {
    return delete(cardId);
  }

  public CompletableFuture<byte[]> findCardLogo(UUID cardId) {
    return selectRow(cardId).thenApply(row -> row.findCell(1).blobValue().array());
  }
}