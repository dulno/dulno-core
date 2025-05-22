package com.dulno.core.member.session;

import com.dulno.core.database.DatabaseColumn;
import com.dulno.core.database.DatabaseRow;
import com.dulno.core.database.DatabaseTable;
import com.dulno.core.session.SessionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor(staticName = "create")
public final class MemberSession {
  public static MemberSession of(DatabaseRow row, DatabaseTable table) {
    return of(row, table.columns().stream().map(DatabaseColumn::name).toList());
  }

  public static MemberSession of(DatabaseRow row, List<String> columns) {
    return create(row.findCell(columns.indexOf("id")).uuidValue(),
      row.findCell(columns.indexOf("member")).uuidValue(),
      SessionStatus.valueOf(row.findCell(columns.indexOf("status")).stringValue()),
      row.findCell(columns.indexOf("device_platform")).stringValue(),
      row.findCell(columns.indexOf("ip_address")).stringValue(),
      row.findCell(columns.indexOf("country")).stringValue(),
      row.findCell(columns.indexOf("city")).stringValue(),
      row.findCell(columns.indexOf("open_time")).longValue(),
      row.findCell(columns.indexOf("refresh_token")).stringValue(),
      row.findCell(columns.indexOf("last_refresh")).longValue());
  }

  private final UUID id;
  private final UUID memberId;
  private SessionStatus status;
  private final String devicePlatform;
  private final String ipAddress;
  private final String country;
  private final String city;
  private final long openTime;
  private String lastRefreshToken;
  private long lastRefresh;

  public void close() {
    status = SessionStatus.CLOSED;
  }

  public void updateRefreshToken(String refreshToken) {
    lastRefreshToken = refreshToken;
    lastRefresh = System.currentTimeMillis();
  }
}
