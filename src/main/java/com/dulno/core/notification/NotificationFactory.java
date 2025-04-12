package com.dulno.core.notification;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__({@Inject}))
public final class NotificationFactory {
  private final NotificationConfiguration notificationConfiguration;
  private final GoogleCredentials googleCredentials;

  public Notification create(String receiver, String title, String body) {
    return Notification.create(notificationConfiguration, googleCredentials,
      receiver, title, body);
  }
}
