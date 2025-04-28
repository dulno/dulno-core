package com.dulno.core.bundle;

import com.dulno.core.configuration.Configuration;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.json.JSONObject;

@Getter
@Accessors(fluent = true)
public final class BundlePreset extends Configuration {
  private static final String CONFIGURATION_PATH = "/configurations/bundle/%s.json";

  public static BundlePreset createAndLoad(BundleType bundleType) throws Exception {
    var configuration = new BundlePreset(String.format(CONFIGURATION_PATH,
      bundleType.toString().toLowerCase()), bundleType);
    configuration.load();
    return configuration;
  }

  private final BundleType bundleType;
  private boolean hasPrice;
  private double monthlyPrice;
  private double yearlyPrice;
  private boolean portalAccess;
  private boolean collectionCardAccess;
  private boolean valueCardAccess;
  private boolean memberCardAccess;
  private boolean analysisAccess;
  private boolean balancesAccess;
  private boolean campaignAccess;
  private boolean notificationAccess;

  private BundlePreset(String path, BundleType bundleType) {
    super(path);
    this.bundleType = bundleType;
  }

  @Override
  protected void deserialize(JSONObject json) {
    hasPrice = json.has("monthlyPrice") && json.has("yearlyPrice");
    if (hasPrice) {
      monthlyPrice = json.getDouble("monthlyPrice");
      yearlyPrice = json.getDouble("yearlyPrice");
    }
    portalAccess = json.getBoolean("portalAccess");
    collectionCardAccess = json.getBoolean("collectionCardAccess");
    valueCardAccess = json.getBoolean("valueCardAccess");
    memberCardAccess = json.getBoolean("memberCardAccess");
    analysisAccess = json.getBoolean("analysisAccess");
    balancesAccess = json.getBoolean("balancesAccess");
    campaignAccess = json.getBoolean("campaignAccess");
    notificationAccess = json.getBoolean("notificationAccess");
  }
}
