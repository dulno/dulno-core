package com.dulno.core.accessory;

import com.dulno.core.database.DatabaseConnection;
import com.dulno.core.database.DatabaseKeyspace;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class AccessoryInjectionModule extends AbstractModule {
  @Provides
  @Singleton
  AccessoryDatabaseTable provideBundleDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return AccessoryDatabaseTable.create(connection, keyspace);
  }

  @Provides
  @Singleton
  AccessoryPresetRepository provideAccessoryPresetRepository() throws Exception {
    var bundlePresetRepository = AccessoryPresetRepository.create();
    bundlePresetRepository.registerPreset(AccessoryPreset.createAndLoad(
      "starter-kit"));
    bundlePresetRepository.registerPreset(AccessoryPreset.createAndLoad("stamp"));
    bundlePresetRepository.registerPreset(AccessoryPreset.createAndLoad("tag"));
    bundlePresetRepository.registerPreset(AccessoryPreset.createAndLoad("display"));
    return bundlePresetRepository;
  }
}