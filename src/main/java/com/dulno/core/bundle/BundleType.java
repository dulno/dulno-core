package com.dulno.core.bundle;

public enum BundleType {
  BASIC,
  PROFESSIONAL,
  PREMIUM,
  ENTERPRISE;

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
