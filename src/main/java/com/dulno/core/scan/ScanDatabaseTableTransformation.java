package com.dulno.core.scan;

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
public class ScanDatabaseTableTransformation implements DatabaseTransformation {
  @Override
  public CompletableFuture<DatabaseRow> transformOldToNew(DatabaseRow oldRow) {
    return CompletableFuture.completedFuture(DatabaseRow.of(
      oldRow.findCell(0).uuidValue(), oldRow.findCell(1).stringValue(),
      oldRow.findCell(2).stringValue(), oldRow.findCell(3).uuidValue(),
      oldRow.findCell(4).uuidValue(), oldRow.findCell(5).uuidValue(),
      -1, oldRow.findCell(6).longValue()));
  }

  @Override
  public CompletableFuture<DatabaseRow> transformNewToOld(DatabaseRow newRow) {
    return CompletableFuture.completedFuture(DatabaseRow.of(
      newRow.findCell(0).uuidValue(), newRow.findCell(1).stringValue(),
      newRow.findCell(2).stringValue(), newRow.findCell(3).uuidValue(),
      newRow.findCell(4).uuidValue(), newRow.findCell(5).uuidValue(),
      newRow.findCell(7).longValue()));
  }

  @Override
  public List<DatabaseColumn> oldColumns() {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("id", DatabaseDataType.UUID,
      DatabaseColumn.Type.PARTITION_KEY));
    columns.add(DatabaseColumn.create("picc", DatabaseDataType.TEXT,
      DatabaseColumn.Type.CLUSTERING_KEY));
    columns.add(DatabaseColumn.create("cmac", DatabaseDataType.TEXT,
      DatabaseColumn.Type.CLUSTERING_KEY));
    columns.add(DatabaseColumn.create("stamp", DatabaseDataType.UUID));
    columns.add(DatabaseColumn.create("card", DatabaseDataType.UUID));
    columns.add(DatabaseColumn.create("partner", DatabaseDataType.UUID));
    columns.add(DatabaseColumn.create("time", DatabaseDataType.BIGINT));
    return columns;
  }

  @Override
  public void initializeNewTableIndexes(DatabaseTable newTable) {

  }

  @Override
  public void initializeNewTableViews(DatabaseTable newTable) {
    ((ScanDatabaseTable) newTable).initializeViews();
  }
}
