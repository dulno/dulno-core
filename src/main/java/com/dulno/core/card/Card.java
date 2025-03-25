package com.dulno.core.card;


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
public final class Card {
  public static Card of(DatabaseRow row, DatabaseTable table) {
    return of(row, table.columns().stream().map(DatabaseColumn::name).toList());
  }

  public static Card of(DatabaseRow row, List<String> columns) {
    return create(row.findCell(columns.indexOf("id")).uuidValue(),
      row.findCell(columns.indexOf("partner")).uuidValue(),
      row.findCell(columns.indexOf("color")).stringValue(),
      CardType.valueOf(row.findCell(columns.indexOf("type")).stringValue()),
      row.findCell(columns.indexOf("content")).stringValue(),
      row.findCell(columns.indexOf("creation")).longValue());
  }

  private final UUID id;
  private final UUID partner;
  private String color;
  private CardType type;
  private String content;
  private final long creation;

  public void changeColor(String newColor) {
    color = newColor;
  }

  public void changeType(CardType newType) {
    type = newType;
  }

  public void changeContent(String newContent) {
    content = newContent;
  }
}
