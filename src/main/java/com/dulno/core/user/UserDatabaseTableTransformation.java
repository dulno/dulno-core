package com.dulno.core.user;

import com.dulno.core.database.DatabaseColumn;
import com.dulno.core.database.DatabaseDataType;
import com.dulno.core.database.DatabaseRow;
import com.dulno.core.database.DatabaseTable;
import com.dulno.core.database.transformation.DatabaseTransformation;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor(staticName = "create")
public class UserDatabaseTableTransformation implements DatabaseTransformation {
  @Override
  public CompletableFuture<DatabaseRow> transformOldToNew(DatabaseRow oldRow) {
    return CompletableFuture.completedFuture(DatabaseRow.of(
      oldRow.findCell(0).uuidValue(), oldRow.findCell(2).stringValue(),
      oldRow.findCell(3).stringValue(), oldRow.findCell(4).booleanValue(),
      oldRow.findCell(5).booleanValue()));
  }

  @Override
  public CompletableFuture<DatabaseRow> transformNewToOld(DatabaseRow newRow) {
    return CompletableFuture.completedFuture(DatabaseRow.of(
      newRow.findCell(0).uuidValue(), "", newRow.findCell(1).stringValue(),
      newRow.findCell(2).stringValue(), newRow.findCell(3).booleanValue(),
      newRow.findCell(4).booleanValue(), newRow.findCell(5).longValue()));
  }

  @Override
  public List<DatabaseColumn> oldColumns() {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("id", DatabaseDataType.UUID,
      DatabaseColumn.Type.PRIMARY_KEY));
    columns.add(DatabaseColumn.create("authentication_key", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("email", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("language", DatabaseDataType.TEXT));
    columns.add(DatabaseColumn.create("compliant", DatabaseDataType.BOOLEAN));
    columns.add(DatabaseColumn.create("newsletter", DatabaseDataType.BOOLEAN));
    columns.add(DatabaseColumn.create("accession", DatabaseDataType.BIGINT));
    return columns;
  }

  @Override
  public void initializeNewTableIndexes(DatabaseTable newTable) {

  }

  @Override
  public void initializeNewTableViews(DatabaseTable newTable) {
    ((UserDatabaseTable) newTable).initializeViews();
  }
}
