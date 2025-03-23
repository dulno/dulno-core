package com.dulno.core.worker;

import com.dulno.core.event.EventExecutor;
import com.dulno.core.packet.PacketEventRepository;
import com.dulno.core.packet.PacketRegistry;
import com.dulno.core.worker.client.WorkerOperatorClient;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class WorkerInjectionModule extends AbstractModule {
  @Provides
  @Singleton
  WorkerConfiguration provideDistributionConfiguration() throws Exception {
    return WorkerConfiguration.createAndLoad();
  }

  @Provides
  @Singleton
  WorkerOperatorClient provideWorkerOperatorClient(
    WorkerConfiguration configuration, PacketRegistry packetRegistry,
    EventExecutor eventExecutor, PacketEventRepository packetEventRepository
  ) {
    return WorkerOperatorClient.create(configuration, packetRegistry,
      eventExecutor, packetEventRepository, configuration.operatorHostname(),
      configuration.operatorDistributionPort());
  }
}
