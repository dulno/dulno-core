package com.dulno.core.worker.event.node;

import com.dulno.core.event.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "create")
public final class NodeHandshakeResponseEvent extends Event {
  private final boolean success;
}
