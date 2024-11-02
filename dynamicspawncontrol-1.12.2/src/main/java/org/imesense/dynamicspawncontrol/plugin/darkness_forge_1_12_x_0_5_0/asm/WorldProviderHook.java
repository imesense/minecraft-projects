package org.imesense.dynamicspawncontrol.plugin.darkness_forge_1_12_x_0_5_0.asm;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import org.imesense.dynamicspawncontrol.plugin.darkness_forge_1_12_x_0_5_0.config.DataDarkness;

/**
 *
 */
public final class WorldProviderHook
{
    /**
     *
     * @param provider
     * @param angle
     * @param partialTicks
     * @return
     */
    public static Vec3d onGetFogColor(WorldProvider provider, float angle, float partialTicks)
    {
        if (!DataDarkness.ConfigDataRenderNight.instance.getDarknessEndFog() && provider instanceof WorldProviderEnd)
        {
            return null;
        }

        if (!DataDarkness.ConfigDataRenderNight.instance.getDarknessNetherFog() && provider instanceof WorldProviderHell)
        {
            return null;
        }

        return Vec3d.ZERO;
    }
}
