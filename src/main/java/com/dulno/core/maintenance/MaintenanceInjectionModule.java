package com.dulno.core.maintenance;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.dulno.core.database.DatabaseConnection;
import com.dulno.core.database.DatabaseKeyspace;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class MaintenanceInjectionModule extends AbstractModule {
  @Provides
  @Singleton
  MaintenanceDatabaseTable provideMaintenanceDatabaseTable(
    DatabaseConnection databaseConnection, DatabaseKeyspace databaseKeyspace
  ) {
    return MaintenanceDatabaseTable.create(databaseConnection, databaseKeyspace);
  }
}
