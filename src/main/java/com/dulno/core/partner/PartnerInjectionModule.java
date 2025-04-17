package com.dulno.core.partner;

import com.dulno.core.database.DatabaseConnection;
import com.dulno.core.database.DatabaseKeyspace;
import com.dulno.core.partner.analysis.*;
import com.dulno.core.partner.link.PartnerLinkDatabaseTable;
import com.dulno.core.partner.location.PartnerLocationDatabaseTable;
import com.dulno.core.partner.logo.PartnerLogoDatabaseTable;
import com.dulno.core.partner.member.PartnerMemberDatabaseTable;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class PartnerInjectionModule extends AbstractModule {
  @Provides
  @Singleton
  PartnerConfiguration providePartnerConfiguration() throws Exception {
    return PartnerConfiguration.createAndLoad();
  }

  @Provides
  @Singleton
  PartnerDatabaseTable providePartnerDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return PartnerDatabaseTable.create(connection, keyspace);
  }

  @Provides
  @Singleton
  PartnerMemberDatabaseTable providePartnerMemberDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return PartnerMemberDatabaseTable.create(connection, keyspace);
  }

  @Provides
  @Singleton
  PartnerLogoDatabaseTable providePartnerLogoDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return PartnerLogoDatabaseTable.create(connection, keyspace);
  }

  @Provides
  @Singleton
  PartnerLocationDatabaseTable providePartnerLocationDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return PartnerLocationDatabaseTable.create(connection, keyspace);
  }

  @Provides
  @Singleton
  PartnerLinkDatabaseTable providePartnerLinkDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return PartnerLinkDatabaseTable.create(connection, keyspace);
  }

  @Provides
  @Singleton
  PartnerScanDatabaseTable providePartnerScanDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return PartnerScanDatabaseTable.create(connection, keyspace);
  }

  @Provides
  @Singleton
  PartnerNewUserDatabaseTable providePartnerNewUserDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return PartnerNewUserDatabaseTable.create(connection, keyspace);
  }

  @Provides
  @Singleton
  PartnerNewCardDatabaseTable providePartnerNewCardDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return PartnerNewCardDatabaseTable.create(connection, keyspace);
  }

  @Provides
  @Singleton
  PartnerCardUsageDatabaseTable providePartnerCardUsageDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return PartnerCardUsageDatabaseTable.create(connection, keyspace);
  }

  @Provides
  @Singleton
  PartnerUserLanguageDatabaseTable providePartnerUserLanguageDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return PartnerUserLanguageDatabaseTable.create(connection, keyspace);
  }
}
