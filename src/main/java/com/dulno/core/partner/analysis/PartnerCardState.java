package com.dulno.core.partner.analysis;

public enum PartnerCardState {
  UNUSED,
  USED,
  USED_UP;

  public boolean isUnused() {
    return this == UNUSED;
  }

  public boolean isUsed() {
    return this == USED;
  }

  public boolean isUsedUp() {
    return this == USED_UP;
  }
}
