package com.dulno.core.coupon.logo;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class CouponLogoDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "coupon_logo";

  public static CouponLogoDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("coupon", DatabaseDataType.UUID,
      DatabaseColumn.Type.PARTITION_KEY));
    columns.add(DatabaseColumn.create("id", DatabaseDataType.UUID,
      DatabaseColumn.Type.CLUSTERING_KEY));
    columns.add(DatabaseColumn.create("content", DatabaseDataType.BLOB));
    var table = new CouponLogoDatabaseTable(connection, keyspace, TABLE_NAME,
      columns);
    table.createIfNotExists();
    return table;
  }

  private CouponLogoDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  public CompletableFuture<Void> insertCouponLogo(
    UUID couponId, UUID logoId, byte[] content
  ) {
    return insert(DatabaseRow.of(couponId, logoId, ByteBuffer.wrap(content)));
  }

  public CompletableFuture<UUID> generateAvailableCouponLogoId(UUID couponId) {
    var futureResponse = new CompletableFuture<UUID>();
    var id = UUID.randomUUID();
    couponLogoExists(couponId, id).thenApply(exists -> exists ?
      generateAvailableCouponLogoId(couponId).thenApply(futureResponse::complete) :
      CompletableFuture.completedFuture(futureResponse.complete(id)));
    return futureResponse;
  }

  public CompletableFuture<Boolean> couponLogoExists(UUID couponId, UUID logoId) {
    return exists(DatabaseCondition.of("coupon", couponId, "id", logoId));
  }

  public CompletableFuture<Void> deleteCouponLogo(UUID couponId) {
    return delete(DatabaseCondition.of("coupon", couponId));
  }

  public CompletableFuture<Void> deleteCouponLogo(UUID couponId, UUID logoId) {
    return delete(DatabaseCondition.of("coupon", couponId, "id", logoId));
  }

  public CompletableFuture<UUID> findCouponLogoId(UUID couponId) {
    return selectRowColumns(DatabaseCondition.of("coupon", couponId),
      Lists.newArrayList(findColumnByName("id")))
      .thenApply(row -> row.findCell(0).uuidValue());
  }

  public CompletableFuture<CouponLogo> findCouponLogo(UUID couponId) {
    return selectRow(DatabaseCondition.of("coupon", couponId))
      .thenApply(CouponLogo::of);
  }
}