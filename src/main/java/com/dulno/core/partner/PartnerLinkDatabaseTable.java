package com.dulno.core.partner;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class PartnerLinkDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "partner_link";

  public static PartnerLinkDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("partner", DatabaseDataType.UUID,
      DatabaseColumn.Type.PARTITION_KEY));
    columns.add(DatabaseColumn.create("type", DatabaseDataType.TEXT,
      DatabaseColumn.Type.CLUSTERING_KEY));
    columns.add(DatabaseColumn.create("link", DatabaseDataType.TEXT));
    var table = new PartnerLinkDatabaseTable(connection, keyspace, TABLE_NAME,
      columns);
    table.createIfNotExists();
    return table;
  }

  private PartnerLinkDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  public CompletableFuture<Void> insertPartnerLink(PartnerLink link) {
    return insertPartnerLink(link.partnerId(), link.type().toString(), link.link());
  }

  public CompletableFuture<Void> insertPartnerLink(
    UUID partnerId, String type, String link
  ) {
    return insert(DatabaseRow.of(partnerId, type, link));
  }

  public CompletableFuture<Boolean> partnerLinkExists(UUID partnerId, String type) {
    return exists(DatabaseCondition.of("partner", partnerId, "type", type));
  }

  public CompletableFuture<Void> deletePartnerLink(UUID partnerId, String type) {
    return delete(DatabaseCondition.of("partner", partnerId, "type", type));
  }

  public CompletableFuture<PartnerLink> findPartnerLink(UUID partnerId, String type) {
    return selectRow(DatabaseCondition.of("partner", partnerId, "type", type))
      .thenApply(PartnerLink::of);
  }

  public CompletableFuture<List<PartnerLink>> findPartnerLinks(UUID partnerId) {
    return selectRows(DatabaseCondition.of("partner", partnerId))
      .thenApply(rows -> rows.stream().map(PartnerLink::of).toList());
  }
}
