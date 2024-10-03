package org.imesense.dynamicspawncontrol.technical.asm;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import org.imesense.dynamicspawncontrol.technical.config.rendernight.DataRenderNight;

/**
 *
 */
public final class WorldProviderHooks
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
        if (!DataRenderNight.ConfigDataRenderNight.instance.getDarknessEndFog() && provider instanceof WorldProviderEnd)
        {
            return null;
        }

        if (!DataRenderNight.ConfigDataRenderNight.instance.getDarknessNetherFog() && provider instanceof WorldProviderHell)
        {
            return null;
        }

        return Vec3d.ZERO;
    }
}
