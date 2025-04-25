package com.dulno.core.partner;

import com.dulno.core.configuration.Configuration;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.json.JSONObject;

@Getter
@Accessors(fluent = true)
public final class PartnerConfiguration extends Configuration {
  private static final String CONFIGURATION_PATH = "/configurations/partner/partner.json";

  public static PartnerConfiguration createAndLoad() throws Exception {
    var configuration = new PartnerConfiguration(CONFIGURATION_PATH);
    configuration.load();
    return configuration;
  }

  private String defaultLogo;

  private PartnerConfiguration(String path) {
    super(path);
  }

  @Override
  protected void deserialize(JSONObject json) {
    defaultLogo = json.getString("defaultLogo");
  }
}