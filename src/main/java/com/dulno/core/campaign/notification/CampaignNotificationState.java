package com.dulno.core.campaign.notification;

public enum CampaignNotificationState {
  PENDING,
  SENT;

  public boolean isPending() {
    return this == PENDING;
  }

  public boolean isSent() {
    return this == SENT;
  }
}
