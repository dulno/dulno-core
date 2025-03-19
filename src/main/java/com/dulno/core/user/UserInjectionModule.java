package com.dulno.core.user;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.dulno.core.database.DatabaseConnection;
import com.dulno.core.database.DatabaseKeyspace;
import com.dulno.core.user.mfa.MultiFactorAuthDatabaseTable;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class UserInjectionModule extends AbstractModule {
  @Provides
  @Singleton
  UserDatabaseTable provideUserDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var userDatabaseTable = UserDatabaseTable.create(connection, keyspace);
    userDatabaseTable.createIfNotExists();
    userDatabaseTable.createIndexIfNotExists("email");
    return userDatabaseTable;
  }

  @Provides
  @Singleton
  UserVerificationDatabaseTable provideUserVerificationDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var userVerificationDatabaseTable = UserVerificationDatabaseTable.create(
      connection, keyspace);
    userVerificationDatabaseTable.createIfNotExists();
    return userVerificationDatabaseTable;
  }

  @Provides
  @Singleton
  UserPasswordResetDatabaseTable provideUserPasswordResetDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var userPasswordResetDatabaseTable = UserPasswordResetDatabaseTable.create(
      connection, keyspace);
    userPasswordResetDatabaseTable.createIfNotExists();
    return userPasswordResetDatabaseTable;
  }

  @Provides
  @Singleton
  UserEmailChangeDatabaseTable provideUserEmailChangeDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var userEmailChangeDatabaseTable = UserEmailChangeDatabaseTable.create(
      connection, keyspace);
    userEmailChangeDatabaseTable.createIfNotExists();
    return userEmailChangeDatabaseTable;
  }

  @Provides
  @Singleton
  MultiFactorAuthDatabaseTable provideMultiFactorAuthDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var multiFactorAuthDatabaseTable = MultiFactorAuthDatabaseTable.create(
      connection, keyspace);
    multiFactorAuthDatabaseTable.createIfNotExists();
    return multiFactorAuthDatabaseTable;
  }
}
