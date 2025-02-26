package org.imesense.dynamicspawncontrol.core.plugin.mod.darkness_forge_1_12_x_0_5_0;

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
     * @param worldProvider
     * @param angle
     * @param partialTicks
     * @return
     */
    public static Vec3d onGetFogColor(WorldProvider worldProvider, float angle, float partialTicks)
    {
        if (!DataDarkness.ConfigDataRenderNight.Instance.getDarknessEndFog() && worldProvider instanceof WorldProviderEnd)
        {
            return null;
        }

        if (!DataDarkness.ConfigDataRenderNight.Instance.getDarknessNetherFog() && worldProvider instanceof WorldProviderHell)
        {
            return null;
        }

        return Vec3d.ZERO;
    }
}
