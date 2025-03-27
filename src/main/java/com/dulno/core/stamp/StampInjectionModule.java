package com.dulno.core.stamp;

import com.dulno.core.database.DatabaseConnection;
import com.dulno.core.database.DatabaseKeyspace;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class StampInjectionModule extends AbstractModule {
  @Provides
  @Singleton
  StampDatabaseTable provideStampDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return StampDatabaseTable.create(connection, keyspace);
  }

  @Provides
  @Singleton
  StampSetupDatabaseTable provideStampSetupDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return StampSetupDatabaseTable.create(connection, keyspace);
  }
}
