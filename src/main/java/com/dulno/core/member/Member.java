package com.dulno.core.member;

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
public final class Member {
  public static Member of(DatabaseRow row, DatabaseTable table) {
    return of(row, table.columns().stream().map(DatabaseColumn::name).toList());
  }

  public static Member of(DatabaseRow row, List<String> columns) {
    return create(row.findCell(columns.indexOf("id")).uuidValue(),
      row.findCell(columns.indexOf("name")).stringValue(),
      row.findCell(columns.indexOf("email")).stringValue(),
      row.findCell(columns.indexOf("password")).stringValue(),
      row.findCell(columns.indexOf("language")).stringValue(),
      row.findCell(columns.indexOf("compliant")).booleanValue(),
      row.findCell(columns.indexOf("newsletter")).booleanValue(),
      row.findCell(columns.indexOf("accession")).longValue());
  }

  public static Member unknown(UUID id) {
    return create(id, "Unknown", "Unknown", "", "", true, true, -1);
  }

  private final UUID id;
  private String name;
  private String email;
  private String passwordHash;
  private String language;
  private final boolean compliant;
  private final boolean newsletter;
  private final long accession;

  public void changeName(String newName) {
    name = newName;
  }

  public void changeEmail(String newEmail) {
    email = newEmail;
  }

  public void changePassword(String newPasswordHash) {
    passwordHash = newPasswordHash;
  }

  public void changeLanguage(String newLanguage) {
    language = newLanguage;
  }
}
