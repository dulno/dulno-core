package com.dulno.core.stamp;

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
public final class Stamp {
  public static Stamp of(DatabaseRow row, DatabaseTable table) {
    return of(row, table.columns().stream().map(DatabaseColumn::name).toList());
  }

  public static Stamp of(DatabaseRow row, List<String> columns) {
    return create(row.findCell(columns.indexOf("id")).uuidValue(),
      row.findCell(columns.indexOf("partner")).uuidValue(),
      row.findCell(columns.indexOf("uid")).stringValue(),
      row.findCell(columns.indexOf("master_key")).stringValue(),
      row.findCell(columns.indexOf("name")).stringValue(),
      row.findCell(columns.indexOf("assigned_card")).uuidValue(),
      row.findCell(columns.indexOf("creation")).longValue());
  }

  private final UUID id;
  private final UUID partnerId;
  private String uid;
  private String masterKey;
  private String name;
  private UUID assignedCard;
  private final long creation;

  public void updateUid(String newUid) {
    this.uid = newUid;
  }

  public void updateMasterKey(String newMasterKey) {
    this.masterKey = newMasterKey;
  }

  public void changeName(String newName) {
    name = newName;
  }

  public void changeAssignedCard(UUID newAssignedCard) {
    assignedCard = newAssignedCard;
  }
}
