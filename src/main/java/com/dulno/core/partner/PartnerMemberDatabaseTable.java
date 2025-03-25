package com.dulno.core.partner;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class PartnerMemberDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "partner_member";

  public static PartnerMemberDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("partner", DatabaseDataType.UUID,
      DatabaseColumn.Type.PARTITION_KEY));
    columns.add(DatabaseColumn.create("member", DatabaseDataType.UUID,
      DatabaseColumn.Type.CLUSTERING_KEY));
    var table = new PartnerMemberDatabaseTable(connection, keyspace, TABLE_NAME,
      columns);
    table.createIfNotExists();
    table.initializeViews();
    return table;
  }

  private DatabaseTable memberView;

  private PartnerMemberDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  private void initializeViews() {
    memberView = createMaterializedViewIfNotExists("member_view", "member",
      DatabaseColumn.Type.PARTITION_KEY);
  }

  public CompletableFuture<Void> insertPartnerMember(UUID partnerId, UUID memberId) {
    return insert(DatabaseRow.of(partnerId, memberId));
  }

  public CompletableFuture<Boolean> isPartnerMember(UUID partnerId, UUID memberId) {
    return exists(DatabaseCondition.of("partner", partnerId, "member", memberId));
  }

  public void deletePartnerMember(UUID partnerId, UUID memberId) {
    delete(DatabaseCondition.of("partner", partnerId, "member", memberId));
  }

  public CompletableFuture<List<UUID>> findPartnerMembers(UUID partnerId) {
    return selectRows(DatabaseCondition.of("partner", partnerId))
      .thenApply(rows -> rows.stream().map(row ->
        row.findCell(1).uuidValue()).toList());
  }

  public CompletableFuture<List<UUID>> findMemberPartners(UUID memberId) {
    return memberView.selectRows(DatabaseCondition.of("member", memberId))
      .thenApply(rows -> rows.stream().map(row ->
        row.findCell(1).uuidValue()).toList());
  }

  public CompletableFuture<UUID> findMemberPartner(UUID memberId) {
    return memberView.selectRow(DatabaseCondition.of("member", memberId))
      .thenApply(row -> row.findCell(1).uuidValue());
  }
}

