package com.dulno.core.campaign.notification;

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
public final class CampaignNotification {
  public static CampaignNotification of(DatabaseRow row, DatabaseTable table) {
    return of(row, table.columns().stream().map(DatabaseColumn::name).toList());
  }

  public static CampaignNotification of(DatabaseRow row, List<String> columns) {
    return create(row.findCell(columns.indexOf("id")).uuidValue(),
      row.findCell(columns.indexOf("campaign")).uuidValue(),
      row.findCell(columns.indexOf("title")).stringValue(),
      row.findCell(columns.indexOf("body")).stringValue(),
      row.findCell(columns.indexOf("dispatch")).longValue(),
      CampaignNotificationState.valueOf(row.findCell(columns.indexOf("state"))
        .stringValue()));
  }

  private final UUID id;
  private final UUID campaignId;
  private String title;
  private String body;
  private long dispatch;
  private CampaignNotificationState state;

  public void changeTitle(String newTitle) {
    title = newTitle;
  }

  public void changeBody(String newBody) {
    body = newBody;
  }

  public void changeDispatch(long newDispatch) {
    dispatch = newDispatch;
  }

  public void changeState(CampaignNotificationState newState) {
    state = newState;
  }
}
