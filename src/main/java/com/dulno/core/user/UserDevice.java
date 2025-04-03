package com.dulno.core.user;

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
public final class UserDevice {
  public static UserDevice of(DatabaseRow row, DatabaseTable table) {
    return of(row, table.columns().stream().map(DatabaseColumn::name).toList());
  }

  public static UserDevice of(DatabaseRow row, List<String> columns) {
    return create(row.findCell(columns.indexOf("id")).uuidValue(),
      row.findCell(columns.indexOf("user")).uuidValue(),
      row.findCell(columns.indexOf("device_id")).stringValue(),
      row.findCell(columns.indexOf("operating_system")).stringValue(),
      row.findCell(columns.indexOf("operating_system_version")).stringValue(),
      row.findCell(columns.indexOf("brand")).stringValue(),
      row.findCell(columns.indexOf("model")).stringValue(),
      row.findCell(columns.indexOf("name")).stringValue());
  }

  private final UUID id;
  private final UUID userId;
  private final String deviceId;
  private final String operatingSystem;
  private final String operatingSystemVersion;
  private final String brand;
  private final String model;
  private final String name;
}