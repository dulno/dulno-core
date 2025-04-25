package com.dulno.core.accessory;

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
public final class Accessory {
  public static Accessory of(DatabaseRow row, DatabaseTable table) {
    return of(row, table.columns().stream().map(DatabaseColumn::name).toList());
  }

  public static Accessory of(DatabaseRow row, List<String> columns) {
    return create(row.findCell(columns.indexOf("id")).uuidValue(),
      row.findCell(columns.indexOf("partner")).uuidValue(),
      row.findCell(columns.indexOf("accessory")).stringValue(),
      row.findCell(columns.indexOf("price")).doubleValue(),
      row.findCell(columns.indexOf("purchase_date")).longValue());
  }

  private final UUID id;
  private final UUID partnerId;
  private final String accessoryId;
  private final double price;
  private final long purchaseDate;
}
