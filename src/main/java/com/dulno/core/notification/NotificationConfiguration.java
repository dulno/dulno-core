package com.dulno.core.notification;

import com.dulno.core.configuration.Configuration;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.json.JSONObject;

@Getter
@Accessors(fluent = true)
public final class NotificationConfiguration extends Configuration {
  private static final String CONFIGURATION_PATH = "/configurations/notification/notification.json";

  public static NotificationConfiguration createAndLoad() throws Exception {
    var configuration = new NotificationConfiguration(CONFIGURATION_PATH);
    configuration.load();
    return configuration;
  }

  private String firebaseConfigurationName;
  private String firebaseProjectId;

  private NotificationConfiguration(String path) {
    super(path);
  }

  @Override
  protected void deserialize(JSONObject json) {
    firebaseConfigurationName = json.getString("firebaseConfigurationName");
    firebaseProjectId = json.getString("firebaseProjectId");
  }
}
