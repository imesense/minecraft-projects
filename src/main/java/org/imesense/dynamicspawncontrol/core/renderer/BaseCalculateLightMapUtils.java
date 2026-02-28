package org.imesense.dynamicspawncontrol.core.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.core.mixinconfig.nightrenderer.NightRendererData;
import org.imesense.dynamicspawncontrol.mixins.interfaces.IEntityRendererAccessor;

import static org.imesense.dynamicspawncontrol.core.renderer.color.LuminanceStage.calculateLuminance;

public abstract class BaseCalculateLightMapUtils
{
    public static float calculateLinear(float factor, float start, float end)
    {
        return start + (factor * (end - start));
    }

    private static final class PACK_DARK_COLOR
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
    }

    public static int packColor(int color, float targetLuminance)
    {
        float[] rgbComponents = PACK_DARK_COLOR.extractRGBComponents(color);
        float red = rgbComponents[0];
        float green = rgbComponents[1];
        float blue = rgbComponents[2];

        float currentLuminance = PACK_DARK_COLOR.calculateCurrentLuminance(red, green, blue);

        if (PACK_DARK_COLOR.shouldSkipColorCorrection(currentLuminance, targetLuminance))
        {
            return color;
        }

        float correctionFactor = PACK_DARK_COLOR.calculateCorrectionFactor(targetLuminance, currentLuminance);

        int[] correctedComponents = PACK_DARK_COLOR.applyCorrectionToComponents(red, green, blue, correctionFactor);

        return PACK_DARK_COLOR.packComponentsIntoColor(correctedComponents);
    }

    private static final class CALCULATED_MOON_LIGHT
    {
        private static boolean hasSkyLight(World world)
        {
            return world.provider.hasSkyLight();
        }

        private static float getCelestialAngle(World world, float partialTicks)
        {
            return world.getCelestialAngle(partialTicks);
        }

        private static boolean isFullMoonVisible(float celestialAngle)
        {
            return celestialAngle <= 0.25f || celestialAngle >= 0.75f;
        }

        private static float getBaseMoonBrightness(World world)
        {
            if (!NightRendererData.isDependenceLightMoonPhase())
            {
                return 0.0f;
            }

            int moonPhase = getCurrentMoonPhase(world);
            return getMoonPhaseBrightness(moonPhase);
        }

        private static int getCurrentMoonPhase(World world)
        {
            return world.provider.getMoonPhase(world.getWorldTime());
        }

        private static float getMoonPhaseBrightness(int moonPhase)
        {
            return NightRendererData.getMoonPhaseFactorsArray()[moonPhase];
        }

        private static float calculateTransitionFactor(float celestialAngle)
        {
            if (isInTransitionZone(celestialAngle))
            {
                return calculateRawTransitionFactor(celestialAngle);
            }

            return 0.0f;
        }

        private static boolean isInTransitionZone(float celestialAngle)
        {
            return celestialAngle <= 0.3f || celestialAngle >= 0.7f;
        }

        private static float calculateRawTransitionFactor(float celestialAngle)
        {
            return 20.0f * (Math.abs(celestialAngle - 0.5f) - 0.2f);
        }

        private static float calculateFinalMoonBrightness(float transitionFactor, float moonBrightness)
        {
            float squaredTransitionFactor = transitionFactor * transitionFactor;
            return BaseCalculateLightMapUtils.calculateLinear(squaredTransitionFactor, moonBrightness, 1.0f);
        }
    }

    public static float getMoonBrightness(float partialTicks, World world)
    {
        if (!CALCULATED_MOON_LIGHT.hasSkyLight(world))
        {
            return 0.0f;
        }

        float celestialAngle = CALCULATED_MOON_LIGHT.getCelestialAngle(world, partialTicks);

        if (CALCULATED_MOON_LIGHT.isFullMoonVisible(celestialAngle))
        {
            return 1.0f;
        }

        float moonBrightness = CALCULATED_MOON_LIGHT.getBaseMoonBrightness(world);

        float transitionFactor = CALCULATED_MOON_LIGHT.calculateTransitionFactor(celestialAngle);

        return CALCULATED_MOON_LIGHT.calculateFinalMoonBrightness(transitionFactor, moonBrightness);
    }
}
