package org.imesense.dynamicspawncontrol.core.plugin.mod.darkness_forge_1_12_x_0_5_0;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import org.imesense.dynamicspawncontrol.core.pluginconfig.darkness.PluginDarknessConfig;

public final class WorldProviderHook
{
    public WorldProviderHook()
    {

    }

    public static Vec3d onGetFogColor(WorldProvider worldProvider, float angle, float partialTicks)
    {
        if (!PluginDarknessConfig.getInstance(PluginDarknessConfig.class).isDarknessEndFog() && worldProvider instanceof WorldProviderEnd)
        {
            return null;
        }

        if (!PluginDarknessConfig.getInstance(PluginDarknessConfig.class).isDarknessNetherFog() && worldProvider instanceof WorldProviderHell)
        {
            return null;
        }

        return Vec3d.ZERO;
    }
}
