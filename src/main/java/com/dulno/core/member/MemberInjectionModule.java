package com.dulno.core.member;

import com.dulno.core.database.DatabaseConnection;
import com.dulno.core.database.DatabaseKeyspace;
import com.dulno.core.member.mfa.MultiFactorAuthDatabaseTable;
import com.dulno.core.member.session.MemberSessionDatabaseTable;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class MemberInjectionModule extends AbstractModule {
  @Provides
  @Singleton
  MemberDatabaseTable providememberDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return MemberDatabaseTable.create(connection, keyspace);
  }

  @Provides
  @Singleton
  MemberSessionDatabaseTable provideSessionDatabaseTable(
    DatabaseConnection databaseConnection, DatabaseKeyspace databaseKeyspace
  ) {
    return MemberSessionDatabaseTable.create(databaseConnection, databaseKeyspace);
  }

  @Provides
  @Singleton
  MemberVerificationDatabaseTable provideMemberVerificationDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return MemberVerificationDatabaseTable.create(connection, keyspace);
  }

  @Provides
  @Singleton
  MemberPasswordResetDatabaseTable provideMemberPasswordResetDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return MemberPasswordResetDatabaseTable.create(connection, keyspace);
  }

  @Provides
  @Singleton
  MemberEmailChangeDatabaseTable provideMemberEmailChangeDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return MemberEmailChangeDatabaseTable.create(connection, keyspace);
  }

  @Provides
  @Singleton
  MultiFactorAuthDatabaseTable provideMultiFactorAuthDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return MultiFactorAuthDatabaseTable.create(connection, keyspace);
  }
}
