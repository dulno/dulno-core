package com.dulno.core.redeemable;

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
public final class Redeemable {
  public static Redeemable of(DatabaseRow row, DatabaseTable table) {
    return of(row, table.columns().stream().map(DatabaseColumn::name).toList());
  }

  public static Redeemable of(DatabaseRow row, List<String> columns) {
    return create(row.findCell(columns.indexOf("id")).uuidValue(),
      row.findCell(columns.indexOf("partner")).uuidValue(),
      row.findCell(columns.indexOf("coupon")).uuidValue(),
      row.findCell(columns.indexOf("expiration")).longValue(),
      row.findCell(columns.indexOf("last_update")).longValue());
  }

  private final UUID redeemableId;
  private final UUID partnerId;
  private final UUID couponId;
  private long expiration;
  private long lastUpdate;

  public void changeExpiration(long newExpiration) {
    expiration = newExpiration;
  }

  public void changeLastUpdate(long newLastUpdate) {
    lastUpdate = newLastUpdate;
  }
}