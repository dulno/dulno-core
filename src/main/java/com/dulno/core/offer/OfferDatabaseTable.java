package com.dulno.core.offer;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class OfferDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "offer";

  public static OfferDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("id", DatabaseDataType.UUID,
      DatabaseColumn.Type.PRIMARY_KEY));
    columns.add(DatabaseColumn.create("partner", DatabaseDataType.UUID));
    columns.add(DatabaseColumn.create("price_id", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("offer_status", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("bundle_type", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("bundler_runtime", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("price", DatabaseDataType.DOUBLE));
    columns.add(DatabaseColumn.create("content", DatabaseDataType.TEXT));
    return new OfferDatabaseTable(connection, keyspace, TABLE_NAME, columns);
  }

  private OfferDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  public CompletableFuture<Void> insertOffer(Offer offer) {
    return insert(DatabaseRow.of(offer.id(), offer.partnerId(), offer.priceId(),
      offer.offerStatus().toString(), offer.bundleType().toString(),
      offer.bundleRuntime().toString(), offer.price(), offer.serializeContent()));
  }

  public void updateOfferStatus(Offer offer, OfferStatus newStatus) {
    offer.updateStatus(newStatus);
    updateOffer(offer);
  }

  public CompletableFuture<Void> updateOffer(Offer offer) {
    return update(offer.id(), DatabaseRow.of(offer.id(),
      offer.partnerId(), offer.priceId(), offer.offerStatus().toString(),
      offer.bundleType().toString(), offer.bundleRuntime().toString(),
      offer.price(), offer.serializeContent()));
  }

  public CompletableFuture<UUID> generateAvailableOfferId() {
    var futureResponse = new CompletableFuture<UUID>();
    var id = UUID.randomUUID();
    offerExists(id).thenApply(exists -> exists ?
      generateAvailableOfferId().thenApply(futureResponse::complete) :
      CompletableFuture.completedFuture(futureResponse.complete(id)));
    return futureResponse;
  }

  public CompletableFuture<Void> deleteOffer(UUID offerId) {
    return delete(offerId);
  }

  public CompletableFuture<Boolean> offerExists(UUID offerId) {
    return exists(offerId);
  }

  public CompletableFuture<Offer> findOffer(UUID offerId) {
    return selectRow(offerId).thenApply(Offer::of);
  }

  public CompletableFuture<Offer> findOffersByPriceId(String priceId) {
    return selectRow(DatabaseCondition.of("price_id", priceId)).thenApply(Offer::of);
  }

  public CompletableFuture<List<Offer>> findOffersByPartner(UUID partnerId) {
    return selectRows(DatabaseCondition.of("partner", partnerId))
      .thenApply(rows -> rows.stream().map(Offer::of).toList());
  }
}