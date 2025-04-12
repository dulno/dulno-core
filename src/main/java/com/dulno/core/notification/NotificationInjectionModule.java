package com.dulno.core.notification;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;

import java.io.FileInputStream;

@RequiredArgsConstructor(staticName = "create")
public final class NotificationInjectionModule extends AbstractModule {
  @Provides
  @Singleton
  NotificationConfiguration provideNotificationConfiguration() throws Exception {
    return NotificationConfiguration.createAndLoad();
  }

  @Provides
  @Singleton
  GoogleCredentials provideGoogleCredentials(
    NotificationConfiguration configuration
  ) throws Exception {
    return ServiceAccountCredentials
      .fromStream(new FileInputStream(System.getProperty("user.dir") +
        "/configurations/notification/" + configuration.firebaseConfigurationName()))
      .createScoped("https://www.googleapis.com/auth/firebase.messaging");
  }
}
