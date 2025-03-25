package com.dulno.core.user;

import com.dulno.core.database.DatabaseRow;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor(staticName = "create")
public final class UserStamp {
  public static UserStamp of(DatabaseRow row) {
    return create(row.findCell(0).uuidValue(), row.findCell(1).uuidValue(),
      row.findCell(2).integerValue());
  }

  private final UUID userId;
  private final UUID stampId;
  private final int value;
}