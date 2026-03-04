package org.imesense.dynamicspawncontrol.core.renderer.night.color;

import static org.imesense.dynamicspawncontrol.core.renderer.night.color.LuminanceStage.calculateLuminance;

public abstract class ColorCombineDark
{
    private static int clampToByte(int v)
    {
        if (v < 0)
            return 0;

        return Math.min(v, 255);
    }

    public static int finalPackDarkColor(int color, float targetLuminance)
    {
        float r = (color & 0xFF) * (1.0f / 255.0f);
        float g = ((color >>> 8) & 0xFF) * (1.0f / 255.0f);
        float b = ((color >>> 16) & 0xFF) * (1.0f / 255.0f);

        float currentLuminance = calculateLuminance(r, g, b);

        if (currentLuminance <= 0.0f || targetLuminance >= currentLuminance)
        {
            return color;
        }

        float factor = targetLuminance / currentLuminance;

        int rr = clampToByte((int)(factor * r * 255.0f + 0.5f));
        int gg = clampToByte((int)(factor * g * 255.0f + 0.5f));
        int bb = clampToByte((int)(factor * b * 255.0f + 0.5f));

        return 0xFF000000 | rr | (gg << 8) | (bb << 16);
    }
}
