package com.dulno.core.accessory;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor(staticName = "create")
public final class AccessoryPresetRepository {
  private final List<AccessoryPreset> presets = Lists.newArrayList();

  public void registerPreset(AccessoryPreset preset) {
    presets.add(preset);
  }

  public void unregisterPreset(AccessoryPreset preset) {
    presets.remove(preset);
  }

  public Optional<AccessoryPreset> findPreset(String id) {
    return presets.stream().filter(preset -> preset.id().equals(id))
      .findFirst();
  }

  public Optional<AccessoryPreset> findPresetByPriceId(String priceId) {
    return presets.stream().filter(preset -> preset.priceId().equals(priceId))
      .findFirst();
  }

  public List<AccessoryPreset> findAll() {
    return List.copyOf(presets);
  }
}
