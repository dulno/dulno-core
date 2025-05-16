package com.dulno.core.user.statistic;

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
public class UserAppOpenDatabaseTableTransformation implements DatabaseTransformation {
  @Override
  public CompletableFuture<DatabaseRow> transformOldToNew(DatabaseRow oldRow) {
    return CompletableFuture.completedFuture(DatabaseRow.of(
      oldRow.findCell(0).uuidValue(), oldRow.findCell(1).longValue(), ""));
  }

  @Override
  public CompletableFuture<DatabaseRow> transformNewToOld(DatabaseRow newRow) {
    return CompletableFuture.completedFuture(DatabaseRow.of(
      newRow.findCell(0).uuidValue(), newRow.findCell(1).longValue()));
  }

  @Override
  public List<DatabaseColumn> oldColumns() {
    var columns = Lists.<DatabaseColumn>newArrayList();
    columns.add(DatabaseColumn.create("id", DatabaseDataType.UUID,
      DatabaseColumn.Type.PRIMARY_KEY));
    columns.add(DatabaseColumn.create("date", DatabaseDataType.BIGINT));
    return columns;
  }

  @Override
  public void initializeNewTableIndexes(DatabaseTable newTable) {

  }

  @Override
  public void initializeNewTableViews(DatabaseTable newTable) {

  }
}
