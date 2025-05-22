package com.dulno.core.coupon;

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
public final class Coupon {
  public static Coupon of(DatabaseRow row, DatabaseTable table) {
    return of(row, table.columns().stream().map(DatabaseColumn::name).toList());
  }

  public static Coupon of(DatabaseRow row, List<String> columns) {
    return create(row.findCell(columns.indexOf("id")).uuidValue(),
      row.findCell(columns.indexOf("partner")).uuidValue(),
      row.findCell(columns.indexOf("background_color")).stringValue(),
      row.findCell(columns.indexOf("foreground_color")).stringValue(),
      row.findCell(columns.indexOf("reward")).stringValue(),
      row.findCell(columns.indexOf("description")).stringValue(),
      row.findCell(columns.indexOf("expiration")).longValue(),
      row.findCell(columns.indexOf("creation")).longValue());
  }

  private final UUID id;
  private final UUID partnerId;
  private String backgroundColor;
  private String foregroundColor;
  private String reward;
  private String description;
  private long expiration;
  private final long creation;

  public void changeBackgroundColor(String newBackgroundColor) {
    backgroundColor = newBackgroundColor;
  }

  public void changeForegroundColor(String newForegroundColor) {
    foregroundColor = newForegroundColor;
  }

  public void changeReward(String newReward) {
    reward = newReward;
  }

  public void changeDescription(String newDescription) {
    description = newDescription;
  }

  public void changeExpiration(long newExpiration) {
    expiration = newExpiration;
  }
}
