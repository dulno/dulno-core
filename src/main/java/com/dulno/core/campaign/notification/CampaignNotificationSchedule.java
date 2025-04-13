package com.dulno.core.campaign.notification;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Singleton
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__({@Inject}))
public final class CampaignNotificationSchedule {
  private final CampaignNotificationDatabaseTable campaignNotificationDatabaseTable;
  private final ScheduledExecutorService executorService =
    Executors.newScheduledThreadPool(1);
  private ScheduledFuture<?> scheduler;

  private static final long CHECK_INTERVAL = 1000L * 60;
  private static final TimeUnit CHECK_TIME_UNIT = TimeUnit.MILLISECONDS;

  public void start() {
    scheduler = executorService.scheduleAtFixedRate(this::execute,
      calculateInitialDelay(), CHECK_INTERVAL, CHECK_TIME_UNIT);
    execute();
  }

  private long calculateInitialDelay() {
    var current = LocalDateTime.now();
    return Duration.between(current, current.plusMinutes(1)
      .truncatedTo(ChronoUnit.MINUTES)).toMillis();
  }

  private void execute() {
    //TODO: IMPLEMENT
  }

  public void stop() {
    scheduler.cancel(false);
  }
}
