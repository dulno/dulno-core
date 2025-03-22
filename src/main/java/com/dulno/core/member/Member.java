package com.dulno.core.member;

import com.dulno.core.database.DatabaseRow;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor(staticName = "create")
public final class Member {
  public static Member of(DatabaseRow row) {
    return create(row.findCell(0).uuidValue(), row.findCell(1).stringValue(),
      row.findCell(2).stringValue(), row.findCell(3).stringValue(),
      row.findCell(4).stringValue(), row.findCell(5).booleanValue(),
      row.findCell(6).booleanValue(), row.findCell(7).longValue());
  }

  public static Member unknown(UUID id) {
    return create(id, "Unknown", "Unknown", "", "", true, true, -1);
  }

  private final UUID id;
  private String name;
  private String email;
  private String passwordHash;
  private String language;
  private final boolean legalAccepted;
  private final boolean newsletter;
  private final long joinDate;

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
