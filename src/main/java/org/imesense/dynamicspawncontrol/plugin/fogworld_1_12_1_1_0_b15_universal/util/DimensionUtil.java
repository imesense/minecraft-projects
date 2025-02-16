package org.imesense.dynamicspawncontrol.plugin.fogworld_1_12_1_1_0_b15_universal.util;

public class DimensionUtil {
    public static String getDimensionName(DimensionType dimension) {
        if (dimension != null && dimension.func_186065_b() != null) {
            return dimension.func_186065_b();
        }
        return "";
    }

    public static String getDimensionName(int dimensionID) {
        return getDimensionName(DimensionManager.getProviderType(dimensionID));
    }

    public static boolean isDimensionBlacklisted(DimensionType dimension) {
        List<String> dimensionBlacklist = FogWorldConfig.getFogDimensionBlacklist();
        return dimensionBlacklist.contains(String.valueOf(dimension.func_186068_a())) || dimensionBlacklist.contains(getDimensionName(dimension)) || dimensionBlacklist.contains(dimension.func_186065_b());
    }
}
