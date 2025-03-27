package com.dulno.core.accessory;

import com.dulno.core.configuration.Configuration;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.json.JSONObject;

@Getter
@Accessors(fluent = true)
public final class AccessoryPreset extends Configuration {
  private static final String CONFIGURATION_PATH = "/configurations/accessory/%s.json";

  public static AccessoryPreset createAndLoad(String name) throws Exception {
    var configuration = new AccessoryPreset(String.format(CONFIGURATION_PATH, name));
    configuration.load();
    return configuration;
  }

  private String id;
  private String priceId;
  private double price;
  private String name;
  private String description;

  private AccessoryPreset(String path) {
    super(path);
  }

  @Override
  protected void deserialize(JSONObject json) {
    id = json.getString("id");
    priceId = json.getString("priceId");
    price = json.getDouble("price");
    name = json.getString("name");
    description = json.getString("description");
  }
}
