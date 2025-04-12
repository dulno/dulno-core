package com.dulno.core.notification;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@RequiredArgsConstructor(staticName = "create")
public final class Notification {
  private final NotificationConfiguration notificationConfiguration;
  private final GoogleCredentials googleCredentials;
  private final String receiver;
  private final String title;
  private final String body;

  private static final String FIREBASE_URL =
    "https://fcm.googleapis.com/v1/projects/%s/messages:send";

  public void send() throws Exception {
    googleCredentials.refreshIfExpired();
    var token = googleCredentials.getAccessToken().getTokenValue();
    var url = String.format(FIREBASE_URL,
      notificationConfiguration.firebaseProjectId());
    var requestBody = new JSONObject(createPayload());
    var requestBuilder = HttpRequest.newBuilder().uri(URI.create(url))
      .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
      .setHeader("Content-Type", "application/json")
      .setHeader("Authorization", "Bearer " + token)
      .build();
    HttpClient.newHttpClient().sendAsync(requestBuilder,
      HttpResponse.BodyHandlers.ofByteArray());
  }

  private Map<String, Object> createPayload() {
    var payload = Maps.<String, Object>newHashMap();
    payload.put("to", receiver);
    var notification = Maps.<String, Object>newHashMap();
    notification.put("title", title);
    notification.put("body", body);
    notification.put("sound", "default");
    payload.put("notification", notification);
    return payload;
  }
}
