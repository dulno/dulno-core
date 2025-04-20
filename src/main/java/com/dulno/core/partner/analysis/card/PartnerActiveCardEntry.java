package com.dulno.core.partner.analysis.card;

import com.dulno.core.database.DatabaseRow;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor(staticName = "create")
public final class PartnerActiveCardEntry {
  public static PartnerActiveCardEntry of(DatabaseRow row) {
    return create(row.findCell(0).uuidValue(), row.findCell(1).uuidValue(),
      row.findCell(2).integerValue(), row.findCell(3).longValue());
  }

  private final UUID partnerId;
  private final UUID openStampId;
  private final int change;
  private final long date;
}
