package com.dulno.core.item;

import com.dulno.core.database.DatabaseRow;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor(staticName = "create")
public final class Item {
  public static Item of(DatabaseRow row) {
    return create(row.findCell(0).uuidValue(), row.findCell(1).uuidValue(),
      row.findCell(2).stringValue(), row.findCell(3).longValue());
  }

  private final UUID itemId;
  private final UUID cardId;
  private String content;
  private long lastUpdate;

  public void changeContent(String newContent) {
    content = newContent;
  }

  public void changeLastUpdate(long newLastUpdate) {
    lastUpdate = newLastUpdate;
  }
}