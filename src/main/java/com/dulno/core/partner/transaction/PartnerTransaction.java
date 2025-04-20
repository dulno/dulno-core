package com.dulno.core.partner.transaction;

import com.dulno.core.database.DatabaseRow;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor(staticName = "create")
public final class PartnerTransaction {
  public static PartnerTransaction of(DatabaseRow row) {
    return create(row.findCell(0).uuidValue(), row.findCell(1).uuidValue(),
      row.findCell(2).uuidValue(), row.findCell(3).doubleValue(),
      row.findCell(4).longValue());
  }

  private final UUID partnerId;
  private final UUID transactionId;
  private final UUID cardId;
  private final double value;
  private final long time;
}
