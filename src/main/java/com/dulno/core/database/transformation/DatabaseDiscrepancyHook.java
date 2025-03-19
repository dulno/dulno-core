package com.dulno.core.database.transformation;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.dulno.core.application.ApplicationPostRunEvent;
import com.dulno.core.application.ApplicationPreRunEvent;
import com.dulno.core.database.DatabaseKeyspace;
import com.dulno.core.database.DatabaseTable;
import com.dulno.core.event.EventHook;
import com.dulno.core.event.Hook;
import com.dulno.core.log.Log;
import com.dulno.core.worker.client.WorkerOperatorClient;
import com.dulno.core.worker.packet.outgoing.database.PacketOutgoingTableDiscrepancy;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__({@Inject}))
public final class DatabaseDiscrepancyHook implements Hook {
  private final Log log;
  private final DatabaseKeyspace keyspace;
  private final WorkerOperatorClient workerOperatorClient;

  @EventHook
  private void preApplicationRun(ApplicationPreRunEvent event) {
    for (var table : keyspace.tables()) {
      table.checkTableDiscrepancy();
    }
  }

  @EventHook
  private void applicationRun(ApplicationPostRunEvent event) {
    new Thread(this::sendDiscrepancyNotices).start();
  }

  private void sendDiscrepancyNotices() {
    try {
      Thread.sleep(10000);
    } catch (Exception ignored) {
    }
    for (var table : keyspace.tables()) {
      senDiscrepancyNotice(table);
    }
  }

  private void senDiscrepancyNotice(DatabaseTable table) {
    if (table.temporaryTable() == null) {
      return;
    }
    workerOperatorClient.sendPacket(new PacketOutgoingTableDiscrepancy(
      table.getClass().getCanonicalName()));
    log.info("A discrepancy was found in table " + table.getClass().getCanonicalName() +
      ". The transformation is being prepared.");
  }
}
