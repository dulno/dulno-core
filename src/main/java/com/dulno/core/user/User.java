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
public final class User {
  public static User of(DatabaseRow row, DatabaseTable table) {
    return of(row, table.columns().stream().map(DatabaseColumn::name).toList());
  }

  public static User of(DatabaseRow row, List<String> columns) {
    return create(row.findCell(columns.indexOf("id")).uuidValue(),
      row.findCell(columns.indexOf("authentication_key")).stringValue(),
      row.findCell(columns.indexOf("email")).stringValue(),
      row.findCell(columns.indexOf("language")).stringValue(),
      row.findCell(columns.indexOf("compliant")).booleanValue(),
      row.findCell(columns.indexOf("newsletter")).booleanValue(),
      row.findCell(columns.indexOf("accession")).longValue());
  }

  private final UUID id;
  private final String authenticationKey;
  private String email;
  private String language;
  private final boolean compliant;
  private boolean newsletter;
  private final long accession;

  public void changeEmail(String newEmail) {
    email = newEmail;
  }

  public void changeLanguage(String newLanguage) {
    language = newLanguage;
  }

  public void changeNewsletter(boolean newNewsletter) {
    newsletter = newNewsletter;
  }
}