package com.dulno.core.apple;

import com.dulno.core.database.DatabaseConnection;
import com.dulno.core.database.DatabaseKeyspace;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class AppleInjectionModule extends AbstractModule {
  @Provides
  @Singleton
  AppleDatabaseTable provideAppleDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return AppleDatabaseTable.create(connection, keyspace);
  }
}

