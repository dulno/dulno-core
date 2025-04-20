package com.dulno.core.partner;

import com.dulno.core.database.DatabaseConnection;
import com.dulno.core.database.DatabaseKeyspace;
import com.dulno.core.partner.analysis.*;
import com.dulno.core.partner.link.PartnerLinkDatabaseTable;
import com.dulno.core.partner.location.PartnerLocationDatabaseTable;
import com.dulno.core.partner.logo.PartnerLogoDatabaseTable;
import com.dulno.core.partner.member.PartnerMemberDatabaseTable;
import com.dulno.core.partner.transaction.PartnerTransactionDatabaseTable;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class PartnerInjectionModule extends AbstractModule {
  @Override
  protected void configure() {
    install(PartnerAnalysisInjectionModule.create());
  }

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
  PartnerTransactionDatabaseTable providePartnerTransactionDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return PartnerTransactionDatabaseTable.create(connection, keyspace);
  }
}
