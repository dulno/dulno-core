package com.dulno.core.offer;

import com.dulno.core.bundle.*;
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
public final class Offer {
  public static Offer of(DatabaseRow row) {
    var content = new JSONObject(row.findCell(7).longValue());
    return create(row.findCell(0).uuidValue(), row.findCell(1).uuidValue(),
      row.findCell(2).stringValue(),
      OfferStatus.valueOf(row.findCell(3).stringValue()),
      BundleType.valueOf(row.findCell(4).stringValue()),
      BundleRuntime.valueOf(row.findCell(5).stringValue()),
      row.findCell(6).doubleValue(),
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

  public static Offer of(
    UUID id, UUID partnerId, String priceId, OfferStatus offerStatus,
    BundlePreset preset, BundleRuntime runtime, double price
  ) {
    return create(id, partnerId, priceId, offerStatus, preset.bundleType(),
      runtime, price, preset.collectionCardAccess(), preset.valueCardAccess(),
      preset.memberCardAccess(), preset.analysisAccess(), preset.actionAccess(),
      preset.notificationAccess(), preset.portalAccess(),
      preset.multiPortalUserAccess(), preset.portalPermissionAccess());
  }

  private final UUID id;
  private final UUID partnerId;
  private final String priceId;
  private OfferStatus offerStatus;
  private final BundleType bundleType;
  private final BundleRuntime bundleRuntime;
  private final double price;
  private final boolean collectionCardAccess;
  private final boolean valueCardAccess;
  private final boolean memberCardAccess;
  private final boolean analysisAccess;
  private final boolean actionAccess;
  private final boolean notificationAccess;
  private final boolean portalAccess;
  private final boolean multiPortalUserAccess;
  private final boolean portalPermissionAccess;

  public void updateStatus(OfferStatus newStatus) {
    offerStatus = newStatus;
  }

  public Bundle toBundle() {
    return Bundle.create(partnerId, bundleType, bundleRuntime, price,
      calculateBundleExpiration(bundleRuntime), collectionCardAccess,
      valueCardAccess, memberCardAccess, analysisAccess, actionAccess,
      notificationAccess, portalAccess, multiPortalUserAccess, portalPermissionAccess);
  }

  private long calculateBundleExpiration(BundleRuntime runtime) {
    if (runtime.isUnbound()) {
      return 0;
    }
    var current = ZonedDateTime.now();
    var next = current.plusMonths(runtime.isMonthly() ? 1 : 12);
    if (next.getDayOfMonth() != current.getDayOfMonth()) {
      next = next.withDayOfMonth(next.getMonth().length(
        next.toLocalDate().isLeapYear()));
    }
    return next.toInstant().toEpochMilli();
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
