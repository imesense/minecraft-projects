package org.imesense.dynamicspawncontrol.core.renderer.math;

public abstract class FastMath
{
    public static float pow4(float v)
    {
        float sq = v * v;
        return sq * sq;
    }

    public static float calculateLinear(float factor, float start, float end)
    {
        return start + (factor * (end - start));
    }
}
