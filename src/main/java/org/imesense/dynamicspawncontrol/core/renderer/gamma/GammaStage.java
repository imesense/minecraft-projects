package org.imesense.dynamicspawncontrol.core.renderer.gamma;

import net.minecraft.client.Minecraft;

import static org.imesense.dynamicspawncontrol.core.renderer.math.FastMath.pow4;

public abstract class GammaStage
{
    public static float computeGamma(float fa)
    {
        return Minecraft.getMinecraft().gameSettings.gammaSetting * fa;
    }

    public static float gammaCorrectChannel(float value, float gamma)
    {
        float inv = 1.0f - value;
        float invPow4 = 1.0f - pow4(inv);
        return (value * (1.0f - gamma)) + (invPow4 * gamma);
    }
}
