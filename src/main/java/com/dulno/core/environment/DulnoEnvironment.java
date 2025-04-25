package com.dulno.core.environment;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class DulnoEnvironment {
  public static DulnoEnvironment create() {
    var environment = new DulnoEnvironment();
    environment.initialize();
    return environment;
  }

  private enum Type {
    PRODUCTION,
    STAGING,
    LOCAL
  }

  private Type type;

  public void initialize() {
    try {
      type = Type.valueOf(System.getenv("DULNO_ENVIRONMENT"));
    } catch (Exception exception) {
      type = Type.LOCAL;
    }
  }

  public boolean isProduction() {
    return type == Type.PRODUCTION;
  }

  public boolean isStaging() {
    return type == Type.STAGING;
  }

  public boolean isLocal() {
    return type == Type.LOCAL;
  }

  public String domain() {
    if (isProduction()) {
      return "dulno.com";
    }
    return "dulno.dev";
  }

  public String publicEndpoint() {
    if (isProduction()) {
      return "api.dulno.com";
    }
    return "pub.dulno.dev";
  }
}
