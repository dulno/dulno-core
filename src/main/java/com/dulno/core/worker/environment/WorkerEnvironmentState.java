package com.dulno.core.worker.environment;

public enum WorkerEnvironmentState {
  HEAD,
  WORKER;

  public boolean isHead() {
    return this == HEAD;
  }

  public boolean isWorker() {
    return this == WORKER;
  }
}
