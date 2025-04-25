package com.dulno.core.stripe;

import com.dulno.core.bundle.BundlePreset;
import com.dulno.core.bundle.BundleRuntime;
import com.dulno.core.bundle.BundleType;
import com.dulno.core.configuration.Configuration;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
@Accessors(fluent = true)
public final class StripeConfiguration extends Configuration {
  private static final String CONFIGURATION_PATH = "/configurations/stripe/stripe.json";

  public static StripeConfiguration createAndLoad() throws Exception {
    var configuration = new StripeConfiguration(CONFIGURATION_PATH);
    configuration.load();
    return configuration;
  }

  private String apiKey;
  private String checkoutWebhookSecret;
  private String paymentWebhookSecret;
  private Map<String, String> priceIds;

  private StripeConfiguration(String path) {
    super(path);
  }

  @Override
  protected void deserialize(JSONObject json) {
    apiKey = json.getString("apiKey");
    checkoutWebhookSecret = json.getString("checkoutWebhookSecret");
    paymentWebhookSecret = json.getString("paymentWebhookSecret");
    priceIds = Maps.newHashMap();
    registerPriceId(json, BundleType.BASIC, BundleRuntime.MONTHLY);
    registerPriceId(json, BundleType.BASIC, BundleRuntime.YEARLY);
    registerPriceId(json, BundleType.PROFESSIONAL, BundleRuntime.MONTHLY);
    registerPriceId(json, BundleType.PROFESSIONAL, BundleRuntime.YEARLY);
    registerPriceId(json, BundleType.PREMIUM, BundleRuntime.MONTHLY);
    registerPriceId(json, BundleType.PREMIUM, BundleRuntime.YEARLY);
  }

  private void registerPriceId(
    JSONObject json, BundleType bundleType, BundleRuntime bundleRuntime
  ) {
    var typeString = switch(bundleType) {
      case BASIC -> "Basic";
      case PROFESSIONAL -> "Professional";
      case PREMIUM -> "Premium";
      case ENTERPRISE -> "";
    };
    var runtimeString = switch(bundleRuntime) {
      case MONTHLY -> "Monthly";
      case YEARLY -> "Yearly";
      case UNBOUND -> "";
    };
    priceIds.put(bundleType.toString() + "-" + bundleRuntime.toString(),
      json.getString("price" + typeString + runtimeString + "Id"));
  }

  public String findPriceId(
    BundleType bundleType, BundleRuntime bundleRuntime
  ) {
    return priceIds.get(bundleType.toString() + "-" + bundleRuntime.toString());
  }

  public boolean priceIdExists(String priceId) {
    return priceIds.containsValue(priceId);
  }

  public List<String> findPriceIdsOfType(BundleType bundleType) {
    var priceIds = Lists.<String>newArrayList();
    priceIds.add(findPriceId(bundleType, BundleRuntime.MONTHLY));
    priceIds.add(findPriceId(bundleType, BundleRuntime.YEARLY));
    return priceIds;
  }

  public List<String> findPriceIdsOfRuntime(BundleRuntime bundleRuntime) {
    var priceIds = Lists.<String>newArrayList();
    priceIds.add(findPriceId(BundleType.BASIC, bundleRuntime));
    priceIds.add(findPriceId(BundleType.PROFESSIONAL, bundleRuntime));
    priceIds.add(findPriceId(BundleType.PREMIUM, bundleRuntime));
    return priceIds;
  }

  public Optional<BundlePreset> findBundlePreset(String priceId) throws Exception {
    if (findPriceIdsOfType(BundleType.BASIC).contains(priceId)) {
      return Optional.of(BundlePreset.createAndLoad(BundleType.BASIC));
    } else if (findPriceIdsOfType(BundleType.PROFESSIONAL).contains(priceId)) {
      return Optional.of(BundlePreset.createAndLoad(BundleType.PROFESSIONAL));
    } else if (findPriceIdsOfType(BundleType.PREMIUM).contains(priceId)) {
      return Optional.of(BundlePreset.createAndLoad(BundleType.PREMIUM));
    }
    return Optional.empty();
  }
}