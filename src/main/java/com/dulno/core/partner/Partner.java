package com.dulno.core.partner;

import com.dulno.core.database.DatabaseRow;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor(staticName = "create")
public final class Partner {
  public static Partner of(DatabaseRow row) {
    return create(row.findCell(0).uuidValue(), row.findCell(1).stringValue(),
      row.findCell(2).stringValue(), row.findCell(3).longValue());
  }

  private final UUID id;
  private String name;
  private String description;
  private final long accession;

  public void changeName(String newName) {
    name = newName;
  }

  public void changeDescription(String newDescription) {
    description = newDescription;
  }
}
