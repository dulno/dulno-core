package com.dulno.core.user;

import com.dulno.core.database.DatabaseConnection;
import com.dulno.core.database.DatabaseKeyspace;
import com.dulno.core.user.change.UserEmailChangeDatabaseTable;
import com.dulno.core.user.device.UserDeviceDatabaseTable;
import com.dulno.core.user.item.UserItemDatabaseTable;
import com.dulno.core.user.partner.UserPartnerDatabaseTable;
import com.dulno.core.user.redeemable.UserRedeemableDatabaseTable;
import com.dulno.core.user.scan.UserScanDatabaseTable;
import com.dulno.core.user.session.UserSessionDatabaseTable;
import com.dulno.core.user.statistic.UserAppOpenDatabaseTable;
import com.dulno.core.user.statistic.UserJoinDatabaseTable;
import com.dulno.core.user.statistic.UserStatisticConfiguration;
import com.dulno.core.user.transmission.UserTransmissionDatabaseTable;
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
  UserSessionDatabaseTable provideSessionDatabaseTable(
    DatabaseConnection databaseConnection, DatabaseKeyspace databaseKeyspace
  ) {
    return UserSessionDatabaseTable.create(databaseConnection, databaseKeyspace);
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
  UserRedeemableDatabaseTable provideUserRedeemableDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return UserRedeemableDatabaseTable.create(connection, keyspace);
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
  UserPartnerDatabaseTable provideUserPartnerDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return UserPartnerDatabaseTable.create(connection, keyspace);
  }

  @Provides
  @Singleton
  UserVerificationDatabaseTable provideUserVerificationDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return UserVerificationDatabaseTable.create(connection, keyspace);
  }

  @Provides
  @Singleton
  UserEmailChangeDatabaseTable provideUserEmailChangeDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return UserEmailChangeDatabaseTable.create(connection, keyspace);
  }

  @Provides
  @Singleton
  UserJoinDatabaseTable provideUserJoinDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return UserJoinDatabaseTable.create(connection, keyspace);
  }

  @Provides
  @Singleton
  UserAppOpenDatabaseTable provideUserAppOpenDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return UserAppOpenDatabaseTable.create(connection, keyspace);
  }

  @Provides
  @Singleton
  UserStatisticConfiguration provideUserStatisticConfiguration() throws Exception {
    return UserStatisticConfiguration.createAndLoad();
  }

  @Provides
  @Singleton
  UserTransmissionDatabaseTable provideUserTransmissionDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return UserTransmissionDatabaseTable.create(connection, keyspace);
  }
}