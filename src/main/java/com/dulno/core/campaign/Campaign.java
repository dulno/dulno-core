package com.dulno.core.campaign;

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
public final class Campaign {
  public static Campaign of(DatabaseRow row, DatabaseTable table) {
    return of(row, table.columns().stream().map(DatabaseColumn::name).toList());
  }

  public static Campaign of(DatabaseRow row, List<String> columns) {
    return create(row.findCell(columns.indexOf("id")).uuidValue(),
      row.findCell(columns.indexOf("partner")).uuidValue(),
      row.findCell(columns.indexOf("title")).stringValue(),
      row.findCell(columns.indexOf("description")).stringValue(),
      CampaignType.valueOf(row.findCell(columns.indexOf("type")).stringValue()),
      row.findCell(columns.indexOf("start")).longValue(),
      row.findCell(columns.indexOf("end")).longValue(),
      row.findCell(columns.indexOf("coupon")).uuidValue());
  }

  private final UUID id;
  private final UUID partnerId;
  private String title;
  private String description;
  private CampaignType type;
  private long start;
  private long end;
  private UUID couponId;

  public void changeTitle(String newTitle) {
    title = newTitle;
  }

  public void changeDescription(String newDescription) {
    description = newDescription;
  }

  public void changeType(CampaignType newType) {
    type = newType;
  }

  public void changeStart(long newStart) {
    start = newStart;
  }

  public void changeEnd(long newEnd) {
    end = newEnd;
  }

  public void changeCoupon(UUID newCoupon) {
    couponId = newCoupon;
  }
}
