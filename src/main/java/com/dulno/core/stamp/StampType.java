package com.dulno.core.stamp;

public enum StampType {
  STAMP,
  TAG;

  public boolean isStamp() {
    return this == STAMP;
  }

  public boolean isTag() {
    return this == TAG;
  }
}
