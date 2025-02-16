package org.imesense.dynamicspawncontrol.plugin.fogworld_1_12_1_1_0_b15_universal.api.interfaces;

public interface IDimensionFog {
    float getFogDensity(int i, int i2, int i3);

    int getFogColor(int i, int i2, int i3);

    default boolean getFogEnabled() {
        return true;
    }
}
