package org.imesense.dynamicspawncontrol.core.renderer.night.color;

public abstract class LuminanceStage
{
    public static float calculateLuminance(float red, float green, float blue)
    {
        return (red * 0.2126f) + (green * 0.7152f) + (blue * 0.0722f);
    }
}
