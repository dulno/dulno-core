package com.dulno.core.partner;

import com.dulno.core.database.*;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class PartnerDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "partner";

  public static PartnerDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("id", DatabaseDataType.UUID,
      DatabaseColumn.Type.PRIMARY_KEY));
    columns.add(DatabaseColumn.create("name", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("description", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("accession", DatabaseDataType.BIGINT));
    var table = new PartnerDatabaseTable(connection, keyspace, TABLE_NAME, columns);
    table.createIfNotExists();
    return table;
  }

  private PartnerDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  public CompletableFuture<Void> insertPartner(Partner partner) {
    return insertPartner(partner.id(), partner.name(), partner.description(),
      partner.accession());
  }

  public CompletableFuture<Void> insertPartner(
    UUID id, String name, String description, long accession
  ) {
    return insert(DatabaseRow.of(id, name, description, accession));
  }

  public CompletableFuture<Void> updatePartner(Partner partner) {
    return update(partner.id(), DatabaseRow.of(partner.id(), partner.name(),
      partner.description(), partner.accession()));
  }

  public CompletableFuture<UUID> generateAvailablePartnerId() {
    var futureResponse = new CompletableFuture<UUID>();
    var id = UUID.randomUUID();
    partnerExists(id).thenApply(exists -> exists ?
      generateAvailablePartnerId().thenApply(futureResponse::complete) :
      CompletableFuture.completedFuture(futureResponse.complete(id)));
    return futureResponse;
  }

  public CompletableFuture<Boolean> partnerExists(UUID partnerId) {
    return exists(partnerId);
  }

  public CompletableFuture<Void> deletePartner(UUID partnerId) {
    return delete(partnerId);
  }

  public CompletableFuture<Partner> findPartner(UUID partnerId) {
    return selectRow(partnerId).thenApply(Partner::of);
  }

  public CompletableFuture<List<Partner>> findAllPartners() {
    return selectAllRows().thenApply(rows ->
      rows.stream().map(Partner::of).toList());
  }
}