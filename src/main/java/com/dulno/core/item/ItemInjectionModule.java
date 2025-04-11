package com.dulno.core.item;

import com.dulno.core.database.DatabaseConnection;
import com.dulno.core.database.DatabaseKeyspace;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class ItemInjectionModule extends AbstractModule {
  @Provides
  @Singleton
  ItemDatabaseTable provideItemDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return ItemDatabaseTable.create(connection, keyspace);
  }
}