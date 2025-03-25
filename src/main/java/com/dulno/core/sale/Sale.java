package com.dulno.core.sale;

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
public final class Sale {
  public static Sale of(DatabaseRow row, DatabaseTable table) {
    return of(row, table.columns().stream().map(DatabaseColumn::name).toList());
  }

  public static Sale of(DatabaseRow row, List<String> columns) {
    return create(row.findCell(columns.indexOf("id")).uuidValue(),
      row.findCell(columns.indexOf("sender")).stringValue(),
      row.findCell(columns.indexOf("first_name")).stringValue(),
      row.findCell(columns.indexOf("last_name")).stringValue(),
      row.findCell(columns.indexOf("phone_number")).stringValue(),
      row.findCell(columns.indexOf("country")).stringValue(),
      row.findCell(columns.indexOf("company_name")).stringValue(),
      row.findCell(columns.indexOf("company_size")).stringValue(),
      row.findCell(columns.indexOf("company_role")).stringValue(),
      row.findCell(columns.indexOf("title")).stringValue(),
      Status.valueOf(row.findCell(columns.indexOf("status")).stringValue()),
      row.findCell(columns.indexOf("expiration")).longValue());
  }

  public enum Status {
    OPEN,
    CLOSED
  }

  private final UUID id;
  private final String sender;
  private final String firstName;
  private final String lastName;
  private final String phoneNumber;
  private final String country;
  private final String companyName;
  private final String companySize;
  private final String companyRole;
  private final String title;
  private Status status;
  private long expiration;

  public void updateStatus(Status newStatus) {
    status = newStatus;
  }

  private static final long EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 14;

  public void resetExpiration() {
    expiration = System.currentTimeMillis() + EXPIRATION_TIME;
  }

  public void disableExpiration() {
    expiration = -1;
  }
}
