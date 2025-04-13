package com.dulno.core.campaign;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum CampaignType {
  DISCOUNT("percent", "campaign.type.discount"),
  EVENT("calendar-days", "campaign.type.event"),
  LAUNCH("rocket", "campaign.type.launch"),
  SEASONAL("temperature-half", "campaign.type.seasonal"),
  RECRUITING("user-tie", "campaign.type.recruiting");

  private final String icon;
  private final String locale;
}
