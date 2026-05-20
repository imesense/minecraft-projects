package org.imesense.dynamicspawncontrol.core.renderer.night.color;

import net.minecraft.util.math.MathHelper;

public abstract class ColorCombineBloodMoon
{
    public static int blendRGB(int c1, int c2, float t)
    {
        t = MathHelper.clamp(t, 0.0f, 1.0f);

        int a1 = (c1 >> 24) & 255;
        int r1 = (c1 >> 16) & 255;
        int g1 = (c1 >> 8) & 255;
        int b1 = c1 & 255;

        int a2 = (c2 >> 24) & 255;
        int r2 = (c2 >> 16) & 255;
        int g2 = (c2 >> 8) & 255;
        int b2 = c2 & 255;

        int a = (int)(a1 + (a2 - a1) * t);
        int r = (int)(r1 + (r2 - r1) * t);
        int g = (int)(g1 + (g2 - g1) * t);
        int b = (int)(b1 + (b2 - b1) * t);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
