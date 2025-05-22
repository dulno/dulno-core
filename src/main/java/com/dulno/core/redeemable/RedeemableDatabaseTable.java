package com.dulno.core.redeemable;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class RedeemableDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "redeemable";

  public static RedeemableDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("id", DatabaseDataType.UUID,
      DatabaseColumn.Type.PRIMARY_KEY));
    columns.add(DatabaseColumn.create("partner", DatabaseDataType.UUID));
    columns.add(DatabaseColumn.create("coupon", DatabaseDataType.UUID));
    columns.add(DatabaseColumn.create("expiration", DatabaseDataType.BIGINT));
    columns.add(DatabaseColumn.create("last_update", DatabaseDataType.BIGINT));
    var table = new RedeemableDatabaseTable(connection, keyspace, TABLE_NAME, columns);
    table.createIfNotExists();
    table.initializeViews();
    return table;
  }

  private DatabaseTable partnerView;

  private RedeemableDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  private void initializeViews() {
    partnerView = createMaterializedViewIfNotExists("partner_view", "partner",
      DatabaseColumn.Type.PARTITION_KEY);
  }

  public CompletableFuture<Void> insertRedeemable(Redeemable redeemable) {
    return insertRedeemable(redeemable.redeemableId(), redeemable.partnerId(),
      redeemable.couponId(), redeemable.expiration(), redeemable.lastUpdate());
  }

  public CompletableFuture<Void> insertRedeemable(
    UUID redeemableId, UUID partnerId, UUID couponId, long expiration,
    long lastUpdate
  ) {
    return insert(DatabaseRow.of(redeemableId, partnerId, couponId, expiration,
      lastUpdate));
  }

  public CompletableFuture<Void> updateRedeemable(Redeemable redeemable) {
    return updateRedeemable(redeemable.redeemableId(), redeemable.partnerId(),
      redeemable.couponId(), redeemable.expiration(), redeemable.lastUpdate());
  }

  public CompletableFuture<Void> updateRedeemable(
    UUID redeemableId, UUID partnerId, UUID couponId, long expiration,
    long lastUpdate
  ) {
    return update(redeemableId, DatabaseRow.of(redeemableId, partnerId,
      couponId, expiration, lastUpdate));
  }

  public CompletableFuture<UUID> generateAvailableRedeemableId() {
    var futureResponse = new CompletableFuture<UUID>();
    var id = UUID.randomUUID();
    redeemableExists(id).thenApply(exists -> exists ?
      generateAvailableRedeemableId().thenApply(futureResponse::complete) :
      CompletableFuture.completedFuture(futureResponse.complete(id)));
    return futureResponse;
  }

  public CompletableFuture<Void> deleteRedeemable(UUID redeemableId) {
    return delete(redeemableId);
  }

  public CompletableFuture<Boolean> redeemableExists(UUID redeemableId) {
    return exists(redeemableId);
  }

  public CompletableFuture<Redeemable> findRedeemable(UUID redeemableId) {
    return selectRow(redeemableId).thenApply(row -> Redeemable.of(row, this));
  }

  public CompletableFuture<List<Redeemable>> findAllRedeemableOfPartner(
    UUID partnerId
  ) {
    return partnerView.selectRows(DatabaseCondition.of("partner", partnerId))
      .thenApply(rows -> rows.stream()
        .map(row -> Redeemable.of(row, partnerView)).toList());
  }
}

