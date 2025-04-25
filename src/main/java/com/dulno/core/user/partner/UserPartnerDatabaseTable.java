package com.dulno.core.user.partner;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class UserPartnerDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "user_partner";

  public static UserPartnerDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("user", DatabaseDataType.UUID,
      DatabaseColumn.Type.PARTITION_KEY));
    columns.add(DatabaseColumn.create("partner", DatabaseDataType.UUID,
      DatabaseColumn.Type.CLUSTERING_KEY));
    var table = new UserPartnerDatabaseTable(connection, keyspace, TABLE_NAME, columns);
    table.createIfNotExists();
    return table;
  }

  private UserPartnerDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  public CompletableFuture<Void> insertUserPartner(UUID userId, UUID partnerId) {
    return insert(DatabaseRow.of(userId, partnerId));
  }

  public CompletableFuture<Void> deleteUserPartner(UUID userId, UUID partnerId) {
    return delete(DatabaseCondition.of("user", userId, "partner", partnerId));
  }

  public CompletableFuture<Boolean> userPartnerExists(UUID userId, UUID partnerId) {
    return exists(DatabaseCondition.of("user", userId, "partner", partnerId));
  }

  public CompletableFuture<List<UUID>> findUserPartners(UUID userId) {
    return selectRows(DatabaseCondition.of("user", userId))
      .thenApply(rows -> rows.stream().map(row -> row.findCell(1).uuidValue())
        .toList());
  }
}