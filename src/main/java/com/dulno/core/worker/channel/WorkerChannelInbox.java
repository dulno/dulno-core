package com.dulno.core.worker.channel;

import com.dulno.core.event.EventExecutor;
import com.dulno.core.packet.PacketEventRepository;
import com.dulno.core.worker.client.WorkerOperatorClient;
import com.dulno.core.worker.event.node.NodeDisconnectEvent;
import com.dulno.core.worker.packet.incoming.PacketIncoming;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class WorkerChannelInbox extends SimpleChannelInboundHandler<PacketIncoming> {
  private final EventExecutor eventExecutor;
  private final WorkerOperatorClient workerOperatorClient;
  private final PacketEventRepository packetEventRepository;

  @Override
  protected void channelRead0(
    ChannelHandlerContext context, PacketIncoming incomingPacket
  ) {
    if (context.channel() != workerOperatorClient.channel()) {
      return;
    }
    var event = packetEventRepository.findEvent(incomingPacket.getClass());
    if (event.isEmpty()) {
      return;
    }
    eventExecutor.execute(event.get().process(workerOperatorClient, incomingPacket));
  }

  @Override
  public void channelInactive(ChannelHandlerContext context) {
    if (context.channel() != workerOperatorClient.channel()) {
      return;
    }
    eventExecutor.execute(NodeDisconnectEvent.create(
      NodeDisconnectEvent.DisconnectReason.TIME_OUT));
  }
}

