package com.dulno.core.packet;

import com.dulno.core.event.Event;
import com.dulno.core.worker.packet.incoming.PacketIncoming;

public interface PacketEvent<T> {
  /**
   * Creates a new event when packet is received
   * @param client The client that send the packet
   * @param packet The packet that has been received
   * @return The created event
   */
  Event process(T client, PacketIncoming packet);
}
