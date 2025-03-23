package com.dulno.core.module;

import com.dulno.core.event.Hook;
import com.dulno.core.event.HookRegistry;
import com.google.inject.Injector;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter(AccessLevel.PROTECTED)
@Accessors(fluent = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Module {
  private final Injector injector;

  /**
   * Is called before the enable function is called
   * @throws Exception
   */
  public void preEnable() throws Exception {
  }

  /**
   * Is called up when the module is to be loaded.
   * Used to initialize the module.
   * @throws Exception
   */
  public abstract void enable() throws Exception;

  /**
   * Is called after the enable function is called
   * @throws Exception
   */
  public void postEnable() throws Exception {
  }

  /**
   * Is called before the disable function is called
   * @throws Exception
   */
  public void preDisable() throws Exception {
  }

  /**
   * Is called up when a module is to be unloaded.
   * It is intended to reset the status of the module and release used resources
   * @throws Exception
   */
  public abstract void disable() throws Exception;

  /**
   * Is called after the disable function is called
   * @throws Exception
   */
  public void postDisable() throws Exception {
  }

  /**
   * Is used to register a new hook
   * @param hook The hook that is to be registered
   */
  public void registerHook(Hook hook) {
    injector.getInstance(HookRegistry.class).register(hook);
  }

  /**
   * Can be called to unregister a hook
   * @param hook The hook that is to be unregistered
   */
  public void unregisterHook(Hook hook) {
    injector.getInstance(HookRegistry.class).unregister(hook);
  }
}
