package org.imesense.dynamicspawncontrol.core.renderer.night.color;

import net.minecraft.util.math.MathHelper;

public abstract class ColorCombineStage
{
    public static float combineChannel(float sky, float block)
    {
        return sky + block;
    }

    public static float computeMaxLightFactor(float skyFactor2, float blockFactor)
    {
        return Math.max(skyFactor2, blockFactor);
    }

    public static float applySoftFloor(float value, float fa)
    {
        float min = 0.03f * fa;
        return (value * (0.99f - min)) + min;
    }

    public static float applyFinalSoftFloor(float value, float fa)
    {
        float min = 0.03f * fa;
        float result = (value * (0.99f - min)) + min;

        return MathHelper.clamp(result, 0.0f, 1.0f);
    }
}
