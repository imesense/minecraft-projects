package org.imesense.dynamicspawncontrol.core.plugin.mod.darkness_forge_1_12_x_0_5_0;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.pluginconfig.darkness.PluginDarknessConfig;

import java.lang.reflect.Field;

@TODO(
        value = "Миксины больше не работают с подобным, убрать этот класс",
        showOnce = false,
        priority = TODO.TodoPriority.HIGH)
public final class EntityRendererHook
{
    static Field mcField;

    static Field gameSettingsField;

    static Field gammaSettingField;

    static Field torchFlickerXField;

    static Field lightmapColorsField;

    static Field bossColorModifierField;

    static Field lightmapUpdateNeededField;

    static Field bossColorModifierPrevField;

    public EntityRendererHook()
    {

    }

// ОРИГИНАЛЬНЫЙ МЕТОД
    public static void onUpdateLightmap(EntityRenderer entityRenderer,
                                        float partialTicks) throws NoSuchFieldException, IllegalAccessException
    {
        Class<?> _class = entityRenderer.getClass();

        boolean lightmapUpdateNeededValue;
        {
            lightmapUpdateNeededField = _class.getDeclaredField(UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG ? "lightmapUpdateNeeded" : "field_78536_aa");
            lightmapUpdateNeededField.setAccessible(true);
            lightmapUpdateNeededValue = lightmapUpdateNeededField.getBoolean(entityRenderer);
        }

        if (!lightmapUpdateNeededValue)
        {
            return;
        }

        Minecraft mc;
        {
            mcField = _class.getDeclaredField(UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG ?
                    "mc" : "field_78531_r");

            mcField.setAccessible(true);
            mc = (Minecraft) mcField.get(entityRenderer);
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

        updateLuminance(entityRenderer, partialTicks, world);
    }

//ДОБАВОЧНЫЙ
    private static boolean blacklistDim(WorldProvider worldProvider)
    {
        DimensionType dimensionType = worldProvider.getDimensionType();

        if (dimensionType == DimensionType.THE_END &&
                !PluginDarknessConfig.getInstance(PluginDarknessConfig.class).isDarknessEnd())
        {
            return true;
        }

        return blacklistContains(worldProvider, dimensionType) ^ PluginDarknessConfig.getInstance(PluginDarknessConfig.class).isInvertBlacklist();
    }

//ДОБАВОЧНЫЙ
    private static boolean blacklistContains(WorldProvider worldProvider,
                                             DimensionType dimensionType)
    {
        String dimensionTypeName = dimensionType.getName();

        for (String blacklistName :
                PluginDarknessConfig.getInstance(PluginDarknessConfig.class).getBlacklistByName())
        {
            if (!blacklistName.equals(dimensionTypeName))
            {
                continue;
            }

            return true;
        }

        int dimID = worldProvider.getDimension();

        for (int blacklistID : PluginDarknessConfig.getInstance(PluginDarknessConfig.class).getBlacklistByID())
        {
            if (dimID != blacklistID)
            {
                continue;
            }

            return true;
        }

        return false;
    }

//ДОБАВОЧНЫЙ
    private static boolean isDark(WorldProvider worldProvider,
                                  DimensionType dimensionType)
    {
        if (dimensionType == DimensionType.OVERWORLD)
        {
            return PluginDarknessConfig.getInstance(PluginDarknessConfig.class).isDarknessOverWorld();
        }
        else if (dimensionType == DimensionType.NETHER)
        {
            return PluginDarknessConfig.getInstance(PluginDarknessConfig.class).isDarknessNether();
        }
        else if (dimensionType == DimensionType.THE_END)
        {
            return PluginDarknessConfig.getInstance(PluginDarknessConfig.class).isDarknessEnd();
        }
        else if (worldProvider.hasSkyLight())
        {
            return PluginDarknessConfig.getInstance(PluginDarknessConfig.class).isDarknessDefault();
        }
        else
        {
            return PluginDarknessConfig.getInstance(PluginDarknessConfig.class).isDarknessSkyLess();
        }
    }

//ДОБАВОЧНЫЙ
    private static float getMoonBrightness(float partialTicks, World world)
    {
        WorldProvider worldProvider = world.provider;
        DimensionType dimensionType = worldProvider.getDimensionType();

        if (!isDark(worldProvider, dimensionType))
        {
            return 1.f;
        }

        if (!worldProvider.hasSkyLight())
        {
            return 0.f;
        }

        float angle = world.getCelestialAngle(partialTicks);

        if (angle <= 0.25f || 0.75f <= angle)
        {
            return 1.f;
        }

        final double moon;

        if (!PluginDarknessConfig.getInstance(PluginDarknessConfig.class).isIgnoreMoonLight())
        {
            double[] phaseFactors = PluginDarknessConfig.getInstance(PluginDarknessConfig.class).getMoonPhaseFactors();

            int moonPhase = worldProvider.getMoonPhase(world.getWorldTime());

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

//ДОБАВОЧНЫЙ
    private static void updateLuminance(EntityRenderer entityRenderer,
                                        float partialTicks,
                                        World world) throws NoSuchFieldException, IllegalAccessException
    {
        WorldProvider worldProvider = world.provider;
        DimensionType dimensionType = worldProvider.getDimensionType();

        float[] brightnessTable = worldProvider.getLightBrightnessTable();

        boolean dimDark = isDark(worldProvider, dimensionType);

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
                        entityRenderer.getClass().getDeclaredField(UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG ? "bossColorModifier" : "field_82831_U");

                bossColorModifierField.setAccessible(true);
                bossColorModifier = bossColorModifierField.getFloat(entityRenderer);
            }

            float bossColorModifierPrev;
            {
                bossColorModifierPrevField =
                        entityRenderer.getClass().getDeclaredField(UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG ?
                                "bossColorModifierPrev" : "field_82832_V");

                bossColorModifierPrevField.setAccessible(true);
                bossColorModifierPrev = bossColorModifierPrevField.getFloat(entityRenderer);
            }

            float torchFlickerX;
            {
                torchFlickerXField =
                        entityRenderer.getClass().getDeclaredField(UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG ?
                                "torchFlickerX" : "field_78514_e");

                torchFlickerXField.setAccessible(true);
                torchFlickerX = torchFlickerXField.getFloat(entityRenderer);
            }

            Object mcObject;
            {
                mcField =
                        entityRenderer.getClass().getDeclaredField(UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG ?
                                "mc" : "field_78531_r");

                mcField.setAccessible(true);
                mcObject = mcField.get(entityRenderer);
            }

            Object gameSettingsObject;
            {
                gameSettingsField =
                        mcObject.getClass().getDeclaredField(UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG ?
                                "gameSettings" : "field_71474_y");

                gameSettingsField.setAccessible(true);
                gameSettingsObject = gameSettingsField.get(mcObject);
            }

            float gammaSetting;
            {
                gammaSettingField =
                        gameSettingsObject.getClass().getDeclaredField(UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG ?
                                "gammaSetting" : "field_74333_Y");

                gammaSettingField.setAccessible(true);
                gammaSetting = gammaSettingField.getFloat(gameSettingsObject);
            }

            int[] lightmapColors;
            {
                lightmapColorsField =
                        entityRenderer.getClass().getDeclaredField(UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG ?
                                "lightmapColors" : "field_78504_Q");

                lightmapColorsField.setAccessible(true);
                lightmapColors = (int[]) lightmapColorsField.get(entityRenderer);
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

            if (dimensionType == DimensionType.THE_END)
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

//ДОБАВОЧНЫЙ
    private static int darken(int color, float lightTarget)
    {
        float r = (color & 0xFF) / 255.f;
        float g = ((color >> 8) & 0xFF) / 255.f;
        float b = ((color >> 16) & 0xFF) / 255.f;
        float l = luminance(r, g, b);

        if (l <= 0.f)
        {
            return color;
        }

        if (lightTarget >= l)
        {
            return color;
        }

        float f = lightTarget / l;

        color = 0xFF000000;
        color |= Math.round(f * r * 255);
        color |= Math.round(f * g * 255) << 8;
        color |= Math.round(f * b * 255) << 16;

        return color;
    }

//ДОБАВОЧНЫЙ
    private static float luminance(float red, float green, float blue)
    {
        return red * 0.2126f + green * 0.7152f + blue * 0.0722f;
    }

//ДОБАВОЧНЫЙ
    private static float linear(float t, float start, float end)
    {
        return start + t * (end - start);
    }
}
