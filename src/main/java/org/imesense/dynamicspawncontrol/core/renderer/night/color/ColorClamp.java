package org.imesense.dynamicspawncontrol.core.renderer.night.color;

import net.minecraft.util.math.MathHelper;

public abstract class ColorClamp
{
    public static float clamp01(float value)
    {
        return MathHelper.clamp(value, 0.0f, 1.0f);
    }
}
