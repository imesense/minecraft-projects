package org.imesense.dynamicspawncontrol.core.renderer.sky;

import static org.imesense.dynamicspawncontrol.core.renderer.math.FastMath.pow4;

public abstract class SkyLightStage
{
    public static float computeSkyFactor(int skyIndex)
    {
        return 1.0f - (skyIndex / 15.0f);
    }

    public static float computeSkyFactor2(float skyFactor, float moonBrightness)
    {
        return (1.0f - pow4(skyFactor)) * moonBrightness;
    }

    public static float computeRawAmbient(float sunBrightness, float skyFactor2)
    {
        return sunBrightness * skyFactor2;
    }

    public static float computeMinAmbient(float rawAmbient, float skyFactor2)
    {
        float min = skyFactor2 * 0.05f;
        return (rawAmbient * (1.0f - min)) + min;
    }

    public static float computeSkyBase(float[] brightnessTable, int skyIndex, float minAmbient)
    {
        return brightnessTable[skyIndex] * minAmbient;
    }

    public static float computeSkyColorModifier(float rawAmbient, float skyFactor2)
    {
        float min2 = 0.35f * skyFactor2;
        return (rawAmbient * (1.0f - min2)) + min2;
    }
}
