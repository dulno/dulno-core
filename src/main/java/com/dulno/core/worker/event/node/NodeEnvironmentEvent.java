package com.dulno.core.worker.event.node;

import com.dulno.core.event.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "create")
public final class NodeEnvironmentEvent extends Event {
  private final String yourAddress;
  private final List<String> neighborAddresses;
}
