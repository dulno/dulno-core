package com.dulno.core.partner;

public enum PartnerVisibility {
  VISIBLE,
  INVISIBLE;

  public boolean isVisible() {
    return this == VISIBLE;
  }

  public boolean isInvisible() {
    return this == INVISIBLE;
  }
}
