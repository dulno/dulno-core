package com.dulno.core.partner;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class PartnerLocationDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "partner_location";

  public static PartnerLocationDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("partner", DatabaseDataType.UUID,
      DatabaseColumn.Type.PARTITION_KEY));
    columns.add(DatabaseColumn.create("address", DatabaseDataType.TEXT,
      DatabaseColumn.Type.CLUSTERING_KEY));
    columns.add(DatabaseColumn.create("latitude", DatabaseDataType.DOUBLE));
    columns.add(DatabaseColumn.create("longitude", DatabaseDataType.DOUBLE));
    var table = new PartnerLocationDatabaseTable(connection, keyspace, TABLE_NAME,
      columns);
    table.createIfNotExists();
    return table;
  }

  private PartnerLocationDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  public CompletableFuture<Void> insertPartnerLocation(PartnerLocation location) {
    return insertPartnerLocation(location.partnerId(), location.address(),
      location.latitude(), location.longitude());
  }

  public CompletableFuture<Void> insertPartnerLocation(
    UUID partnerId, String address, double latitude, double longitude
  ) {
    return insert(DatabaseRow.of(partnerId, address, latitude, longitude));
  }

  public CompletableFuture<Boolean> partnerLocationExists(
    UUID partnerId, String address
  ) {
    return exists(DatabaseCondition.of("partner", partnerId, "address", address));
  }

  public CompletableFuture<Void> deletePartnerLocation(
    UUID partnerId, String address
  ) {
    return delete(DatabaseCondition.of("partner", partnerId, "type", address));
  }

  public CompletableFuture<PartnerLocation> findPartnerLocation(
    UUID partnerId, String address
  ) {
    return selectRow(DatabaseCondition.of("partner", partnerId, "address", address))
      .thenApply(PartnerLocation::of);
  }

  public CompletableFuture<List<PartnerLocation>> findPartnerLocations(UUID partnerId) {
    return selectRows(DatabaseCondition.of("partner", partnerId))
      .thenApply(rows -> rows.stream().map(PartnerLocation::of).toList());
  }
}
