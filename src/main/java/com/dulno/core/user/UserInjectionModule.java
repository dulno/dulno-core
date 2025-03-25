package com.dulno.core.user;

import com.dulno.core.database.DatabaseConnection;
import com.dulno.core.database.DatabaseKeyspace;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class UserInjectionModule extends AbstractModule {
  @Provides
  @Singleton
  UserDatabaseTable provideUserDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return UserDatabaseTable.create(connection, keyspace);
  }

  @Provides
  @Singleton
  UserStampDatabaseTable provideUserStampDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return UserStampDatabaseTable.create(connection, keyspace);
  }
}