package com.dulno.core.stamp;

public enum StampState {
  ACTIVE,
  DEACTIVATED;

  public boolean isActive() {
    return this == ACTIVE;
  }

  public boolean isDeactivated() {
    return this == DEACTIVATED;
  }
}
