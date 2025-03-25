package com.dulno.core.member;

import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class MemberDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "member";

  public static MemberDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("id", DatabaseDataType.UUID,
      DatabaseColumn.Type.PRIMARY_KEY));
    columns.add(DatabaseColumn.create("name", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("email", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("password", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("language", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("compliant", DatabaseDataType.BOOLEAN));
    columns.add(DatabaseColumn.create("newsletter", DatabaseDataType.BOOLEAN));
    columns.add(DatabaseColumn.create("accession", DatabaseDataType.BIGINT));
    var table = new MemberDatabaseTable(connection, keyspace, TABLE_NAME, columns);
    table.createIfNotExists();
    table.initializeViews();
    return table;
  }

  private DatabaseTable emailView;

  private MemberDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  private void initializeViews() {
    emailView = createMaterializedViewIfNotExists("email_view", "email");
  }

  public CompletableFuture<Void> insertMember(Member member) {
    return insertMember(member.id(), member.name(), member.email(),
      member.passwordHash(), member.language(), member.compliant(),
      member.newsletter(), member.accession());
  }

  public CompletableFuture<Void> insertMember(
    UUID id, String name, String email, String passwordHash, String language,
    boolean compliant, boolean newsletter, long accession
  ) {
    return insert(DatabaseRow.of(id, name, email.toLowerCase(), passwordHash,
      language, compliant, newsletter, accession));
  }

  public CompletableFuture<Void> changeMemberName(UUID memberId, String newName) {
    return findMember(memberId).thenCompose(member -> changeMemberName(member, newName));
  }

  private CompletableFuture<Void> changeMemberName(Member member, String newName) {
    member.changeName(newName);
    return updateMember(member);
  }

  public CompletableFuture<Void> changeMemberEmail(UUID memberId, String newEmail) {
    return findMember(memberId).thenCompose(member -> changeMemberEmail(member, newEmail));
  }

  private CompletableFuture<Void> changeMemberEmail(Member member, String newEmail) {
    member.changeEmail(newEmail);
    return updateMember(member);
  }

  public CompletableFuture<Void> changeMemberPassword(
    UUID memberId, String newPasswordHash
  ) {
    return findMember(memberId).thenCompose(member ->
      changeMemberPassword(member, newPasswordHash));
  }

  private CompletableFuture<Void> changeMemberPassword(
    Member member, String newPasswordHash
  ) {
    member.changePassword(newPasswordHash);
    return updateMember(member);
  }

  public CompletableFuture<Void> changeMemberLanguage(UUID memberId, String newLanguage) {
    return findMember(memberId).thenCompose(member -> changeMemberLanguage(member, newLanguage));
  }

  private CompletableFuture<Void> changeMemberLanguage(Member member, String newLanguage) {
    member.changeLanguage(newLanguage);
    return updateMember(member);
  }

  private CompletableFuture<Void> updateMember(Member member) {
    return update(member.id(), DatabaseRow.of(member.id(), member.name(),
      member.email().toLowerCase(), member.passwordHash(), member.language(),
      member.compliant(), member.newsletter(), member.accession()));
  }

  public CompletableFuture<UUID> generateAvailableMemberId() {
    var futureResponse = new CompletableFuture<UUID>();
    var id = UUID.randomUUID();
    memberExists(id).thenApply(exists -> exists ?
      generateAvailableMemberId().thenApply(futureResponse::complete) :
      CompletableFuture.completedFuture(futureResponse.complete(id)));
    return futureResponse;
  }

  public CompletableFuture<Boolean> memberExists(UUID memberId) {
    return exists(memberId);
  }

  public CompletableFuture<Boolean> memberExists(String email) {
    return emailView.exists(DatabaseCondition.of("email", email.toLowerCase()));
  }

  public CompletableFuture<Void> deleteMember(UUID memberId) {
    return delete(memberId);
  }

  public CompletableFuture<Member> findMember(UUID memberId) {
    return selectRow(memberId).thenApply(Member::of);
  }

  public CompletableFuture<Member> findMemberIfExists(UUID memberId) {
    var futureResponse = new CompletableFuture<Member>();
    memberExists(memberId).thenAccept(exists -> completeExistenceMemberFinding(
      memberId, exists).thenAccept(futureResponse::complete));
    return futureResponse;
  }

  private CompletableFuture<Member> completeExistenceMemberFinding(
    UUID memberId, boolean exists
  ) {
    if (!exists) {
      return CompletableFuture.completedFuture(Member.unknown(memberId));
    }
    return findMember(memberId);
  }

  public CompletableFuture<Member> findMember(String email) {
    return emailView.selectRow(DatabaseCondition.of("email", email.toLowerCase()))
      .thenApply(Member::of);
  }
}
