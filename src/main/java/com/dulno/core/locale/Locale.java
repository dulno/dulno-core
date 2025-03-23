package com.dulno.core.locale;

import com.dulno.core.configuration.Configuration;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.json.JSONObject;

import java.util.Map;

@Accessors(fluent = true)
public final class Locale extends Configuration {
  private static final String LOCALE_PATH = "/locale/%s/%s.json";

  public static Locale createAndLoad(String module, String language) throws Exception {
    var configuration = create(module, language);
    configuration.load();
    return configuration;
  }

  public static Locale create(String module, String language) {
    return new Locale(String.format(LOCALE_PATH, module, language),
      module, language);
  }

  @Getter
  private final String module;
  @Getter
  private final String language;
  private final Map<String, String> locale = Maps.newHashMap();

  private Locale(String path, String module, String language) {
    super(path);
    this.module = module;
    this.language = language;
  }

  @Override
  protected void deserialize(JSONObject json) {
    for (var key : json.keySet()) {
      locale.put(key, json.getString(key));
    }
  }

  public void addLocale(Locale newLocale) {
    locale.putAll(newLocale.locale);
  }

  public void removeLocale(Locale otherLocale) {
    locale.keySet().removeAll(otherLocale.locale.keySet());
  }

  public String findText(String key) {
    if (!locale.containsKey(key)) {
      return key;
    }
    return locale.get(key);
  }
}