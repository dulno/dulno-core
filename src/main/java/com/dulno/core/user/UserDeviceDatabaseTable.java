package com.dulno.core.user;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class UserDeviceDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "user_device";

  public static UserDeviceDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("id", DatabaseDataType.UUID,
      DatabaseColumn.Type.PRIMARY_KEY));
    columns.add(DatabaseColumn.create("user", DatabaseDataType.UUID));
    columns.add(DatabaseColumn.create("device_id", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("operating_system", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("operating_system_version", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("brand", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("model", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("name", DatabaseDataType.TEXT));
    var table = new UserDeviceDatabaseTable(connection, keyspace, TABLE_NAME, columns);
    table.createIfNotExists();
    table.initializeViews();
    return table;
  }

  private DatabaseTable userView;

  private UserDeviceDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  private void initializeViews() {
    userView = createMaterializedViewIfNotExists("user_view", "user",
      DatabaseColumn.Type.PARTITION_KEY);
  }

  public CompletableFuture<Void> insertUserDevice(UserDevice device) {
    return insertUserDevice(device.id(), device.userId(), device.deviceId(),
      device.operatingSystem(), device.operatingSystemVersion(), device.brand(),
      device.model(), device.name());
  }

  public CompletableFuture<Void> insertUserDevice(
    UUID id, UUID userId, String deviceId, String operatingSystem,
    String operatingSystemVersion, String brand, String model, String name
  ) {
    return insert(DatabaseRow.of(id, userId, deviceId, operatingSystem,
      operatingSystemVersion, brand, model, name));
  }

  public CompletableFuture<UUID> generateAvailableUserDeviceId() {
    var futureResponse = new CompletableFuture<UUID>();
    var id = UUID.randomUUID();
    userDeviceExists(id).thenApply(exists -> exists ?
      generateAvailableUserDeviceId().thenApply(futureResponse::complete) :
      CompletableFuture.completedFuture(futureResponse.complete(id)));
    return futureResponse;
  }

  public CompletableFuture<Void> transferUserDevice(
    UUID deviceId, UUID newUser
  ) {
    return findUserDevice(deviceId).thenCompose(user ->
      transferUserDevice(user, newUser));
  }

  private CompletableFuture<Void> transferUserDevice(
    UserDevice device, UUID newUser
  ) {
    device.transfer(newUser);
    return updateUserDevice(device);
  }

  private CompletableFuture<Void> updateUserDevice(UserDevice device) {
    return update(device.id(), DatabaseRow.of(device.id(), device.userId(),
      device.deviceId(), device.operatingSystem(), device.operatingSystemVersion(),
      device.brand(), device.model(), device.name()));
  }

  public CompletableFuture<Boolean> userDeviceExists(UUID id) {
    return exists(id);
  }

  public CompletableFuture<Void> deleteUserDevice(UUID id) {
    return delete(id);
  }

  public CompletableFuture<UserDevice> findUserDevice(UUID id) {
    return selectRow(id).thenApply(row -> UserDevice.of(row, this));
  }

  public CompletableFuture<List<UserDevice>> findUserDevices(UUID userId) {
    return userView.selectRows(DatabaseCondition.of("user", userId))
      .thenApply(rows -> rows.stream().map(row -> UserDevice.of(row, this))
        .toList());
  }
}
