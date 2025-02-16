package org.imesense.dynamicspawncontrol.plugin.fogworld_1_12_1_1_0_b15_universal.util;

import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

import java.util.List;

public class DimensionUtil {
    public static String getDimensionName(DimensionType dimension) {
        if (dimension != null && dimension.getName() != null) {
            return dimension.getName();
        }
        return "";
    }

    public static String getDimensionName(int dimensionID) {
        return getDimensionName(DimensionManager.getProviderType(dimensionID));
    }

    public static boolean isDimensionBlacklisted(DimensionType dimension) {
        List<String> dimensionBlacklist = FogWorldConfig.getFogDimensionBlacklist();
        return dimensionBlacklist.contains(String.valueOf(dimension.getId())) || dimensionBlacklist.contains(getDimensionName(dimension)) || dimensionBlacklist.contains(dimension.getName());
    }
}
