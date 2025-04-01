package com.dulno.core.partner;

import com.dulno.core.database.DatabaseRow;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor(staticName = "create")
public final class PartnerLink {
  public static PartnerLink of(DatabaseRow row) {
    return create(row.findCell(0).uuidValue(),
      PartnerLinkType.valueOf(row.findCell(1).stringValue()),
      row.findCell(2).stringValue(), row.findCell(3).longValue());
  }

  private final UUID partnerId;
  private final PartnerLinkType type;
  private final String link;
  private final long creation;
}
