package com.dulno.core.card;


import com.dulno.core.database.DatabaseRow;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor(staticName = "create")
public final class Card {
  public static Card of(DatabaseRow row) {
    return create(row.findCell(0).uuidValue(), row.findCell(1).uuidValue(),
      row.findCell(2).stringValue(), row.findCell(3).stringValue(),
      row.findCell(4).stringValue(), CardType.valueOf(row.findCell(5).stringValue()),
      row.findCell(6).stringValue(), row.findCell(7).longValue());
  }

  private final UUID id;
  private final UUID partner;
  private String name;
  private String description;
  private String color;
  private CardType type;
  private String content;
  private final long creation;

  public void changeName(String newName) {
    name = newName;
  }

  public void changeDescription(String newDescription) {
    description = newDescription;
  }

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
