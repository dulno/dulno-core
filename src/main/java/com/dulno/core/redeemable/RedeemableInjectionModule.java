package com.dulno.core.redeemable;

import com.dulno.core.database.DatabaseConnection;
import com.dulno.core.database.DatabaseKeyspace;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class RedeemableInjectionModule extends AbstractModule {
  @Provides
  @Singleton
  RedeemableDatabaseTable provideRedeemableDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return RedeemableDatabaseTable.create(connection, keyspace);
  }
}