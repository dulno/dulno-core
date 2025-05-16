package com.dulno.core.partner.analysis.user;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class PartnerActiveUserDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "partner_active_user";

  public static PartnerActiveUserDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("partner", DatabaseDataType.UUID,
      DatabaseColumn.Type.PARTITION_KEY));
    columns.add(DatabaseColumn.create("id", DatabaseDataType.UUID,
      DatabaseColumn.Type.CLUSTERING_KEY));
    columns.add(DatabaseColumn.create("change", DatabaseDataType.INT));
    columns.add(DatabaseColumn.create("date", DatabaseDataType.BIGINT));
    var table = new PartnerActiveUserDatabaseTable(connection, keyspace,
      TABLE_NAME, columns);
    table.createIfNotExists();
    return table;
  }

  private PartnerActiveUserDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  public CompletableFuture<Void> insertPartnerActiveUser(
    UUID partnerId, int change, long date
  ) {
    return generateAvailablePartnerActiveUserId(partnerId)
      .thenCompose(activeUserId -> insert(DatabaseRow.of(partnerId,
        activeUserId, change, date)));
  }

  public CompletableFuture<Void> insertPartnerActiveUser(
    UUID partnerId, UUID activeUserId, int change, long date
  ) {
    return insert(DatabaseRow.of(partnerId, activeUserId, change, date));
  }

  public CompletableFuture<UUID> generateAvailablePartnerActiveUserId(
    UUID partnerId
  ) {
    var futureResponse = new CompletableFuture<UUID>();
    var id = UUID.randomUUID();
    partnerActiveUserExists(partnerId, id).thenApply(exists -> exists ?
      generateAvailablePartnerActiveUserId(partnerId)
        .thenApply(futureResponse::complete) :
      CompletableFuture.completedFuture(futureResponse.complete(id)));
    return futureResponse;
  }

  public CompletableFuture<Boolean> partnerActiveUserExists(
    UUID partnerId, UUID activeUserId
  ) {
    return exists(DatabaseCondition.of("partner", partnerId, "id", activeUserId));
  }

  public CompletableFuture<Void> deletePartnerActiveUsers(UUID partnerId) {
    return delete(DatabaseCondition.of("partner", partnerId));
  }

  public CompletableFuture<List<PartnerActiveUserEntry>> findPartnerActiveUsers(
    UUID partnerId
  ) {
    return selectRows(DatabaseCondition.of("partner", partnerId))
      .thenApply(rows -> rows.stream().map(PartnerActiveUserEntry::of).toList());
  }
}