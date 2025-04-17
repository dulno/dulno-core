package com.dulno.core.partner.analysis;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class PartnerNewUserDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "partner_new_user";

  public static PartnerNewUserDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("partner", DatabaseDataType.UUID,
      DatabaseColumn.Type.PARTITION_KEY));
    columns.add(DatabaseColumn.create("id", DatabaseDataType.UUID,
      DatabaseColumn.Type.CLUSTERING_KEY));
    columns.add(DatabaseColumn.create("date", DatabaseDataType.BIGINT));
    var table = new PartnerNewUserDatabaseTable(connection, keyspace, TABLE_NAME,
      columns);
    table.createIfNotExists();
    return table;
  }

  private PartnerNewUserDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  public CompletableFuture<Void> insertPartnerNewUser(
    UUID partnerId, UUID scanId, long date
  ) {
    return insert(DatabaseRow.of(partnerId, scanId, date));
  }

  public CompletableFuture<Void> deletePartnerNewUsers(UUID partnerId) {
    return delete(DatabaseCondition.of("partner", partnerId));
  }

  public CompletableFuture<List<Long>> findPartnerNewUsers(UUID partnerId) {
    return selectRowsColumns(DatabaseCondition.of("partner", partnerId),
      Lists.newArrayList(findColumnByName("date")))
      .thenApply(rows -> rows.stream().map(row -> row.findCell(0).longValue())
        .toList());
  }
}
