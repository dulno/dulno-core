package com.dulno.core.item;

import com.dulno.core.database.DatabaseColumn;
import com.dulno.core.database.DatabaseRow;
import com.dulno.core.database.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor(staticName = "create")
public final class Item {
  public static Item of(DatabaseRow row, DatabaseTable table) {
    return of(row, table.columns().stream().map(DatabaseColumn::name).toList());
  }

  public static Item of(DatabaseRow row, List<String> columns) {
    return create(row.findCell(columns.indexOf("id")).uuidValue(),
      row.findCell(columns.indexOf("partner")).uuidValue(),
      row.findCell(columns.indexOf("card")).uuidValue(),
      row.findCell(columns.indexOf("content")).stringValue(),
      row.findCell(columns.indexOf("last_update")).longValue());
  }

  private final UUID itemId;
  private final UUID partnerId;
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