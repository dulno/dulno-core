package com.dulno.core.partner;

import com.dulno.core.database.DatabaseConnection;
import com.dulno.core.database.DatabaseKeyspace;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class PartnerInjectionModule extends AbstractModule {
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
}
