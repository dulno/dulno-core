package com.dulno.core.card.icon;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum CardMemberIcon {
  CROWN("crown", "card.member.icon.crown"),
  STAR("star", "card.member.icon.star"),
  HEART("heart", "card.member.icon.heart"),
  USER("circle-user", "card.member.icon.user");

  private final String identifier;
  private final String locale;
}
