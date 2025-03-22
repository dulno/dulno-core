package com.dulno.core.member;

import com.dulno.core.database.DatabaseConnection;
import com.dulno.core.database.DatabaseKeyspace;
import com.dulno.core.member.mfa.MultiFactorAuthDatabaseTable;
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
    var memberDatabaseTable = MemberDatabaseTable.create(connection, keyspace);
    memberDatabaseTable.createIfNotExists();
    memberDatabaseTable.createIndexIfNotExists("email");
    return memberDatabaseTable;
  }

  @Provides
  @Singleton
  MemberVerificationDatabaseTable provideMemberVerificationDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var memberVerificationDatabaseTable = MemberVerificationDatabaseTable.create(
      connection, keyspace);
    memberVerificationDatabaseTable.createIfNotExists();
    return memberVerificationDatabaseTable;
  }

  @Provides
  @Singleton
  MemberPasswordResetDatabaseTable provideMemberPasswordResetDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var memberPasswordResetDatabaseTable = MemberPasswordResetDatabaseTable.create(
      connection, keyspace);
    memberPasswordResetDatabaseTable.createIfNotExists();
    return memberPasswordResetDatabaseTable;
  }

  @Provides
  @Singleton
  MemberEmailChangeDatabaseTable provideMemberEmailChangeDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var memberEmailChangeDatabaseTable = MemberEmailChangeDatabaseTable.create(
      connection, keyspace);
    memberEmailChangeDatabaseTable.createIfNotExists();
    return memberEmailChangeDatabaseTable;
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
