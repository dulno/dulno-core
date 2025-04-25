package com.dulno.core.bundle;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum BundleType {
  BASIC(1),
  PROFESSIONAL(2),
  PREMIUM(3),
  ENTERPRISE(4);

  private final int value;

  public boolean isBasic() {
    return this == BASIC;
  }

  public boolean isProfessional() {
    return this == PROFESSIONAL;
  }

  public boolean isPremium() {
    return this == PREMIUM;
  }

  public boolean isEnterprise() {
    return this == ENTERPRISE;
  }
}
