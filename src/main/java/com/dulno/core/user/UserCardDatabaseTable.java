package com.dulno.core.user;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class UserCardDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "user_card";

  public static UserCardDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("user", DatabaseDataType.UUID,
      DatabaseColumn.Type.PARTITION_KEY));
    columns.add(DatabaseColumn.create("card", DatabaseDataType.UUID,
      DatabaseColumn.Type.CLUSTERING_KEY));
    columns.add(DatabaseColumn.create("content", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("last_update", DatabaseDataType.BIGINT));
    var table = new UserCardDatabaseTable(connection, keyspace, TABLE_NAME, columns);
    table.createIfNotExists();
    return table;
  }

  private UserCardDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  public CompletableFuture<Void> insertUserCard(UserCard userCard) {
    return insertUserCard(userCard.userId(), userCard.cardId(),
      userCard.content(), userCard.lastUpdate());
  }

  public CompletableFuture<Void> insertUserCard(
    UUID userId, UUID cardId, String content, long lastUpdate
  ) {
    return insert(DatabaseRow.of(userId, cardId, content, lastUpdate));
  }

  public CompletableFuture<Void> updateUserCardContent(
    UUID userId, UUID cardId, String content, long lastUpdate
  ) {
    return update(DatabaseCondition.of("user", userId, "card", cardId),
      DatabaseRow.of(userId, cardId, content, lastUpdate));
  }

  public CompletableFuture<Void> deleteUserCard(UUID userId, UUID cardId) {
    return delete(DatabaseCondition.of("user", userId, "card", cardId));
  }

  public CompletableFuture<Boolean> userCardExists(UUID userId, UUID cardId) {
    return exists(DatabaseCondition.of("user", userId, "card", cardId));
  }

  public CompletableFuture<List<UserCard>> findUserCards(UUID userId) {
    return selectRows(DatabaseCondition.of("user", userId))
      .thenApply(rows -> rows.stream().map(UserCard::of).toList());
  }

  public CompletableFuture<String> findUserCardContent(UUID userId, UUID cardId) {
    return selectRow(DatabaseCondition.of("user", userId, "card", cardId))
      .thenApply(row -> row.findCell(2).stringValue());
  }
}

