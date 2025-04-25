package com.dulno.core.campaign.notification;

import com.dulno.core.application.ApplicationPreRunEvent;
import com.dulno.core.event.EventHook;
import com.dulno.core.event.Hook;
import com.dulno.core.log.Log;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__({@Inject}))
public final class CampaignNotificationHook implements Hook {
  private final Log log;
  private final CampaignNotificationSchedule campaignNotificationSchedule;

  @EventHook
  private void preApplicationRun(ApplicationPreRunEvent event) {
    campaignNotificationSchedule.start();
    log.info("Successfully started campaign notification schedule");
  }
}
