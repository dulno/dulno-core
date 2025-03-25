package com.dulno.core.user;

import com.dulno.core.database.DatabaseRow;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor(staticName = "create")
public final class User {
  public static User of(DatabaseRow row) {
    return create(row.findCell(0).uuidValue(), row.findCell(1).stringValue(),
      row.findCell(2).stringValue(), row.findCell(3).booleanValue(),
      row.findCell(4).booleanValue(), row.findCell(5).longValue());
  }

  private final UUID id;
  private String email;
  private String language;
  private final boolean compliant;
  private final boolean newsletter;
  private final long accession;

  public void changeEmail(String newEmail) {
    email = newEmail;
  }

  public void changeLanguage(String newLanguage) {
    language = newLanguage;
  }
}