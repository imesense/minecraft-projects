package org.imesense.dynamicspawncontrol.technical.asm;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import org.imesense.dynamicspawncontrol.debug.CheckDebugger;
import org.imesense.dynamicspawncontrol.technical.config.rendernight.DataRenderNight;

import java.lang.reflect.Field;

/**
 *
 */
public final class EntityRendererHooks
{
    /**
     *
     */
    static Field mcField;

    /**
     *
     */
    static Field gameSettingsField;

    /**
     *
     */
    static Field gammaSettingField;

    /**
     *
     */
    static Field torchFlickerXField;

    /**
     *
     */
    static Field lightmapColorsField;

    /**
     *
     */
    static Field bossColorModifierField;

    /**
     *
     */
    static Field lightmapUpdateNeededField;

    /**
     *
     */
    static Field bossColorModifierPrevField;

    /**
     *
     * @param renderer
     * @param partialTicks
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static void onUpdateLightmap(EntityRenderer renderer, float partialTicks) throws NoSuchFieldException, IllegalAccessException
    {
        Class<?> rendererClass = renderer.getClass();

        boolean lightmapUpdateNeededValue;
        {
            lightmapUpdateNeededField = rendererClass.getDeclaredField(CheckDebugger.instance.IsRunDebugger ? "lightmapUpdateNeeded" : "field_78536_aa");
            lightmapUpdateNeededField.setAccessible(true);
            lightmapUpdateNeededValue = lightmapUpdateNeededField.getBoolean(renderer);
        }

        if (!lightmapUpdateNeededValue)
        {
            return;
        }

        Minecraft mc;
        {
            mcField = rendererClass.getDeclaredField(CheckDebugger.instance.IsRunDebugger ? "mc" : "field_78531_r");
            mcField.setAccessible(true);
            mc = (Minecraft) mcField.get(renderer);
        }

        if (mc == null)
        {
            return;
        }

        World world = mc.world;

        if (mc.player.isPotionActive(MobEffects.NIGHT_VISION))
        {
            return;
        }

        if (world.getLastLightningBolt() > 0)
        {
            return;
        }

        if (blacklistDim(world.provider))
        {
            return;
        }

        updateLuminance(renderer, partialTicks, world);
    }

    /**
     *
     * @param dimension
     * @return
     */
    private static boolean blacklistDim(WorldProvider dimension)
    {
        DimensionType dimType = dimension.getDimensionType();

        if (dimType == DimensionType.THE_END && !DataRenderNight.renderNight.instance.getDarknessEnd())
        {
            return true;
        }

        return blacklistContains(dimension, dimType) ^ DataRenderNight.renderNight.instance.getInvertBlacklist();
    }

    /**
     *
     * @param dimension
     * @param dimensionType
     * @return
     */
    private static boolean blacklistContains(WorldProvider dimension, DimensionType dimensionType)
    {
        String dimName = dimensionType.getName();

        for (String blacklistName : DataRenderNight.renderNight.instance.getBlacklistByName())
        {
            if (!blacklistName.equals(dimName))
            {
                continue;
            }

            return true;
        }

        int dimID = dimension.getDimension();

        for (int blacklistID : DataRenderNight.renderNight.instance.getBlacklistByID())
        {
            if (dimID != blacklistID)
            {
                continue;
            }

            return true;
        }

        return false;
    }

    /**
     *
     * @param dimension
     * @param dimensionType
     * @return
     */
    private static boolean isDark(WorldProvider dimension, DimensionType dimensionType)
    {
        if (dimensionType == DimensionType.OVERWORLD)
        {
            return DataRenderNight.renderNight.instance.getDarknessOverWorld();
        }
        else if (dimensionType == DimensionType.NETHER)
        {
            return DataRenderNight.renderNight.instance.getDarknessNether();
        }
        else if (dimensionType == DimensionType.THE_END)
        {
            return DataRenderNight.renderNight.instance.getDarknessEnd();
        }
        else if (dimension.hasSkyLight())
        {
            return DataRenderNight.renderNight.instance.getDarknessDefault();
        }
        else
        {
            return DataRenderNight.renderNight.instance.getDarknessSkyLess();
        }
    }

    /**
     *
     * @param partialTicks
     * @param world
     * @return
     */
    private static float getMoonBrightness(float partialTicks, World world)
    {
        WorldProvider dim = world.provider;
        DimensionType dimType = dim.getDimensionType();

        if (!isDark(dim, dimType))
        {
            return 1.f;
        }

        if (!dim.hasSkyLight())
        {
            return 0.f;
        }

        float angle = world.getCelestialAngle(partialTicks);

        if (angle <= 0.25f || 0.75f <= angle)
        {
            return 1.f;
        }

        final double moon;

        if (!DataRenderNight.renderNight.instance.getIgnoreMoonLight())
        {
            Double[] phaseFactors = DataRenderNight.renderNight.instance.getMoonPhaseFactors();

            int moonPhase = dim.getMoonPhase(world.getWorldTime());

            if (moonPhase < phaseFactors.length)
            {
                moon = phaseFactors[moonPhase];
            }
            else
            {
                moon = world.getCurrentMoonPhaseFactor();
            }
        }
        else
        {
            moon = 0.f;
        }

        float w;

        if (angle <= 0.3f || 0.7f <= angle)
        {
            w = 20.f * (Math.abs(angle - 0.5f) - 0.2f);
        }
        else
        {
            w = 0.f;
        }

        return linear(w * w, (float) moon, 1.f);
    }

    /**
     *
     * @param renderer
     * @param partialTicks
     * @param world
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static void updateLuminance(EntityRenderer renderer, float partialTicks, World world) throws NoSuchFieldException, IllegalAccessException
    {
        WorldProvider dim = world.provider;
        DimensionType dimType = dim.getDimensionType();

        // Light to brightness float[16] conversion table.
        float[] brightnessTable = dim.getLightBrightnessTable();

        boolean dimDark = isDark(dim, dimType);

        float sunBrightness = world.getSunBrightness(1.0f);
        float moonBrightness = getMoonBrightness(partialTicks, world);

        for (int i = 0; i < 256; ++i)
        {
            int skyIndex = i / 16;
            int blockIndex = i % 16;

            float skyFactor = 1.f - skyIndex / 15.f;
            skyFactor = 1.f - skyFactor * skyFactor * skyFactor * skyFactor;
            skyFactor *= moonBrightness;

            float min = skyFactor * 0.05f;

            float rawAmbient = sunBrightness * skyFactor;
            float minAmbient = rawAmbient * (1 - min) + min;
            float skyBase = brightnessTable[skyIndex] * minAmbient;

            min = 0.35f * skyFactor;

            float skyRed = skyBase * (rawAmbient * (1.f - min) + min);
            float skyGreen = skyBase * (rawAmbient * (1.f - min) + min);
            float skyBlue = skyBase;

            float bossColorModifier;
            {
                bossColorModifierField =
                        renderer.getClass().getDeclaredField(CheckDebugger.instance.IsRunDebugger ? "bossColorModifier" : "field_82831_U");
                bossColorModifierField.setAccessible(true);
                bossColorModifier = bossColorModifierField.getFloat(renderer);
            }

            float bossColorModifierPrev;
            {
                bossColorModifierPrevField =
                        renderer.getClass().getDeclaredField(CheckDebugger.instance.IsRunDebugger ? "bossColorModifierPrev" : "field_82832_V");
                bossColorModifierPrevField.setAccessible(true);
                bossColorModifierPrev = bossColorModifierPrevField.getFloat(renderer);
            }

            float torchFlickerX;
            {
                torchFlickerXField =
                        renderer.getClass().getDeclaredField(CheckDebugger.instance.IsRunDebugger ? "torchFlickerX" : "field_78514_e");
                torchFlickerXField.setAccessible(true);
                torchFlickerX = torchFlickerXField.getFloat(renderer);
            }

            Object mcObject;
            {
                mcField =
                        renderer.getClass().getDeclaredField(CheckDebugger.instance.IsRunDebugger ? "mc" : "field_78531_r");
                mcField.setAccessible(true);
                mcObject = mcField.get(renderer);
            }

            Object gameSettingsObject;
            {
                gameSettingsField =
                        mcObject.getClass().getDeclaredField(CheckDebugger.instance.IsRunDebugger ? "gameSettings" : "field_71474_y");
                gameSettingsField.setAccessible(true);
                gameSettingsObject = gameSettingsField.get(mcObject);
            }

            float gammaSetting;
            {
                gammaSettingField =
                        gameSettingsObject.getClass().getDeclaredField(CheckDebugger.instance.IsRunDebugger ? "gammaSetting" : "field_74333_Y");
                gammaSettingField.setAccessible(true);
                gammaSetting = gammaSettingField.getFloat(gameSettingsObject);
            }

            int[] lightmapColors;
            {
                lightmapColorsField = renderer.getClass().getDeclaredField(CheckDebugger.instance.IsRunDebugger ? "lightmapColors" : "field_78504_Q");
                lightmapColorsField.setAccessible(true);
                lightmapColors = (int[]) lightmapColorsField.get(renderer);
            }

            if (bossColorModifier > 0.f)
            {
                float d = bossColorModifier - bossColorModifierPrev;
                float m = bossColorModifierPrev + partialTicks * d;
                skyRed = skyRed * (1.f - m) + skyRed * 0.7f * m;
                skyGreen = skyGreen * (1.f - m) + skyGreen * 0.6F * m;
                skyBlue = skyBlue * (1.f - m) + skyBlue * 0.6f * m;
            }

            float blockFactor = 1.f;

            if (dimDark)
            {
                blockFactor = 1.f - blockIndex / 15.f;
                blockFactor = 1.f - blockFactor * blockFactor * blockFactor * blockFactor;
            }

            float flicker = torchFlickerX * 0.1f + 1.5f;
            float blockBase = blockFactor * brightnessTable[blockIndex] * flicker;
            min = 0.4f * blockFactor;

            float blockGreen = blockBase * ((blockBase * (1.f - min) + min) * (1.f - min) + min);
            float blockBlue = blockBase * (blockBase * blockBase * (1.f - min) + min);

            float red = skyRed + blockBase;
            float green = skyGreen + blockGreen;
            float blue = skyBlue + blockBlue;

            float f = Math.max(skyFactor, blockFactor);
            min = 0.03f * f;
            red = red * (0.99f - min) + min;
            green = green * (0.99f - min) + min;
            blue = blue * (0.99f - min) + min;

            if (dimType == DimensionType.THE_END)
            {
                red = skyFactor * 0.22f + blockBase * 0.75f;
                green = skyFactor * 0.28f + blockGreen * 0.75f;
                blue = skyFactor * 0.25f + blockBlue * 0.75f;
            }

            red = MathHelper.clamp(red, 0.f, 1.f);
            green = MathHelper.clamp(green, 0.f, 1.f);
            blue = MathHelper.clamp(blue, 0.f, 1.f);

            float gamma = gammaSetting * f;
            float invRed = 1.0f - red;
            float invGreen = 1.0f - green;
            float invBlue = 1.0f - blue;
            invRed = 1.0f - invRed * invRed * invRed * invRed;
            invGreen = 1.0f - invGreen * invGreen * invGreen * invGreen;
            invBlue = 1.0f - invBlue * invBlue * invBlue * invBlue;
            red = red * (1.0f - gamma) + invRed * gamma;
            green = green * (1.0f - gamma) + invGreen * gamma;
            blue = blue * (1.0f - gamma) + invBlue * gamma;

            min = 0.03f * f;
            red = red * (0.99f - min) + min;
            green = green * (0.99f - min) + min;
            blue = blue * (0.99f - min) + min;

            red = MathHelper.clamp(red, 0.f, 1.f);
            green = MathHelper.clamp(green, 0.f, 1.f);
            blue = MathHelper.clamp(blue, 0.f, 1.f);

            float lTarget = luminance(red, green, blue);
            int c = lightmapColors[i];
            lightmapColors[i] = darken(c, lTarget);
        }
    }

    /**
     *
     * @param color
     * @param lTarget
     * @return
     */
    private static int darken(int color, float lTarget)
    {
        float r = (color & 0xFF) / 255.f;
        float g = ((color >> 8) & 0xFF) / 255.f;
        float b = ((color >> 16) & 0xFF) / 255.f;
        float l = luminance(r, g, b);

        if (l <= 0.f)
        {
            return color;
        }

        if (lTarget >= l)
        {
            return color;
        }

        float f = lTarget / l;

        color = 0xFF000000;
        color |= Math.round(f * r * 255);
        color |= Math.round(f * g * 255) << 8;
        color |= Math.round(f * b * 255) << 16;

        return color;
    }

    /**
     *
     * @param red
     * @param green
     * @param blue
     * @return
     */
    private static float luminance(float red, float green, float blue)
    {
        return red * 0.2126f + green * 0.7152f + blue * 0.0722f;
    }

    /**
     *
     * @param f
     * @param g
     * @param h
     * @return
     */
    private static float linear(float f, float g, float h)
    {
        return g + f * (h - g);
    }
}
