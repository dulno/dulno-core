package com.dulno.core.user.session;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.dulno.core.session.SessionStatus;
import com.google.common.collect.Lists;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class UserSessionDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "user_session";

  public static UserSessionDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("id", DatabaseDataType.UUID,
      DatabaseColumn.Type.PARTITION_KEY));
    columns.add(DatabaseColumn.create("user", DatabaseDataType.UUID,
      DatabaseColumn.Type.CLUSTERING_KEY));
    columns.add(DatabaseColumn.create("status", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("device_platform", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("ip_address", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("country", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("city", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("open_time", DatabaseDataType.BIGINT));
    columns.add(DatabaseColumn.create("refresh_token", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("last_refresh", DatabaseDataType.BIGINT));
    var table = new UserSessionDatabaseTable(connection, keyspace, TABLE_NAME, columns);
    table.createIfNotExists();
    table.createIndexIfNotExists("user");
    table.createIndexIfNotExists("status");
    table.initializeViews();
    return table;
  }

  private DatabaseTable userStatusView;

  private UserSessionDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  private void initializeViews() {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("user", DatabaseDataType.UUID,
      DatabaseColumn.Type.PARTITION_KEY));
    columns.add(DatabaseColumn.create("status", DatabaseDataType.TEXT,
      DatabaseColumn.Type.CLUSTERING_KEY));
    columns.add(DatabaseColumn.create("id", DatabaseDataType.UUID,
      DatabaseColumn.Type.CLUSTERING_KEY));
    userStatusView = createMaterializedViewIfNotExists("user_status_view", columns);
  }

  public CompletableFuture<Void> insertSession(UserSession session) {
    return insertSession(session.id(), session.userId(), session.status(),
      session.devicePlatform(), session.ipAddress(), session.country(),
      session.city(), session.openTime(), session.lastRefreshToken(),
      session.lastRefresh());
  }

  public CompletableFuture<Void> insertSession(
    UUID id, UUID userId, SessionStatus status, String devicePlatform,
    String ipAddress, String country, String city, long openTime,
    String refreshToken, long lastRefresh
  ) {
    return insert(DatabaseRow.of(id, userId, status.toString(), devicePlatform,
      ipAddress, country, city, openTime, refreshToken, lastRefresh));
  }

  public CompletableFuture<Void> updateSessionRefreshToken(
    UUID id, String refreshToken
  ) {
    var futureResponse = new CompletableFuture<Void>();
    findSession(id)
      .thenAccept(session -> updateSessionRefreshToken(session, refreshToken)
      .thenAccept(futureResponse::complete));
    return futureResponse;
  }

  public CompletableFuture<Void> updateSessionRefreshToken(
    UserSession session, String refreshToken
  ) {
    session.updateRefreshToken(refreshToken);
    return updateSession(session);
  }

  public CompletableFuture<Void> closeSession(UUID id) {
    var futureResponse = new CompletableFuture<Void>();
    findSession(id).thenAccept(session -> closeSession(session)
      .thenAccept(futureResponse::complete));
    return futureResponse;
  }

  public CompletableFuture<Void> closeSession(UserSession session) {
    session.close();
    return updateSession(session);
  }

  public CompletableFuture<Void> updateSession(UserSession session) {
    return update(DatabaseCondition.of("id", session.id(), "user", session.userId()),
      DatabaseRow.of(session.id(), session.userId(), session.status().toString(),
        session.devicePlatform(), session.ipAddress(), session.country(),
        session.city(), session.openTime(), session.lastRefreshToken(),
        session.lastRefresh()));
  }

  public CompletableFuture<Void> deleteSession(UUID id) {
    return delete(DatabaseCondition.of("id", id));
  }

  public CompletableFuture<UUID> generateAvailableSessionId() {
    var futureResponse = new CompletableFuture<UUID>();
    var id = UUID.randomUUID();
    sessionExists(id).thenApply(exists -> exists ?
      generateAvailableSessionId().thenApply(futureResponse::complete) :
      CompletableFuture.completedFuture(futureResponse.complete(id)));
    return futureResponse;
  }

  public CompletableFuture<Boolean> sessionExists(UUID id) {
    return exists(DatabaseCondition.of("id", id));
  }

  public CompletableFuture<UserSession> findSession(UUID id) {
    return selectRow(DatabaseCondition.of("id", id))
      .thenApply(row -> UserSession.of(row, this));
  }

  public CompletableFuture<List<UserSession>> findSessionsOfUser(UUID userId) {
    return selectRows(DatabaseCondition.of("user", userId))
      .thenApply(rows -> rows.stream().map(row -> UserSession.of(row, this)).toList());
  }

  public CompletableFuture<List<UserSession>> findSessionsOfUserByStatus(
    UUID userId, SessionStatus status
  ) {
    var condition = DatabaseCondition.of("user", userId, "status", status.toString());
    return userStatusView.selectRows(condition)
      .thenApply(rows -> rows.stream().map(row -> UserSession.of(row, userStatusView))
        .sorted(Comparator.comparingLong(UserSession::openTime).reversed()).toList());
  }

  public CompletableFuture<List<UserSession>> findAllSessionsByStatus(
    SessionStatus status
  ) {
    return selectRows(DatabaseCondition.of("status", status.toString()))
      .thenApply(rows -> rows.stream().map(row -> UserSession.of(row, this)).toList());
  }
}