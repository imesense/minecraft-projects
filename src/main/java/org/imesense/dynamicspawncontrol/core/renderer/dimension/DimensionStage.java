package org.imesense.dynamicspawncontrol.core.renderer.dimension;

import net.minecraft.world.World;

public abstract class DimensionStage
{
    public static boolean isEndDimension(World world)
    {
        return world.provider.getDimensionType().getId() == 1;
    }

    public static float overrideEndRed(float skyFactor2, float blockBase)
    {
        return (skyFactor2 * 0.22f) + (blockBase * 0.75f);
    }

    public static float overrideEndGreen(float skyFactor2, float blockGreen)
    {
        return (skyFactor2 * 0.28f) + (blockGreen * 0.75f);
    }

    public static float overrideEndBlue(float skyFactor2, float blockBlue)
    {
        return (skyFactor2 * 0.25f) + (blockBlue * 0.75f);
    }
}
