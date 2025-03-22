package com.dulno.core;

import com.dulno.core.application.ApplicationLaunchEvent;
import com.dulno.core.application.ApplicationPostRunEvent;
import com.dulno.core.application.ApplicationPreRunEvent;
import com.dulno.core.database.DatabaseConnection;
import com.dulno.core.database.transformation.DatabaseDiscrepancyHook;
import com.dulno.core.error.ErrorRepository;
import com.dulno.core.event.EventExecutor;
import com.dulno.core.event.HookRegistry;
import com.dulno.core.intro.Intro;
import com.dulno.core.log.Log;
import com.dulno.core.maintenance.MaintenanceSchedule;
import com.dulno.core.module.ModuleLoader;
import com.dulno.core.worker.WorkerConfiguration;
import com.dulno.core.worker.WorkerDistribution;
import com.dulno.core.worker.client.WorkerOperatorClient;
import com.dulno.core.worker.packet.outgoing.node.PacketOutgoingDisconnect;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication(scanBasePackages = {"com.dulno"},
  exclude = {org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration.class})
public class CoreApplication {
  /**
   * The starting point where the application is executed
   * @param args The arguments that are passed into the application
   */
  public static void main(String[] args) {
    var injector = Guice.createInjector(CoreInjectionModule.create());
    var errorRepository = injector.getInstance(ErrorRepository.class);
    Thread.setDefaultUncaughtExceptionHandler((thread, throwable) ->
      errorRepository.processError(throwable));
    injector.getInstance(DatabaseConnection.class).errorRepository(errorRepository);
    try {
      injector.getInstance(Intro.class).print();
      var log = injector.getInstance(Log.class);
      log.info("Initializing Dulno - Core");
      registerHooks(injector.getInstance(HookRegistry.class), injector);
      var eventExecutor = injector.getInstance(EventExecutor.class);
      eventExecutor.execute(ApplicationLaunchEvent.create());
      var distributionConfiguration = injector.getInstance(WorkerConfiguration.class);
      var distribution = injector.getInstance(WorkerDistribution.class);
      distribution.initialize();
      injector.getInstance(ModuleLoader.class).loadModules();
      var application = injector.getInstance(SpringApplication.class);
      application.setDefaultProperties(Collections.singletonMap("server.port",
        distributionConfiguration.restPort()));
      eventExecutor.execute(ApplicationPreRunEvent.create());
      log.info("Booting Spring...");
      application.run(args);
      injector.getInstance(MaintenanceSchedule.class).start();
      log.info("Successfully booted Dulno - Core");
      Runtime.getRuntime().addShutdownHook(new Thread(() ->
        injector.getInstance(WorkerOperatorClient.class)
          .sendPacket(new PacketOutgoingDisconnect())));
      eventExecutor.execute(ApplicationPostRunEvent.create());
    } catch (Exception exception) {
      errorRepository.processError(exception);
    }
  }

  private static void registerHooks(HookRegistry registry, Injector injector) {
    registry.register(injector.getInstance(DatabaseDiscrepancyHook.class));
  }
}