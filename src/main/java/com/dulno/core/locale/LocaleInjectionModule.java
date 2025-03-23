package com.dulno.core.locale;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class LocaleInjectionModule extends AbstractModule {
  @Provides
  @Singleton
  Locales provideLocales() throws Exception {
    return Locales.create();
  }
}
