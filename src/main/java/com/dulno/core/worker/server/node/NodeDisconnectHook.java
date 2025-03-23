package com.dulno.core.worker.server.node;

import com.dulno.core.event.EventHook;
import com.dulno.core.event.Hook;
import com.dulno.core.log.Log;
import com.dulno.core.worker.WorkerConfiguration;
import com.dulno.core.worker.client.WorkerOperatorClient;
import com.dulno.core.worker.event.node.NodeDisconnectEvent;
import com.dulno.core.worker.packet.outgoing.node.PacketOutgoingHandshakeRequest;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__({@Inject}))
public final class NodeDisconnectHook implements Hook {
  private final Log log;
  private final WorkerConfiguration configuration;
  private final WorkerOperatorClient workerOperatorClient;

  @EventHook
  private void nodeDisconnect(NodeDisconnectEvent event) {
    var reason = event.reason();
    if (reason.isConnectionFailed()) {
      log.severe("The connection to the operator failed");
    } else if (reason.isTimeOut()) {
      log.severe("The connection to the operator timed out");
    }
    workerOperatorClient.connectAsync(() -> workerOperatorClient.sendPacket(
      new PacketOutgoingHandshakeRequest(configuration.distributionKey())));
  }
}
