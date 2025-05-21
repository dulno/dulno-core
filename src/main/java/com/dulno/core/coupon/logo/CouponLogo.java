package com.dulno.core.coupon.logo;

import com.dulno.core.database.DatabaseRow;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor(staticName = "create")
public final class CouponLogo {
  public static CouponLogo of(DatabaseRow row) {
    return create(row.findCell(0).uuidValue(), row.findCell(1).uuidValue(),
      row.findCell(2).blobValue().array());
  }

  private final UUID couponId;
  private final UUID logoId;
  private final byte[] content;
}