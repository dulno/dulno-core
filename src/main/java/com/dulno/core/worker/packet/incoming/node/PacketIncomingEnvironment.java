package com.dulno.core.worker.packet.incoming.node;

import com.dulno.core.packet.PacketBuffer;
import com.dulno.core.worker.packet.incoming.PacketIncoming;
import com.google.api.client.util.Lists;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Accessors(fluent = true)
public final class PacketIncomingEnvironment extends PacketIncoming {
  private String yourAddress;
  private List<String> neighborAddresses;

  public PacketIncomingEnvironment() {
    super(0x05);
  }

  @Override
  public void read(PacketBuffer buffer) throws Exception {
    yourAddress = buffer.readString();
    var neighbors = buffer.readVarInt();
    neighborAddresses = Lists.newArrayList();
    for (var i = 0; i < neighbors; i++) {
      neighborAddresses.add(buffer.readString());
    }
  }
}