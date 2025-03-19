package com.dulno.core.ntag.cmac;

public enum CMACType {
  AES,
  LRP;

  public boolean isAes() {
    return this == AES;
  }

  public boolean isLrp() {
    return this == LRP;
  }
}
