package com.dulno.core.campaign.notification;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class CampaignNotificationDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "campaign_notification";

  public static CampaignNotificationDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("id", DatabaseDataType.UUID,
      DatabaseColumn.Type.PRIMARY_KEY));
    columns.add(DatabaseColumn.create("campaign", DatabaseDataType.UUID));
    columns.add(DatabaseColumn.create("partner", DatabaseDataType.UUID));
    columns.add(DatabaseColumn.create("title", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("body", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("dispatch", DatabaseDataType.BIGINT));
    columns.add(DatabaseColumn.create("state", DatabaseDataType.TEXT));
    var table = new CampaignNotificationDatabaseTable(connection, keyspace,
      TABLE_NAME, columns);
    table.createIfNotExists();
    table.initializeViews();
    return table;
  }

  private DatabaseTable campaignView;
  private DatabaseTable partnerView;
  private DatabaseTable stateView;

  private CampaignNotificationDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  private void initializeViews() {
    campaignView = createMaterializedViewIfNotExists("campaign_view", "campaign",
      DatabaseColumn.Type.PARTITION_KEY);
    partnerView = createMaterializedViewIfNotExists("partner_view", "partner",
      DatabaseColumn.Type.PARTITION_KEY);
    stateView = createMaterializedViewIfNotExists("state_view", "state",
      DatabaseColumn.Type.PARTITION_KEY);
  }

  public CompletableFuture<Void> insertCampaignNotification(
    CampaignNotification campaignNotification
  ) {
    return insertCampaignNotification(campaignNotification.id(),
      campaignNotification.campaignId(), campaignNotification.partnerId(),
      campaignNotification.title(), campaignNotification.body(),
      campaignNotification.dispatch(), campaignNotification.state().toString());
  }

  public CompletableFuture<Void> insertCampaignNotification(
    UUID id, UUID campaignId, UUID partnerId, String title, String body,
    long dispatch, String state
  ) {
    return insert(DatabaseRow.of(id, campaignId, partnerId, title, body,
      dispatch, state));
  }

  public CompletableFuture<Void> updateCampaignNotification(
    CampaignNotification campaignNotification
  ) {
    return update(campaignNotification.id(), DatabaseRow.of(
      campaignNotification.id(), campaignNotification.campaignId(),
      campaignNotification.partnerId(), campaignNotification.title(),
      campaignNotification.body(), campaignNotification.dispatch(),
      campaignNotification.state().toString()));
  }

  public CompletableFuture<UUID> generateAvailableCampaignNotificationId() {
    var futureResponse = new CompletableFuture<UUID>();
    var id = UUID.randomUUID();
    campaignNotificationExists(id).thenApply(exists -> exists ?
      generateAvailableCampaignNotificationId().thenApply(futureResponse::complete) :
      CompletableFuture.completedFuture(futureResponse.complete(id)));
    return futureResponse;
  }

  public CompletableFuture<Boolean> campaignNotificationExists(
    UUID campaignNotificationId
  ) {
    return exists(campaignNotificationId);
  }

  public CompletableFuture<Void> deleteCampaignNotification(
    UUID campaignNotificationId
  ) {
    return delete(campaignNotificationId);
  }

  public CompletableFuture<CampaignNotification> findCampaignNotification(
    UUID campaignNotificationId
  ) {
    return selectRow(campaignNotificationId)
      .thenApply(row -> CampaignNotification.of(row, this));
  }

  public CompletableFuture<List<CampaignNotification>> findNotificationsOfCampaign(
    UUID campaignId
  ) {
    return campaignView.selectRows(DatabaseCondition.of("campaign", campaignId))
      .thenApply(rows -> rows.stream()
        .map(row -> CampaignNotification.of(row, campaignView))
        .toList());
  }

  public CompletableFuture<List<CampaignNotification>> findNotificationsOfPartner(
    UUID partnerId
  ) {
    return partnerView.selectRows(DatabaseCondition.of("partner", partnerId))
      .thenApply(rows -> rows.stream()
        .map(row -> CampaignNotification.of(row, partnerView))
        .toList());
  }

  public CompletableFuture<List<CampaignNotification>> findNotificationsByState(
    String state
  ) {
    return stateView.selectRows(DatabaseCondition.of("state", state))
      .thenApply(rows -> rows.stream()
        .map(row -> CampaignNotification.of(row, stateView))
        .toList());
  }
}
