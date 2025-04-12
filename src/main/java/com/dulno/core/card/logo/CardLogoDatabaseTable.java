package com.dulno.core.card.logo;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
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
      DatabaseColumn.Type.PARTITION_KEY));
    columns.add(DatabaseColumn.create("id", DatabaseDataType.UUID,
      DatabaseColumn.Type.CLUSTERING_KEY));
    columns.add(DatabaseColumn.create("content", DatabaseDataType.BLOB));
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

  public CompletableFuture<Void> insertCardLogo(
    UUID cardId, UUID logoId, byte[] content
  ) {
    return insert(DatabaseRow.of(cardId, logoId, ByteBuffer.wrap(content)));
  }

  public CompletableFuture<UUID> generateAvailableCardLogoId(UUID cardId) {
    var futureResponse = new CompletableFuture<UUID>();
    var id = UUID.randomUUID();
    cardLogoExists(cardId, id).thenApply(exists -> exists ?
      generateAvailableCardLogoId(cardId).thenApply(futureResponse::complete) :
      CompletableFuture.completedFuture(futureResponse.complete(id)));
    return futureResponse;
  }

  public CompletableFuture<Boolean> cardLogoExists(UUID cardId, UUID logoId) {
    return exists(DatabaseCondition.of("card", cardId, "id", logoId));
  }

  public CompletableFuture<Void> deleteCardLogo(UUID cardId) {
    return delete(DatabaseCondition.of("card", cardId));
  }

  public CompletableFuture<Void> deleteCardLogo(UUID cardId, UUID logoId) {
    return delete(DatabaseCondition.of("card", cardId, "id", logoId));
  }

  public CompletableFuture<UUID> findCardLogoId(UUID cardId) {
    return selectRowColumns(DatabaseCondition.of("card", cardId),
      Lists.newArrayList(findColumnByName("id")))
      .thenApply(row -> row.findCell(0).uuidValue());
  }

  public CompletableFuture<CardLogo> findCardLogo(UUID cardId) {
    return selectRow(DatabaseCondition.of("card", cardId)).thenApply(CardLogo::of);
  }
}