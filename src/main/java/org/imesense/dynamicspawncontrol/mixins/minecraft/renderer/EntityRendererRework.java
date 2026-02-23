package org.imesense.dynamicspawncontrol.mixins.minecraft.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import org.imesense.dynamicspawncontrol.core.pluginconfig.darkness.PluginDarknessConfig;
import org.imesense.dynamicspawncontrol.mixins.interfaces.IEntityRendererAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.stream.IntStream;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererRework
{
    @Overwrite
    private void updateLightmap(float partialTicks)
    {
        IEntityRendererAccessor accessor = (IEntityRendererAccessor) this;

        if (accessor.getLightmapUpdateNeeded())
        {
            Minecraft minecraft = accessor.getMinecraft();
            int[] lightmapColors = accessor.getLightmapColors();

            if (!accessor.getLightmapUpdateNeeded())
                return;

            minecraft.mcProfiler.startSection("lightTex");

            World world = minecraft.world;

            if (world == null)
            {
                minecraft.mcProfiler.endSection();
                return;
            }

            float sunBrightness = world.getSunBrightness(1.0F);
            float moonBrightness = getMoonBrightness(partialTicks, world);
            float adjustedSunBrightness = sunBrightness * 0.95F + 0.05F;

            float[] brightnessTable = world.provider.getLightBrightnessTable();
            boolean lightningActive = world.getLastLightningBolt() > 0;
            boolean isTheEnd = world.provider.getDimensionType().getId() == 1;

            float torchFlicker = accessor.getTorchFlickerX() * 0.1f + 1.5f;
            float bossColorModifier = accessor.getBossColorModifier();
            float bossColorModifierPrev = accessor.getBossColorModifierPrev();

            float gammaSetting = minecraft.gameSettings.gammaSetting;
            boolean hasNightVision = minecraft.player.isPotionActive(MobEffects.NIGHT_VISION);

            boolean dimensionBlacklisted = isDimensionBlacklisted(world.provider);
            boolean applyDarkness = !hasNightVision && !dimensionBlacklisted && !(world.getLastLightningBolt() > 0);

            calculateLightmapColors(lightmapColors, brightnessTable, adjustedSunBrightness,
                    sunBrightness, moonBrightness, torchFlicker, lightningActive, isTheEnd, gammaSetting, hasNightVision,
                    bossColorModifier, bossColorModifierPrev, partialTicks, applyDarkness, minecraft.player, world, accessor);

            accessor.getLightmapTexture().updateDynamicTexture();
            accessor.setLightmapUpdateNeeded(false);
            minecraft.mcProfiler.endSection();
        }
    }

    private void calculateLightmapColors(int[] outputColors, float[] brightnessTable, float adjustedSunBrightness,
                                         float sunBrightness, float moonBrightness, float torchFlicker, boolean lightningActive, boolean isTheEnd, float gammaSetting,
                                         boolean hasNightVision, float bossColorModifier, float bossColorModifierPrev,
                                         float partialTicks, boolean applyDarkness, EntityPlayer player, World world, IEntityRendererAccessor accessor)
    {
        for (int index = 0; index < 256; index++)
        {
            int skyLightLevel = index / 16;
            int blockLightLevel = index % 16;

            float skyLightBase = brightnessTable[skyLightLevel] * adjustedSunBrightness;
            float blockLightBase = brightnessTable[blockLightLevel] * torchFlicker;

            if (lightningActive)
            {
                skyLightBase = brightnessTable[skyLightLevel];
            }

            float skyRed = skyLightBase * (sunBrightness * 0.65F + 0.35F);

            float blockGreen = blockLightBase * ((blockLightBase * 0.6F + 0.4F) * 0.6F + 0.4F);
            float blockBlue = blockLightBase * (blockLightBase * blockLightBase * 0.6F + 0.4F);

            float red = skyRed + blockLightBase;
            float green = skyRed + blockGreen;
            float blue = skyLightBase + blockBlue;

            red = red * 0.96F + 0.03F;
            green = green * 0.96F + 0.03F;
            blue = blue * 0.96F + 0.03F;

            if (bossColorModifier > 0.0F)
            {
                float bossBlendFactor =
                        bossColorModifierPrev +
                                (bossColorModifier - bossColorModifierPrev) * partialTicks;

                red = red * (1.0F - bossBlendFactor) + red * 0.7F * bossBlendFactor;
                green = green * (1.0F - bossBlendFactor) + green * 0.6F * bossBlendFactor;
                blue = blue * (1.0F - bossBlendFactor) + blue * 0.6F * bossBlendFactor;
            }

            if (isTheEnd)
            {
                red = 0.22F + blockLightBase * 0.75F;
                green = 0.28F + blockGreen * 0.75F;
                blue = 0.25F + blockBlue * 0.75F;
            }

            red = MathHelper.clamp(red, 0F, 1F);
            green = MathHelper.clamp(green, 0F, 1F);
            blue = MathHelper.clamp(blue, 0F, 1F);

            if (hasNightVision)
            {
                float nightVisionStrength =
                        accessor.invokeGetNightVisionBrightness(player, partialTicks);

                float maxScale = 1.0F / red;
                maxScale = Math.min(maxScale, 1.0F / green);
                maxScale = Math.min(maxScale, 1.0F / blue);

                red = red * (1.0F - nightVisionStrength) + red * maxScale * nightVisionStrength;
                green = green * (1.0F - nightVisionStrength) + green * maxScale * nightVisionStrength;
                blue = blue * (1.0F - nightVisionStrength) + blue * maxScale * nightVisionStrength;
            }

            red = MathHelper.clamp(red, 0F, 1F);
            green = MathHelper.clamp(green, 0F, 1F);
            blue = MathHelper.clamp(blue, 0F, 1F);

            float invRed = 1.0F - red;
            float invGreen = 1.0F - green;
            float invBlue = 1.0F - blue;

            invRed = 1.0F - invRed * invRed * invRed * invRed;
            invGreen = 1.0F - invGreen * invGreen * invGreen * invGreen;
            invBlue = 1.0F - invBlue * invBlue * invBlue * invBlue;

            red = red * (1.0F - gammaSetting) + invRed * gammaSetting;
            green = green * (1.0F - gammaSetting) + invGreen * gammaSetting;
            blue = blue * (1.0F - gammaSetting) + invBlue * gammaSetting;

            red = red * 0.96F + 0.03F;
            green = green * 0.96F + 0.03F;
            blue = blue * 0.96F + 0.03F;

            red = MathHelper.clamp(red, 0F, 1F);
            green = MathHelper.clamp(green, 0F, 1F);
            blue = MathHelper.clamp(blue, 0F, 1F);

            int redInt = (int)(red * 255.0F);
            int greenInt = (int)(green * 255.0F);
            int blueInt = (int)(blue * 255.0F);

            int baseColor = 0xFF000000 | (redInt << 16) | (greenInt << 8) | blueInt;

            if (applyDarkness)
            {
                float skyFactorDark = 1f - skyLightLevel / 15f;

                skyFactorDark = 1 - skyFactorDark * skyFactorDark * skyFactorDark * skyFactorDark;
                skyFactorDark *= moonBrightness;

                float minDark = skyFactorDark * 0.05f;

                float rawAmbientDark = sunBrightness * skyFactorDark;
                float minAmbientDark = rawAmbientDark * (1 - minDark) + minDark;
                float skyBaseDark = brightnessTable[skyLightLevel] * minAmbientDark;

                minDark = 0.35f * skyFactorDark;
                float skyRedDark = skyBaseDark * (rawAmbientDark * (1 - minDark) + minDark);
                float skyGreenDark = skyBaseDark * (rawAmbientDark * (1 - minDark) + minDark);
                float skyBlueDark = skyBaseDark;

                if (bossColorModifier > 0.0F)
                {
                    float d = bossColorModifier - bossColorModifierPrev;
                    float m = bossColorModifierPrev + partialTicks * d;

                    skyRedDark = skyRedDark * (1.0F - m) + skyRedDark * 0.7F * m;
                    skyGreenDark = skyGreenDark * (1.0F - m) + skyGreenDark * 0.6F * m;
                    skyBlueDark = skyBlueDark * (1.0F - m) + skyBlueDark * 0.6F * m;
                }

                float blockFactorDark = 1f - blockLightLevel / 15f;
                blockFactorDark = 1 - blockFactorDark * blockFactorDark * blockFactorDark * blockFactorDark;

                final float flickerDark = torchFlicker;
                final float blockBaseDark = blockFactorDark * brightnessTable[blockLightLevel] * flickerDark;
                minDark = 0.4f * blockFactorDark;

                final float blockGreenDark = blockBaseDark * ((blockBaseDark * (1 - minDark) + minDark) * (1 - minDark) + minDark);
                final float blockBlueDark = blockBaseDark * (blockBaseDark * blockBaseDark * (1 - minDark) + minDark);

                float redDark = skyRedDark + blockBaseDark;
                float greenDark = skyGreenDark + blockGreenDark;
                float blueDark = skyBlueDark + blockBlueDark;

                final float f = Math.max(skyFactorDark, blockFactorDark);
                minDark = 0.03f * f;
                redDark = redDark * (0.99F - minDark) + minDark;
                greenDark = greenDark * (0.99F - minDark) + minDark;
                blueDark = blueDark * (0.99F - minDark) + minDark;

                if (isTheEnd)
                {
                    redDark = skyFactorDark * 0.22F + blockBaseDark * 0.75f;
                    greenDark = skyFactorDark * 0.28F + blockGreenDark * 0.75f;
                    blueDark = skyFactorDark * 0.25F + blockBlueDark * 0.75f;
                }

                redDark = MathHelper.clamp(redDark, 0f, 1f);
                greenDark = MathHelper.clamp(greenDark, 0f, 1f);
                blueDark = MathHelper.clamp(blueDark, 0f, 1f);

                final float gammaFactorDark = gammaSetting * f;

                float invRedDark = 1.0F - redDark;
                float invGreenDark = 1.0F - greenDark;
                float invBlueDark = 1.0F - blueDark;

                invRedDark = 1.0F - invRedDark * invRedDark * invRedDark * invRedDark;
                invGreenDark = 1.0F - invGreenDark * invGreenDark * invGreenDark * invGreenDark;
                invBlueDark = 1.0F - invBlueDark * invBlueDark * invBlueDark * invBlueDark;

                redDark = redDark * (1.0F - gammaFactorDark) + invRedDark * gammaFactorDark;
                greenDark = greenDark * (1.0F - gammaFactorDark) + invGreenDark * gammaFactorDark;
                blueDark = blueDark * (1.0F - gammaFactorDark) + invBlueDark * gammaFactorDark;

                minDark = 0.03f * f;

                redDark = redDark * (0.99F - minDark) + minDark;
                greenDark = greenDark * (0.99F - minDark) + minDark;
                blueDark = blueDark * (0.99F - minDark) + minDark;

                redDark = MathHelper.clamp(redDark, 0f, 1f);
                greenDark = MathHelper.clamp(greenDark, 0f, 1f);
                blueDark = MathHelper.clamp(blueDark, 0f, 1f);

                float lTarget = luminance(redDark, greenDark, blueDark);

                outputColors[index] = darken(baseColor, lTarget);
            }
            else
            {
                outputColors[index] = baseColor;
            }
        }
    }

    private boolean isDimensionBlacklisted(WorldProvider worldProvider)
    {
        int dimID = worldProvider.getDimension();

        for (int blacklistID : PluginDarknessConfig.getInstance(PluginDarknessConfig.class).getBlacklistByID())
        {
            if (dimID == blacklistID)
            {
                return true;
            }
        }

        return false;
    }

    private float getMoonBrightness(float partialTicks, World world)
    {
        WorldProvider worldProvider = world.provider;

        if (!worldProvider.hasSkyLight())
        {
            return 0.f;
        }

        float angle = world.getCelestialAngle(partialTicks);
        if (angle <= 0.25f || 0.75f <= angle)
        {
            return 1.f;
        }

        double[] phaseFactors = PluginDarknessConfig.getInstance(PluginDarknessConfig.class).getMoonPhaseFactors();
        int moonPhase = worldProvider.getMoonPhase(world.getWorldTime());
        double moon = moonPhase < phaseFactors.length ? phaseFactors[moonPhase] : world.getCurrentMoonPhaseFactor();

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

    private int darken(int color, float lightTarget)
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

    private float luminance(float red, float green, float blue)
    {
        return red * 0.2126f + green * 0.7152f + blue * 0.0722f;
    }

    private float linear(float t, float start, float end)
    {
        return start + t * (end - start);
    }
}