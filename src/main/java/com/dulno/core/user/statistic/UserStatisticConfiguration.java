package com.dulno.core.user.statistic;

import com.dulno.core.configuration.Configuration;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.json.JSONObject;

@Getter
@Accessors(fluent = true)
public final class UserStatisticConfiguration extends Configuration {
  private static final String CONFIGURATION_PATH = "/configurations/statistic/statistic.json";

  public static UserStatisticConfiguration createAndLoad() throws Exception {
    var configuration = new UserStatisticConfiguration(CONFIGURATION_PATH);
    configuration.load();
    return configuration;
  }

  private String statisticKey;

  private UserStatisticConfiguration(String path) {
    super(path);
  }

  @Override
  protected void deserialize(JSONObject json) {
    statisticKey = json.getString("statisticKey");
  }
}

