package com.dulno.core.stamp;

import com.dulno.core.database.DatabaseRow;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor(staticName = "create")
public final class Stamp {
  public static Stamp of(DatabaseRow row) {
    return create(row.findCell(0).uuidValue(), row.findCell(1).stringValue(),
      row.findCell(2).stringValue(), row.findCell(3).stringValue(),
      row.findCell(4).uuidValue(), row.findCell(5).longValue());
  }

  private final UUID id;
  private final String uid;
  private final String masterKey;
  private String name;
  private UUID assignedCard;
  private final long creation;

  public void changeName(String newName) {
    name = newName;
  }

  public void changeAssignedCard(UUID newAssignedCard) {
    assignedCard = newAssignedCard;
  }
}
