package com.dulno.core.coupon;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class CouponDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "coupon";

  public static CouponDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("id", DatabaseDataType.UUID,
      DatabaseColumn.Type.PRIMARY_KEY));
    columns.add(DatabaseColumn.create("partner", DatabaseDataType.UUID));
    columns.add(DatabaseColumn.create("background_color", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("foreground_color", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("reward", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("description", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("expiration", DatabaseDataType.BIGINT));
    columns.add(DatabaseColumn.create("limitation", DatabaseDataType.INT));
    columns.add(DatabaseColumn.create("creation", DatabaseDataType.BIGINT));
    var table = new CouponDatabaseTable(connection, keyspace, TABLE_NAME, columns);
    table.createIfNotExists();
    table.initializeViews();
    return table;
  }

  private DatabaseTable partnerView;

  private CouponDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  private void initializeViews() {
    partnerView = createMaterializedViewIfNotExists("partner_view", "partner",
      DatabaseColumn.Type.PARTITION_KEY);
  }

  public CompletableFuture<Void> insertCoupon(Coupon coupon) {
    return insertCoupon(coupon.id(), coupon.partnerId(), coupon.backgroundColor(),
      coupon.foregroundColor(), coupon.reward(), coupon.description(),
      coupon.expiration(), coupon.limitation(), coupon.creation());
  }

  public CompletableFuture<Void> insertCoupon(
    UUID id, UUID partner, String backgroundColor, String foregroundColor,
    String reward, String description, long expiration, int limitation,
    long creation
  ) {
    return insert(DatabaseRow.of(id, partner, backgroundColor, foregroundColor,
      reward, description, expiration, limitation, creation));
  }

  public CompletableFuture<Void> updateCoupon(Coupon coupon) {
    return update(coupon.id(), DatabaseRow.of(coupon.id(), coupon.partnerId(),
      coupon.backgroundColor(), coupon.foregroundColor(), coupon.reward(),
      coupon.description(), coupon.expiration(), coupon.limitation(),
      coupon.creation()));
  }

  public CompletableFuture<UUID> generateAvailableCouponId() {
    var futureResponse = new CompletableFuture<UUID>();
    var id = UUID.randomUUID();
    couponExists(id).thenApply(exists -> exists ?
      generateAvailableCouponId().thenApply(futureResponse::complete) :
      CompletableFuture.completedFuture(futureResponse.complete(id)));
    return futureResponse;
  }

  public CompletableFuture<Boolean> couponExists(UUID couponId) {
    return exists(couponId);
  }

  public CompletableFuture<Void> deleteCoupon(UUID couponId) {
    return delete(couponId);
  }

  public CompletableFuture<Coupon> findCoupon(UUID couponId) {
    return selectRow(couponId).thenApply(row -> Coupon.of(row, this));
  }

  public CompletableFuture<List<Coupon>> findCouponsOfPartner(UUID partnerId) {
    return partnerView.selectRows(DatabaseCondition.of("partner", partnerId))
      .thenApply(rows -> rows.stream()
        .map(row -> Coupon.of(row, partnerView)).toList());
  }
}