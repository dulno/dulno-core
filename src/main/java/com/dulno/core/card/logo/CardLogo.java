package com.dulno.core.card.logo;

import com.dulno.core.database.DatabaseRow;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor(staticName = "create")
public final class CardLogo {
  public static CardLogo of(DatabaseRow row) {
    return create(row.findCell(0).uuidValue(), row.findCell(1).uuidValue(),
      row.findCell(2).blobValue().array());
  }

  private final UUID cardId;
  private final UUID logoId;
  private final byte[] content;
}