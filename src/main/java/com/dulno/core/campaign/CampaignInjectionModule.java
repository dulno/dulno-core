package com.dulno.core.campaign;

import com.dulno.core.campaign.notification.CampaignNotificationDatabaseTable;
import com.dulno.core.database.DatabaseConnection;
import com.dulno.core.database.DatabaseKeyspace;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class CampaignInjectionModule extends AbstractModule {
  @Provides
  @Singleton
  CampaignDatabaseTable provideCampaignDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return CampaignDatabaseTable.create(connection, keyspace);
  }

  @Provides
  @Singleton
  CampaignNotificationDatabaseTable provideCampaignNotificationDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return CampaignNotificationDatabaseTable.create(connection, keyspace);
  }
}

