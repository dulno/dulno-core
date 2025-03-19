package com.dulno.core.worker.event.node;

import com.dulno.core.event.Event;
import com.dulno.core.worker.client.WorkerOperatorClient;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "create")
public final class NodePingEvent extends Event {
  private final WorkerOperatorClient client;
  private final int value;
}