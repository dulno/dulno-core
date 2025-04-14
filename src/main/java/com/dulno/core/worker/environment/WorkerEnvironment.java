package com.dulno.core.worker.environment;

import com.google.api.client.util.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Accessors(fluent = true)
@Singleton
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__({@Inject}))
public final class WorkerEnvironment {
  private String self = "";
  private List<String> neighbors = Lists.newArrayList();
  private WorkerEnvironmentState state = WorkerEnvironmentState.WORKER;

  public void update(String yourAddress, List<String> neighborAddresses) {
    self = yourAddress;
    neighbors = neighborAddresses;
    for (var neighbor : neighbors) {
      if (self.compareTo(neighbor) >= 0) {
        state = WorkerEnvironmentState.WORKER;
        return;
      }
    }
    state = WorkerEnvironmentState.HEAD;
  }
}
