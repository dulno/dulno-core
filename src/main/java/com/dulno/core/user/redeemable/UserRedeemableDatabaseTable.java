package com.dulno.core.user.redeemable;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class UserRedeemableDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "user_redeemable";

  public static UserRedeemableDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("user", DatabaseDataType.UUID,
      DatabaseColumn.Type.PARTITION_KEY));
    columns.add(DatabaseColumn.create("redeemable", DatabaseDataType.UUID,
      DatabaseColumn.Type.CLUSTERING_KEY));
    var table = new UserRedeemableDatabaseTable(connection, keyspace, TABLE_NAME, columns);
    table.createIfNotExists();
    table.initializeViews();
    return table;
  }

  private DatabaseTable redeemableView;

  private UserRedeemableDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  private void initializeViews() {
    redeemableView = createMaterializedViewIfNotExists("redeemable_view", "redeemable",
      DatabaseColumn.Type.PARTITION_KEY);
  }

  public CompletableFuture<Void> insertUserRedeemable(
    UUID userId, UUID redeemableId
  ) {
    return insert(DatabaseRow.of(userId, redeemableId));
  }

  public CompletableFuture<Void> deleteUserRedeemable(
    UUID userId, UUID redeemableId
  ) {
    return delete(DatabaseCondition.of("user", userId,
      "redeemable", redeemableId));
  }

  public CompletableFuture<Boolean> userRedeemableExists(
    UUID userId, UUID redeemableId
  ) {
    return exists(DatabaseCondition.of("user", userId,
      "redeemable", redeemableId));
  }

  public CompletableFuture<Boolean> userRedeemableExists(UUID redeemableId) {
    return redeemableView.exists(DatabaseCondition.of(
      "redeemable", redeemableId));
  }

  public CompletableFuture<UUID> findRedeemableUser(UUID redeemableId) {
    var condition = DatabaseCondition.of("redeemable", redeemableId);
    return redeemableView.selectRow(condition)
      .thenApply(row -> row.findCell(1).uuidValue());
  }

  public CompletableFuture<List<UUID>> findAllUserRedeemable(UUID userId) {
    return selectRows(DatabaseCondition.of("user", userId))
      .thenApply(rows -> rows.stream().map(row -> row.findCell(1).uuidValue())
        .toList());
  }
}

