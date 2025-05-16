package com.dulno.core.partner.analysis.card;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class PartnerCardGroupDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "partner_card_group";

  public static PartnerCardGroupDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("item", DatabaseDataType.UUID,
      DatabaseColumn.Type.PRIMARY_KEY));
    columns.add(DatabaseColumn.create("group", DatabaseDataType.UUID));
    columns.add(DatabaseColumn.create("partner", DatabaseDataType.UUID));
    var table = new PartnerCardGroupDatabaseTable(connection, keyspace,
      TABLE_NAME, columns);
    table.createIfNotExists();
    table.initializeViews();
    return table;
  }

  private DatabaseTable groupView;

  private PartnerCardGroupDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  private void initializeViews() {
    groupView = createMaterializedViewIfNotExists("group_view", "group",
      DatabaseColumn.Type.PARTITION_KEY);
  }

  public CompletableFuture<Void> insertPartnerCardGroup(
    UUID itemId, UUID groupId, UUID partnerId
  ) {
    return insert(DatabaseRow.of(itemId, groupId, partnerId));
  }

  public CompletableFuture<Void> deletePartnerCardGroup(UUID itemId) {
    return delete(itemId);
  }

  public CompletableFuture<UUID> generateAvailablePartnerCardGroupId() {
    var futureResponse = new CompletableFuture<UUID>();
    var id = UUID.randomUUID();
    partnerCardGroupExistsByGroup(id).thenApply(exists -> exists ?
      generateAvailablePartnerCardGroupId().thenApply(futureResponse::complete) :
      CompletableFuture.completedFuture(futureResponse.complete(id)));
    return futureResponse;
  }

  public CompletableFuture<Boolean> partnerCardGroupExists(UUID itemId) {
    return exists(itemId);
  }

  public CompletableFuture<Boolean> partnerCardGroupExistsByGroup(UUID groupId) {
    return groupView.exists(DatabaseCondition.of("group", groupId));
  }

  public CompletableFuture<UUID> findPartnerCardGroup(UUID itemId) {
    return selectRow(itemId).thenApply(row -> row.findCell(1).uuidValue());
  }

  public CompletableFuture<List<UUID>> findPartnerCardGroupItems(UUID groupId) {
    return groupView.selectRows(DatabaseCondition.of("group", groupId))
      .thenApply(rows -> rows.stream().map(row ->
        row.findCell(1).uuidValue()).toList());
  }
}