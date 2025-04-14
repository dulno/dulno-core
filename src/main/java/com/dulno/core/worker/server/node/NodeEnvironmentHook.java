package com.dulno.core.worker.server.node;

import com.dulno.core.event.EventHook;
import com.dulno.core.event.Hook;
import com.dulno.core.log.Log;
import com.dulno.core.worker.environment.WorkerEnvironment;
import com.dulno.core.worker.event.node.NodeEnvironmentEvent;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__({@Inject}))
public final class NodeEnvironmentHook implements Hook {
  private final Log log;
  private final WorkerEnvironment environment;

  @EventHook
  private void nodeEnvironment(NodeEnvironmentEvent event) {
    var previousState = environment.state();
    environment.update(environment.self(), environment.neighbors());
    if (previousState.isWorker() && environment.state().isHead()) {
      log.info("This worker is now the operator head");
    } else if (previousState.isHead() && environment.state().isWorker()) {
      log.info("This worker is no longer the operator head");
    }
  }
}
