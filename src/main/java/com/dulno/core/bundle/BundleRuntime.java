package com.dulno.core.bundle;

public enum BundleRuntime {
  MONTHLY,
  YEARLY,
  UNBOUND;

  public boolean isMonthly() {
    return this == MONTHLY;
  }

  public boolean isYearly() {
    return this == YEARLY;
  }

  public boolean isUnbound() {
    return this == UNBOUND;
  }
}
