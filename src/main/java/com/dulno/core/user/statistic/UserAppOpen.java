package com.dulno.core.user.statistic;

import com.dulno.core.database.DatabaseRow;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor(staticName = "create")
public final class UserAppOpen {
  public static UserAppOpen of(DatabaseRow row) {
    return create(row.findCell(0).uuidValue(), row.findCell(1).longValue(),
      row.findCell(2).stringValue());
  }

  private final UUID appOpenId;
  private final long date;
  private final String version;
}