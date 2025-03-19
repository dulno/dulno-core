package com.dulno.core.worker;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.dulno.core.event.HookRegistry;
import com.dulno.core.packet.PacketEventRepository;
import com.dulno.core.packet.PacketRegistry;
import com.dulno.core.worker.client.WorkerOperatorClient;
import com.dulno.core.worker.event.database.TableStateRequestEvent;
import com.dulno.core.worker.event.database.TableStateResponseEvent;
import com.dulno.core.worker.event.database.TableTransformEvent;
import com.dulno.core.worker.event.node.NodeHandshakeResponseEvent;
import com.dulno.core.worker.event.node.NodePingEvent;
import com.dulno.core.worker.packet.incoming.database.PacketIncomingTableStateRequest;
import com.dulno.core.worker.packet.incoming.database.PacketIncomingTableStateResponse;
import com.dulno.core.worker.packet.incoming.database.PacketIncomingTableTransform;
import com.dulno.core.worker.packet.incoming.node.PacketIncomingHandshakeResponse;
import com.dulno.core.worker.packet.incoming.node.PacketIncomingPing;
import com.dulno.core.worker.packet.outgoing.node.PacketOutgoingDisconnect;
import com.dulno.core.worker.packet.outgoing.node.PacketOutgoingHandshakeRequest;
import com.dulno.core.worker.server.database.TableStateRequestHook;
import com.dulno.core.worker.server.database.TableStateResponseHook;
import com.dulno.core.worker.server.database.TableTransformHook;
import com.dulno.core.worker.server.node.NodeDisconnectHook;
import com.dulno.core.worker.server.node.NodeHandshakeResponseHook;
import com.dulno.core.worker.server.node.NodePingHook;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__({@Inject}))
public final class WorkerDistribution {
  private final Injector injector;
  private final WorkerConfiguration configuration;
  private final PacketRegistry packetRegistry;
  private final HookRegistry hookRegistry;
  private final PacketEventRepository packetEventRepository;
  private final WorkerOperatorClient workerOperatorClient;

  /**
   * Initializes node distribution (registers packets, hooks, events and
   * opens server)
   * @throws Exception
   */
  public void initialize() throws Exception {
    registerPackets();
    registerHooks();
    registerEvents();
    workerOperatorClient.connectAsync(() -> workerOperatorClient.sendPacket(
      new PacketOutgoingHandshakeRequest(configuration.distributionKey())));
  }

  private void registerPackets() throws Exception {
    packetRegistry.registerPacket(PacketIncomingHandshakeResponse.class);
    packetRegistry.registerPacket(PacketIncomingPing.class);
    packetRegistry.registerPacket(PacketIncomingTableTransform.class);
    packetRegistry.registerPacket(PacketIncomingTableStateRequest.class);
    packetRegistry.registerPacket(PacketIncomingTableStateResponse.class);
  }

  private void registerHooks() {
    hookRegistry.register(injector.getInstance(NodeDisconnectHook.class));
    hookRegistry.register(injector.getInstance(NodeHandshakeResponseHook.class));
    hookRegistry.register(injector.getInstance(NodePingHook.class));
    hookRegistry.register(injector.getInstance(TableTransformHook.class));
    hookRegistry.register(injector.getInstance(TableStateRequestHook.class));
    hookRegistry.register(injector.getInstance(TableStateResponseHook.class));
  }

  private void registerEvents() {
    packetEventRepository.registerEvent(PacketIncomingHandshakeResponse.class,
      (client, packet) -> NodeHandshakeResponseEvent.create(packet.success()));
    packetEventRepository.<WorkerOperatorClient, PacketIncomingPing>registerEvent(
      PacketIncomingPing.class, (client, packet) ->
        NodePingEvent.create(client, packet.value()));
    packetEventRepository.registerEvent(PacketIncomingTableTransform.class,
      (client, packet) -> TableTransformEvent.create(packet.tableClass()));
    packetEventRepository.registerEvent(PacketIncomingTableStateRequest.class,
      (client, packet) -> TableStateRequestEvent.create(packet.tableClass(),
        packet.state()));
    packetEventRepository.registerEvent(PacketIncomingTableStateResponse.class,
      (client, packet) -> TableStateResponseEvent.create(packet.tableClass(),
        packet.state()));
  }

  /**
   * Sends farewell greeting
   */
  public void disconnect() {
    workerOperatorClient.sendPacket(new PacketOutgoingDisconnect());
  }
}
