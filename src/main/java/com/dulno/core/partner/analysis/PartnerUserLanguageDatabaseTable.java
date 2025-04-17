package com.dulno.core.partner.analysis;

import com.datastax.oss.driver.shaded.guava.common.collect.Maps;
import com.dulno.core.database.*;
import com.dulno.core.database.condition.DatabaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class PartnerUserLanguageDatabaseTable extends DatabaseTable {
  private static final String TABLE_NAME = "partner_user_language";

  public static PartnerUserLanguageDatabaseTable create(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("partner", DatabaseDataType.UUID,
      DatabaseColumn.Type.PARTITION_KEY));
    columns.add(DatabaseColumn.create("language", DatabaseDataType.TEXT,
      DatabaseColumn.Type.CLUSTERING_KEY));
    columns.add(DatabaseColumn.create("number", DatabaseDataType.COUNTER));
    var table = new PartnerUserLanguageDatabaseTable(connection, keyspace,
      TABLE_NAME, columns);
    table.createIfNotExists();;
    return table;
  }

  private PartnerUserLanguageDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace, String name,
    List<DatabaseColumn> columns
  ) {
    super(connection, keyspace, name, columns);
  }

  public CompletableFuture<Void> addPartnerUserLanguage(
    UUID partnerId, String language
  ) {
    return updatePartnerUserLanguage(partnerId, language, 1);
  }

  private CompletableFuture<Void> updatePartnerUserLanguage(
    UUID partnerId, String language, long numberAddition
  ) {
    var condition = DatabaseCondition.of("partner", partnerId,
      "language", language);
    return updateCounter(condition, DatabaseRow.of(partnerId, language,
      numberAddition));
  }

  public CompletableFuture<Void> deletePartnerUserLanguage(
    UUID partnerId, String language
  ) {
    return delete(DatabaseCondition.of("partner", partnerId,
      "language", language));
  }

  public CompletableFuture<Boolean> partnerUserLanguageExists(
    UUID partnerId, String language
  ) {
    return exists(DatabaseCondition.of("partner", partnerId,
      "language", language));
  }

  public CompletableFuture<Long> findPartnerUserLanguageNumber(
    UUID partnerId, String language
  ) {
    var condition = DatabaseCondition.of("partner", partnerId,
      "language", language);
    return selectRow(condition).thenApply(row -> row.findCell(2).longValue());
  }

  public CompletableFuture<Map<String, Long>> findPartnerUserLanguages(
    UUID partnerId
  ) {
    return selectRows(DatabaseCondition.of("partner", partnerId))
      .thenApply(this::assemblyPartnerUserLanguages);
  }

  private Map<String, Long> assemblyPartnerUserLanguages(
    List<DatabaseRow> rows
  ) {
    var languages = Maps.<String, Long>newHashMap();
    for (DatabaseRow row : rows) {
      languages.put(row.findCell(1).stringValue(), row.findCell(2).longValue());
    }
    return languages;
  }
}
