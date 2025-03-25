package com.dulno.core.card;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class CardDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "card";

  public static CardDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("id", DatabaseDataType.UUID,
      DatabaseColumn.Type.PRIMARY_KEY));
    columns.add(DatabaseColumn.create("partner", DatabaseDataType.UUID));
    columns.add(DatabaseColumn.create("color", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("type", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("content", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("creation", DatabaseDataType.BIGINT));
    var table = new CardDatabaseTable(connection, keyspace, TABLE_NAME, columns);
    table.createIfNotExists();
    table.initializeViews();
    return table;
  }

  private DatabaseTable partnerView;

  private CardDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  private void initializeViews() {
    partnerView = createMaterializedViewIfNotExists("partner_view", "partner",
      DatabaseColumn.Type.PARTITION_KEY);
  }

  public CompletableFuture<Void> insertCard(Card card) {
    return insertCard(card.id(), card.partner(), card.color(),
      card.type().toString(), card.content(), card.creation());
  }

  public CompletableFuture<Void> insertCard(
    UUID id, UUID partner, String color, String type, String content, long creation
  ) {
    return insert(DatabaseRow.of(id, partner, color, type, content, creation));
  }

  public CompletableFuture<Void> updateCard(Card card) {
    return update(card.id(), DatabaseRow.of(card.id(), card.partner(),
      card.color(), card.type().toString(), card.content(), card.creation()));
  }

  public CompletableFuture<UUID> generateAvailableCardId() {
    var futureResponse = new CompletableFuture<UUID>();
    var id = UUID.randomUUID();
    cardExists(id).thenApply(exists -> exists ?
      generateAvailableCardId().thenApply(futureResponse::complete) :
      CompletableFuture.completedFuture(futureResponse.complete(id)));
    return futureResponse;
  }

  public CompletableFuture<Boolean> cardExists(UUID cardId) {
    return exists(cardId);
  }

  public CompletableFuture<Void> deleteCard(UUID cardId) {
    return delete(cardId);
  }

  public CompletableFuture<Card> findCard(UUID cardId) {
    return selectRow(cardId).thenApply(row -> Card.of(row, this));
  }

  public CompletableFuture<List<Card>> findCardsOfPartner(UUID partnerId) {
    return partnerView.selectRows(DatabaseCondition.of("partner", partnerId))
      .thenApply(rows -> rows.stream().map(row -> Card.of(row, partnerView)).toList());
  }
}