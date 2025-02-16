package org.imesense.dynamicspawncontrol.plugin.fogworld_1_12_1_1_0_b15_universal.util;

import net.minecraft.world.biome.Biome;
import org.imesense.dynamicspawncontrol.plugin.fogworld_1_12_1_1_0_b15_universal.api.interfaces.IBiomeFog;

import java.util.List;

public class BiomeUtil {
    public static String getBiomeName(Biome biome) {
        if (biome != null && biome.getBiomeName() != null) {
            return biome.getBiomeName();
        }
        return "";
    }

    public static String getBiomeName(int biomeID) {
        return getBiomeName(Biome.getBiomeForId(biomeID));
    }

    public static boolean isBiomeBlacklisted(IBiomeFog biome) {
        List<String> biomeBlacklist = FogWorldConfig.getFogBiomeBlacklist();
        return biomeBlacklist.contains(String.valueOf(Biome.getIdForBiome((Biome) biome))) || biomeBlacklist.contains(getBiomeName((Biome) biome)) || biomeBlacklist.contains(((Biome) biome).getBiomeName());
    }
}
