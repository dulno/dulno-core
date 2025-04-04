package com.dulno.core.scan;

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
public final class Scan {
  public static Scan of(DatabaseRow row, DatabaseTable table) {
    return of(row, table.columns().stream().map(DatabaseColumn::name).toList());
  }

  public static Scan of(DatabaseRow row, List<String> columns) {
    return create(row.findCell(columns.indexOf("id")).uuidValue(),
      row.findCell(columns.indexOf("user")).uuidValue(),
      row.findCell(columns.indexOf("stamp")).uuidValue(),
      row.findCell(columns.indexOf("card")).uuidValue(),
      row.findCell(columns.indexOf("partner")).uuidValue(),
      row.findCell(columns.indexOf("picc")).stringValue(),
      row.findCell(columns.indexOf("cmac")).stringValue(),
      row.findCell(columns.indexOf("time")).longValue());
  }

  private final UUID id;
  private final UUID userId;
  private final UUID stampId;
  private final UUID cardId;
  private final UUID partnerId;
  private final String picc;
  private final String cmac;
  private final long time;
}
