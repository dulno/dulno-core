package com.dulno.core.user;

import com.dulno.core.database.DatabaseConnection;
import com.dulno.core.database.DatabaseKeyspace;
import com.dulno.core.user.device.UserDeviceDatabaseTable;
import com.dulno.core.user.item.UserItemDatabaseTable;
import com.dulno.core.user.scan.UserScanDatabaseTable;
import com.dulno.core.user.verification.UserVerificationDatabaseTable;
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
  UserItemDatabaseTable provideUserItemDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return UserItemDatabaseTable.create(connection, keyspace);
  }

  @Provides
  @Singleton
  UserScanDatabaseTable provideUserScanDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return UserScanDatabaseTable.create(connection, keyspace);
  }

  @Provides
  @Singleton
  UserVerificationDatabaseTable provideUserVerificationDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return UserVerificationDatabaseTable.create(connection, keyspace);
  }
}