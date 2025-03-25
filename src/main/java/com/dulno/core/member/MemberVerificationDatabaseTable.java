package com.dulno.core.member;

import com.dulno.core.database.*;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class MemberVerificationDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "member_verification";

  public static MemberVerificationDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("member", DatabaseDataType.UUID,
      DatabaseColumn.Type.PRIMARY_KEY));
    columns.add(DatabaseColumn.create("verification_token", DatabaseDataType.TEXT));
    var table = new MemberVerificationDatabaseTable(connection, keyspace,
      TABLE_NAME, columns);
    table.createIfNotExists();
    return table;
  }

  private MemberVerificationDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  public void insertVerification(UUID memberId, String token) {
    insert(DatabaseRow.of(memberId, token));
  }

  public CompletableFuture<Boolean> verificationExists(UUID memberId) {
    return exists(memberId);
  }

  public void deleteVerification(UUID memberId) {
    delete(memberId);
  }

  public CompletableFuture<String> findVerification(UUID memberId) {
    return selectRow(memberId).thenApply(row ->
      row.findCell(1).stringValue());
  }
}

