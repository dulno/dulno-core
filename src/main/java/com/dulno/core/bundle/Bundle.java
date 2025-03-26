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
    var content = new JSONObject(row.findCell(5).longValue());
    return create(row.findCell(0).uuidValue(),
      BundleType.valueOf(row.findCell(1).stringValue()),
      BundleRuntime.valueOf(row.findCell(2).stringValue()),
      row.findCell(3).doubleValue(), row.findCell(4).longValue(),
      content.getBoolean("collection_card_access"),
      content.getBoolean("value_card_access"),
      content.getBoolean("member_card_access"),
      content.getBoolean("analysis_access"),
      content.getBoolean("action_access"),
      content.getBoolean("notification_access"),
      content.getBoolean("portal_access"),
      content.getBoolean("multi_portal_user_access"),
      content.getBoolean("portal_permission_access"));
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
      calculateBundleExpiration(runtime), preset.collectionCardAccess(),
      preset.valueCardAccess(), preset.memberCardAccess(),
      preset.analysisAccess(), preset.actionAccess(), preset.notificationAccess(),
      preset.portalAccess(), preset.multiPortalUserAccess(),
      preset.portalPermissionAccess());
  }

  public static Bundle of(
    UUID partnerId, BundlePreset preset, BundleRuntime runtime, double price,
    long expiration
  ) {
    return create(partnerId, preset.bundleType(), runtime,
      price, runtime.isUnbound() ? expiration : calculateBundleExpiration(runtime),
      preset.collectionCardAccess(), preset.valueCardAccess(),
      preset.memberCardAccess(), preset.analysisAccess(), preset.actionAccess(),
      preset.notificationAccess(), preset.portalAccess(),
      preset.multiPortalUserAccess(), preset.portalPermissionAccess());
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
  private final boolean collectionCardAccess;
  private final boolean valueCardAccess;
  private final boolean memberCardAccess;
  private final boolean analysisAccess;
  private final boolean actionAccess;
  private final boolean notificationAccess;
  private final boolean portalAccess;
  private final boolean multiPortalUserAccess;
  private final boolean portalPermissionAccess;

  public void extend() {
    expiration = calculateBundleExpiration(bundleRuntime);
  }

  public String serializeContent() {
    var content = new JSONObject();
    content.put("collection_card_access", collectionCardAccess);
    content.put("value_card_access", valueCardAccess);
    content.put("member_card_access", memberCardAccess);
    content.put("analysis_access", analysisAccess);
    content.put("action_access", actionAccess);
    content.put("notification_access", notificationAccess);
    content.put("portal_access", portalAccess);
    content.put("multi_portal_user_access", multiPortalUserAccess);
    content.put("portal_permission_access", portalPermissionAccess);
    return content.toString();
  }
}
