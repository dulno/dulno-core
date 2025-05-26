package com.dulno.core.coupon;

import com.dulno.core.coupon.logo.CouponLogoDatabaseTable;
import com.dulno.core.database.DatabaseConnection;
import com.dulno.core.database.DatabaseKeyspace;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class CouponInjectionModule extends AbstractModule {
  @Provides
  @Singleton
  CouponDatabaseTable provideCouponDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return CouponDatabaseTable.create(connection, keyspace);
  }

  @Provides
  @Singleton
  CouponLogoDatabaseTable provideCouponLogoDatabaseTable(
    DatabaseConnection connection, DatabaseKeyspace keyspace
  ) {
    return CouponLogoDatabaseTable.create(connection, keyspace);
  }
}

