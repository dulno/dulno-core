package com.dulno.core.card;

import com.dulno.core.database.DatabaseConnection;
import com.dulno.core.database.DatabaseKeyspace;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class CardInjectionModule extends AbstractModule {
  @Provides
  @Singleton
  CardDatabaseTable provideCardDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return CardDatabaseTable.create(connection, keyspace);
  }

  @Provides
  @Singleton
  CardLogoDatabaseTable provideCardLogoDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return CardLogoDatabaseTable.create(connection, keyspace);
  }
}

