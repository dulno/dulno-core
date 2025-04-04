package com.dulno.core.user;

import com.dulno.core.database.DatabaseConnection;
import com.dulno.core.database.DatabaseKeyspace;
import com.dulno.core.scan.PartnerScanDatabaseTable;
import com.dulno.core.scan.ScanDatabaseTable;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class ScanInjectionModule extends AbstractModule {
  @Provides
  @Singleton
  ScanDatabaseTable provideScanDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return ScanDatabaseTable.create(connection, keyspace);
  }

  @Provides
  @Singleton
  PartnerScanDatabaseTable providePartnerScanDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return PartnerScanDatabaseTable.create(connection, keyspace);
  }
}