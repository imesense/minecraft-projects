package org.imesense.dynamicspawncontrol.plugin.fogworld_1_12_1_1_0_b15_universal.util;

public class BiomeUtil {
    public static String getBiomeName(Biome biome) {
        if (biome != null && biome.func_185359_l() != null) {
            return biome.func_185359_l();
        }
        return "";
    }

    public static String getBiomeName(int biomeID) {
        return getBiomeName(Biome.func_185357_a(biomeID));
    }

    public static boolean isBiomeBlacklisted(Biome biome) {
        List<String> biomeBlacklist = FogWorldConfig.getFogBiomeBlacklist();
        return biomeBlacklist.contains(String.valueOf(Biome.func_185362_a(biome))) || biomeBlacklist.contains(getBiomeName(biome)) || biomeBlacklist.contains(biome.func_185359_l());
    }
}
