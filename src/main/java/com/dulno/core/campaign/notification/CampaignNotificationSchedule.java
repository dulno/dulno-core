package com.dulno.core.campaign.notification;

import com.dulno.core.campaign.Campaign;
import com.dulno.core.campaign.CampaignDatabaseTable;
import com.dulno.core.error.ErrorRepository;
import com.dulno.core.notification.NotificationFactory;
import com.dulno.core.worker.environment.WorkerEnvironment;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Singleton
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__({@Inject}))
public final class CampaignNotificationSchedule {
  private final CampaignNotificationDatabaseTable campaignNotificationDatabaseTable;
  private final CampaignDatabaseTable campaignDatabaseTable;
  private final WorkerEnvironment workerEnvironment;
  private final NotificationFactory notificationFactory;
  private final ErrorRepository errorRepository;
  private final ScheduledExecutorService executorService =
    Executors.newScheduledThreadPool(1);
  private ScheduledFuture<?> scheduler;

  private static final long CHECK_INTERVAL = 1000L * 60;
  private static final TimeUnit CHECK_TIME_UNIT = TimeUnit.MILLISECONDS;

  public void start() {
    scheduler = executorService.scheduleAtFixedRate(this::execute,
      calculateInitialDelay(), CHECK_INTERVAL, CHECK_TIME_UNIT);
  }

  private long calculateInitialDelay() {
    var current = LocalDateTime.now();
    return Duration.between(current, current.plusMinutes(1)
      .truncatedTo(ChronoUnit.MINUTES)).toMillis();
  }

  private void execute() {
    if (workerEnvironment.state().isWorker()) {
      return;
    }
    campaignNotificationDatabaseTable
      .findNotificationsByState(CampaignNotificationState.PENDING.toString())
      .thenAccept(this::processNotifications);
  }

  private void processNotifications(List<CampaignNotification> notifications) {
    for (var notification : notifications) {
      if (!shouldDispatchNotification(notification)) {
        continue;
      }
      campaignDatabaseTable.findCampaign(notification.campaignId())
        .thenAccept(campaign -> dispatchNotification(notification, campaign));
    }
  }

  private void dispatchNotification(
    CampaignNotification notification, Campaign campaign
  ) {
    notification.changeState(CampaignNotificationState.SENT);
    campaignNotificationDatabaseTable.updateCampaignNotification(notification);
    try {
      notificationFactory.create(campaign.partnerId().toString(),
        notification.title(), notification.body()).send();
    } catch (Exception exception) {
      errorRepository.processError(exception);
    }
  }

  private boolean shouldDispatchNotification(CampaignNotification notification) {
    return notification.dispatch() > System.currentTimeMillis() - 1000 * 30 &&
      notification.dispatch() < System.currentTimeMillis() + 1000 * 30;
  }

  public void stop() {
    scheduler.cancel(false);
  }
}
