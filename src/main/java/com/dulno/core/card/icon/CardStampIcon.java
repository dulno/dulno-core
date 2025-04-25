package com.dulno.core.card.icon;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum CardStampIcon {
  CIRCLE("circle", "card.stamp.icon.circle"),
  SQUARE("square", "card.stamp.icon.square"),
  STAR("star", "card.stamp.icon.star"),
  HEART("heart", "card.stamp.icon.heart"),
  SMILEY("face-smile", "card.stamp.icon.smiley"),
  BELL("bell", "card.stamp.icon.bell"),
  THUMBS_UP("thumbs-up", "card.stamp.icon.thumbs.up"),
  BOOKMARK("bookmark", "card.stamp.icon.bookmark"),
  DIAMOND("gem", "card.stamp.icon.diamond");

  private final String identifier;
  private final String locale;
}
