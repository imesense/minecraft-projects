package org.imesense.dynamicspawncontrol.core.renderer.color;

import static org.imesense.dynamicspawncontrol.core.renderer.color.LuminanceStage.calculateLuminance;

public abstract class ColorCombineDark
{
    private static float[] extractRGBComponents(int color)
    {
        float red = (color & 255) / 255.0f;
        float green = ((color >> 8) & 255) / 255.0f;
        float blue = ((color >> 16) & 255) / 255.0f;

        return new float[] { red, green, blue };
    }

    private static float calculateCurrentLuminance(float red, float green, float blue)
    {
        return calculateLuminance(red, green, blue);
    }

    private static boolean shouldSkipColorCorrection(float currentLuminance, float targetLuminance)
    {
        return currentLuminance <= 0.0f || targetLuminance >= currentLuminance;
    }

    private static float calculateCorrectionFactor(float targetLuminance, float currentLuminance)
    {
        return targetLuminance / currentLuminance;
    }

    private static int[] applyCorrectionToComponents(float red, float green, float blue, float factor)
    {
        int correctedRed = Math.round(factor * red * 255.0f);
        int correctedGreen = Math.round(factor * green * 255.0f);
        int correctedBlue = Math.round(factor * blue * 255.0f);

        return new int[] { correctedRed, correctedGreen, correctedBlue };
    }

    private static int packComponentsIntoColor(int[] components)
    {
        int alpha = -16777216;

        int red = components[0];
        int green = components[1];
        int blue = components[2];

        return alpha | red | (green << 8) | (blue << 16);
    }

    private static int packColorWithAlpha(int color, float targetLuminance)
    {
        int alpha = (color >> 24) & 0xFF;
        int rgb = finalPackDarkColor(color & 0xFFFFFF, targetLuminance);
        return (alpha << 24) | (rgb & 0xFFFFFF);
    }

    public static int finalPackDarkColor(int color, float targetLuminance)
    {
        float[] rgbComponents = extractRGBComponents(color);
        float red = rgbComponents[0];
        float green = rgbComponents[1];
        float blue = rgbComponents[2];

        float currentLuminance = calculateCurrentLuminance(red, green, blue);

        if (shouldSkipColorCorrection(currentLuminance, targetLuminance))
        {
            return color;
        }

        float correctionFactor = calculateCorrectionFactor(targetLuminance, currentLuminance);

        int[] correctedComponents = applyCorrectionToComponents(red, green, blue, correctionFactor);

        return packComponentsIntoColor(correctedComponents);
    }
}
