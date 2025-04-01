package com.dulno.core.partner;

import com.dulno.core.database.DatabaseRow;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor(staticName = "create")
public final class PartnerLocation {
  public static PartnerLocation of(DatabaseRow row) {
    return create(row.findCell(0).uuidValue(), row.findCell(1).stringValue(),
      row.findCell(2).doubleValue(), row.findCell(3).doubleValue(),
      row.findCell(4).longValue());
  }

  private final UUID partnerId;
  private final String address;
  private final double latitude;
  private final double longitude;
  private final long creation;
}
