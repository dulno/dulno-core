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
      row.findCell(columns.indexOf("background_color")).stringValue(),
      row.findCell(columns.indexOf("foreground_color")).stringValue(),
      CardType.valueOf(row.findCell(columns.indexOf("type")).stringValue()),
      row.findCell(columns.indexOf("content")).stringValue(),
      row.findCell(columns.indexOf("creation")).longValue(),
      CardState.valueOf(row.findCell(columns.indexOf("state")).stringValue()));
  }

  private final UUID id;
  private final UUID partnerId;
  private String backgroundColor;
  private String foregroundColor;
  private final CardType type;
  private String content;
  private final long creation;
  private CardState state;

  public void changeBackgroundColor(String newBackgroundColor) {
    backgroundColor = newBackgroundColor;
  }

  public void changeForegroundColor(String newForegroundColor) {
    foregroundColor = newForegroundColor;
  }

  public void changeContent(String newContent) {
    content = newContent;
  }

  public void changeState(CardState newState) {
    state = newState;
  }
}
