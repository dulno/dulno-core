package com.dulno.core.card;

public enum CardState {
  UNUSED,
  USED;

  public boolean isUnused() {
    return this == UNUSED;
  }

  public boolean isUsed() {
    return this == USED;
  }
}
