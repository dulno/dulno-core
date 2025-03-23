package com.dulno.core.module;

import com.dulno.core.locale.Locales;
import com.dulno.core.log.Log;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class ModuleInjectionModule extends AbstractModule {
  @Provides
  @Singleton
  ModuleLoader provideModuleLoader(Log log, Locales locales, Injector injector) {
    var directory = System.getProperty("user.dir") + "/modules/";
    return ModuleLoader.create(log, directory, locales, injector);
  }
}
