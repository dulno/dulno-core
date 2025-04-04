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
  UserDeviceDatabaseTable provideUserDeviceDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return UserDeviceDatabaseTable.create(connection, keyspace);
  }

  @Provides
  @Singleton
  UserCardDatabaseTable provideUserStampDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return UserCardDatabaseTable.create(connection, keyspace);
  }

  @Provides
  @Singleton
  UserFirebaseDatabaseTable provideUserFirebaseDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return UserFirebaseDatabaseTable.create(connection, keyspace);
  }

  @Provides
  @Singleton
  UserVerificationDatabaseTable provideUserVerificationDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return UserVerificationDatabaseTable.create(connection, keyspace);
  }
}