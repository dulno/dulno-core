package com.dulno.core.partner.analysis;

import com.dulno.core.database.DatabaseConnection;
import com.dulno.core.database.DatabaseKeyspace;
import com.dulno.core.partner.analysis.card.PartnerActiveCardDatabaseTable;
import com.dulno.core.partner.analysis.card.PartnerCardUsageDatabaseTable;
import com.dulno.core.partner.analysis.card.PartnerNewCardDatabaseTable;
import com.dulno.core.partner.analysis.stamp.PartnerOpenStampDatabaseTable;
import com.dulno.core.partner.analysis.stamp.PartnerScanDatabaseTable;
import com.dulno.core.partner.analysis.user.PartnerNewUserDatabaseTable;
import com.dulno.core.partner.analysis.user.PartnerUserLanguageDatabaseTable;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class PartnerAnalysisInjectionModule extends AbstractModule {
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

  @Provides
  @Singleton
  PartnerActiveCardDatabaseTable providePartnerActiveCardDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return PartnerActiveCardDatabaseTable.create(connection, keyspace);
  }

  @Provides
  @Singleton
  PartnerOpenStampDatabaseTable providePartnerOpenStampDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return PartnerOpenStampDatabaseTable.create(connection, keyspace);
  }
}