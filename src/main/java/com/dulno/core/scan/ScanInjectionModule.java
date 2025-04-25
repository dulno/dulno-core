package com.dulno.core.scan;

import com.dulno.core.database.DatabaseConnection;
import com.dulno.core.database.DatabaseKeyspace;
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
}