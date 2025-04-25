package com.dulno.core.question;

import com.dulno.core.database.DatabaseConnection;
import com.dulno.core.database.DatabaseKeyspace;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class QuestionInjectionModule extends AbstractModule {
  @Provides
  @Singleton
  QuestionDatabaseTable provideQuestionDatabaseTable(
    DatabaseConnection databaseConnection, DatabaseKeyspace databaseKeyspace
  ) {
    return QuestionDatabaseTable.create(databaseConnection, databaseKeyspace);
  }

  @Provides
  @Singleton
  QuestionMessageDatabaseTable provideQuestionMessageDatabaseTable(
    DatabaseConnection databaseConnection, DatabaseKeyspace databaseKeyspace
  ) {
    var questionMessageDatabaseTable = QuestionMessageDatabaseTable.create(
      databaseConnection, databaseKeyspace);
    questionMessageDatabaseTable.createIfNotExists();
    questionMessageDatabaseTable.createIndexIfNotExists("public_id");
    questionMessageDatabaseTable.createIndexIfNotExists("question_id");
    return questionMessageDatabaseTable;
  }
}
