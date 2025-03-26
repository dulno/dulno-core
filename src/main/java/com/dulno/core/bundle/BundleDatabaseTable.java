package com.dulno.core.bundle;

import com.dulno.core.database.*;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class BundleDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "bundle";

  public static BundleDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("partner", DatabaseDataType.UUID,
      DatabaseColumn.Type.PRIMARY_KEY));
    columns.add(DatabaseColumn.create("bundle_type", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("bundler_runtime", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("price", DatabaseDataType.DOUBLE));
    columns.add(DatabaseColumn.create("expiration", DatabaseDataType.BIGINT));
    columns.add(DatabaseColumn.create("content", DatabaseDataType.TEXT));
    return new BundleDatabaseTable(connection, keyspace, TABLE_NAME, columns);
  }

  private BundleDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  public CompletableFuture<Void> insertBundle(Bundle bundle) {
    return insert(DatabaseRow.of(bundle.partnerId(), bundle.bundleType().toString(),
      bundle.bundleRuntime().toString(), bundle.price(), bundle.expiration(),
      bundle.serializeContent()));
  }

  public CompletableFuture<Void> updateBundle(Bundle bundle) {
    return update(bundle.partnerId(), DatabaseRow.of(bundle.partnerId(),
      bundle.bundleType().toString(), bundle.bundleRuntime().toString(),
      bundle.price(), bundle.expiration(), bundle.serializeContent()));
  }

  public CompletableFuture<Void> deleteBundle(UUID partnerId) {
    return delete(partnerId);
  }

  public CompletableFuture<Boolean> bundleExists(UUID partnerId) {
    return exists(partnerId);
  }

  public CompletableFuture<Bundle> findBundle(UUID partnerId) {
    return selectRow(partnerId).thenApply(Bundle::of);
  }

  public CompletableFuture<List<Bundle>> findAllBundles() {
    return selectAllRows().thenApply(rows ->
      rows.stream().map(Bundle::of).toList());
  }
}

