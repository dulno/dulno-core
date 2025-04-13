package com.dulno.core.campaign;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class CampaignDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "campaign";

  public static CampaignDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("id", DatabaseDataType.UUID,
      DatabaseColumn.Type.PRIMARY_KEY));
    columns.add(DatabaseColumn.create("partner", DatabaseDataType.UUID));
    columns.add(DatabaseColumn.create("title", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("description", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("type", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("start", DatabaseDataType.BIGINT));
    columns.add(DatabaseColumn.create("end", DatabaseDataType.BIGINT));
    var table = new CampaignDatabaseTable(connection, keyspace, TABLE_NAME, columns);
    table.createIfNotExists();
    table.initializeViews();
    return table;
  }

  private DatabaseTable partnerView;

  private CampaignDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  private void initializeViews() {
    partnerView = createMaterializedViewIfNotExists("partner_view", "partner",
      DatabaseColumn.Type.PARTITION_KEY);
  }

  public CompletableFuture<Void> insertCampaign(Campaign campaign) {
    return insertCampaign(campaign.id(), campaign.partnerId(),
      campaign.title(), campaign.description(), campaign.type().toString(),
      campaign.start(), campaign.end());
  }

  public CompletableFuture<Void> insertCampaign(
    UUID id, UUID partnerId, String title, String description, String type,
    long start, long end
  ) {
    return insert(DatabaseRow.of(id, partnerId, description, type, start, end));
  }

  public CompletableFuture<Void> updateCampaign(Campaign campaign) {
    return update(campaign.id(), DatabaseRow.of(campaign.id(),
      campaign.partnerId(), campaign.title(), campaign.description(),
      campaign.type().toString(), campaign.start(), campaign.end()));
  }

  public CompletableFuture<UUID> generateAvailableCampaignId() {
    var futureResponse = new CompletableFuture<UUID>();
    var id = UUID.randomUUID();
    campaignExists(id).thenApply(exists -> exists ?
      generateAvailableCampaignId().thenApply(futureResponse::complete) :
      CompletableFuture.completedFuture(futureResponse.complete(id)));
    return futureResponse;
  }

  public CompletableFuture<Boolean> campaignExists(UUID campaignId) {
    return exists(campaignId);
  }

  public CompletableFuture<Void> deleteCampaign(UUID campaignId) {
    return delete(campaignId);
  }

  public CompletableFuture<Campaign> findCampaign(UUID campaignId) {
    return selectRow(campaignId).thenApply(row -> Campaign.of(row, this));
  }

  public CompletableFuture<List<Campaign>> findCampaignsOfPartner(UUID partnerId) {
    return partnerView.selectRows(DatabaseCondition.of("partner", partnerId))
      .thenApply(rows -> rows.stream().map(row -> Campaign.of(row, partnerView))
        .toList());
  }
}
