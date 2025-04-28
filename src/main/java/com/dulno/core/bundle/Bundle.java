package com.dulno.core.bundle;

import com.dulno.core.database.DatabaseRow;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.json.JSONObject;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor(staticName = "create")
public final class Bundle {
  public static Bundle of(DatabaseRow row) {
    var content = new JSONObject(row.findCell(5).stringValue());
    return create(row.findCell(0).uuidValue(),
      BundleType.valueOf(row.findCell(1).stringValue()),
      BundleRuntime.valueOf(row.findCell(2).stringValue()),
      row.findCell(3).doubleValue(), row.findCell(4).longValue(),
      content.getBoolean("portal_access"),
      content.getBoolean("collection_card_access"),
      content.getBoolean("value_card_access"),
      content.getBoolean("member_card_access"),
      content.getBoolean("analysis_access"),
      content.getBoolean("balances_access"),
      content.getBoolean("campaign_access"),
      content.getBoolean("notification_access"));
  }

  public static Bundle of(
    UUID partnerId, BundlePreset preset, BundleRuntime runtime
  ) {
    return of(partnerId, preset, runtime, calculatePresetPrice(preset, runtime));
  }

  private static double calculatePresetPrice(
    BundlePreset preset, BundleRuntime runtime
  ) {
    return runtime.isMonthly() ? preset.monthlyPrice() : preset.yearlyPrice();
  }

  public static Bundle of(
    UUID partnerId, BundlePreset preset, BundleRuntime runtime, double price
  ) {
    return create(partnerId, preset.bundleType(), runtime, price,
      calculateBundleExpiration(runtime), preset.portalAccess(),
      preset.collectionCardAccess(), preset.valueCardAccess(),
      preset.memberCardAccess(), preset.analysisAccess(), preset.balancesAccess(),
      preset.campaignAccess(), preset.notificationAccess());
  }

  public static Bundle of(
    UUID partnerId, BundlePreset preset, BundleRuntime runtime, double price,
    long expiration
  ) {
    return create(partnerId, preset.bundleType(), runtime,
      price, runtime.isUnbound() ? expiration : calculateBundleExpiration(runtime),
      preset.portalAccess(), preset.collectionCardAccess(), preset.valueCardAccess(),
      preset.memberCardAccess(), preset.analysisAccess(), preset.balancesAccess(),
      preset.campaignAccess(), preset.notificationAccess());
  }

  private static long calculateBundleExpiration(BundleRuntime runtime) {
    var current = ZonedDateTime.now();
    var next = current.plusMonths(runtime.isMonthly() ? 1 : 12);
    if (next.getDayOfMonth() != current.getDayOfMonth()) {
      next = next.withDayOfMonth(next.getMonth().length(
        next.toLocalDate().isLeapYear()));
    }
    return next.toInstant().toEpochMilli();
  }

  private final UUID partnerId;
  private final BundleType bundleType;
  private final BundleRuntime bundleRuntime;
  private final double price;
  private long expiration;
  private final boolean portalAccess;
  private final boolean collectionCardAccess;
  private final boolean valueCardAccess;
  private final boolean memberCardAccess;
  private final boolean analysisAccess;
  private final boolean balancesAccess;
  private final boolean campaignAccess;
  private final boolean notificationAccess;

  public void extend() {
    expiration = calculateBundleExpiration(bundleRuntime);
  }

  public String serializeContent() {
    var content = new JSONObject();
    content.put("portal_access", portalAccess);
    content.put("collection_card_access", collectionCardAccess);
    content.put("value_card_access", valueCardAccess);
    content.put("member_card_access", memberCardAccess);
    content.put("analysis_access", analysisAccess);
    content.put("balances_access", balancesAccess);
    content.put("campaign_access", campaignAccess);
    content.put("notification_access", notificationAccess);
    return content.toString();
  }
}
