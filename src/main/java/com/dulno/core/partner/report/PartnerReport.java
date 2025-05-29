package com.dulno.core.partner.report;

import com.dulno.core.database.DatabaseRow;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor(staticName = "create")
public final class PartnerReport {
  public static PartnerReport of(DatabaseRow row) {
    return create(row.findCell(0).uuidValue(), row.findCell(1).uuidValue(),
      row.findCell(2).stringValue(), Status.valueOf(row.findCell(3).stringValue()),
      row.findCell(4).longValue());
  }

  public enum Status {
    OPEN,
    CLOSED
  }

  private final UUID reportId;
  private final UUID partnerId;
  private final String message;
  private Status status;
  private final long creation;

  public void updateStatus(Status newStatus) {
    this.status = newStatus;
  }
}