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

@Mixin(EntityRenderer.class)
public abstract class EntityRendererRework
{
    @Overwrite
    private void updateLightmap(float partialTicks)
    {
        IEntityRendererAccessor accessor = (IEntityRendererAccessor) this;
        Minecraft minecraft = accessor.getMinecraft();
        int[] lightmapColors = accessor.getLightmapColors();

        float bossColorModifier = accessor.getBossColorModifier();
        float bossColorModifierPrev = accessor.getBossColorModifierPrev();

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
        float adjustedSunBrightness = sunBrightness * 0.95F + 0.05F;

        float[] brightnessTable = world.provider.getLightBrightnessTable();
        float torchFlicker = accessor.getTorchFlickerX() * 0.1F + 1.5F;
        boolean lightningActive = world.getLastLightningBolt() > 0;
        boolean isTheEnd = world.provider.getDimensionType().getId() == 1;
        float gammaSetting = minecraft.gameSettings.gammaSetting;
        boolean hasNightVision = minecraft.player.isPotionActive(MobEffects.NIGHT_VISION);

        int[] calculatedLightmap = new int[256];

        calculateLightmapColors(calculatedLightmap, brightnessTable, adjustedSunBrightness,
            sunBrightness, torchFlicker, lightningActive, isTheEnd, gammaSetting, hasNightVision,
                bossColorModifier, bossColorModifierPrev, partialTicks, minecraft.player, accessor);

        System.arraycopy(calculatedLightmap, 0, lightmapColors, 0, 256);

        accessor.getLightmapTexture().updateDynamicTexture();
        accessor.setLightmapUpdateNeeded(false);
        minecraft.mcProfiler.endSection();
    }

    private void calculateLightmapColors(int[] outputColors, float[] brightnessTable, float adjustedSunBrightness,
        float sunBrightness, float torchFlicker, boolean lightningActive, boolean isTheEnd, float gammaSetting, boolean hasNightVision,
            float bossColorModifier, float bossColorModifierPrev, float partialTicks, EntityPlayer player, IEntityRendererAccessor accessor)
    {
        for (int index = 0; index < 256; ++index)
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

            float[] rgb = { red, green, blue };

            red = MathHelper.clamp(rgb[0], 0F, 1F);
            green = MathHelper.clamp(rgb[1], 0F, 1F);
            blue = MathHelper.clamp(rgb[2], 0F, 1F);

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

            outputColors[index] = 0xFF000000 | (redInt << 16) | (greenInt << 8) | blueInt;
        }
    }

    private void updateLuminance(float partialTicks, World world, IEntityRendererAccessor accessor)
    {
        WorldProvider dim = world.provider;
        DimensionType dimType = dim.getDimensionType();
        float[] brightnessTable = dim.getLightBrightnessTable();

        float sunBrightness = world.getSunBrightness(1.0F);
        float moonBrightness = getMoonBrightness(partialTicks, world);

        float bossColorModifier = accessor.getBossColorModifier();
        float bossColorModifierPrev = accessor.getBossColorModifierPrev();
        float torchFlickerX = accessor.getTorchFlickerX();
        int[] lightmapColors = accessor.getLightmapColors();
        float gamma = accessor.getMinecraft().gameSettings.gammaSetting;

        for (int i = 0; i < 256; ++i)
        {
            int skyIndex = i / 16;
            int blockIndex = i % 16;

            float skyFactor = 1f - skyIndex / 15f;
            skyFactor = 1 - skyFactor * skyFactor * skyFactor * skyFactor;
            skyFactor *= moonBrightness;

            float min = skyFactor * 0.05f;
            final float rawAmbient = sunBrightness * skyFactor;
            final float minAmbient = rawAmbient * (1 - min) + min;
            final float skyBase = brightnessTable[skyIndex] * minAmbient;

            min = 0.35f * skyFactor;
            float skyRed = skyBase * (rawAmbient * (1 - min) + min);
            float skyGreen = skyBase * (rawAmbient * (1 - min) + min);
            float skyBlue = skyBase;

            if (bossColorModifier > 0.0F)
            {
                float d = bossColorModifier - bossColorModifierPrev;
                float m = bossColorModifierPrev + partialTicks * d;
                skyRed = skyRed * (1.0F - m) + skyRed * 0.7F * m;
                skyGreen = skyGreen * (1.0F - m) + skyGreen * 0.6F * m;
                skyBlue = skyBlue * (1.0F - m) + skyBlue * 0.6F * m;
            }

            float blockFactor = 1f - blockIndex / 15f;
            blockFactor = 1 - blockFactor * blockFactor * blockFactor * blockFactor;

            final float flicker = torchFlickerX * 0.1F + 1.5F;
            final float blockBase = blockFactor * brightnessTable[blockIndex] * flicker;
            min = 0.4f * blockFactor;

            final float blockGreen = blockBase * ((blockBase * (1 - min) + min) * (1 - min) + min);
            final float blockBlue = blockBase * (blockBase * blockBase * (1 - min) + min);

            float red = skyRed + blockBase;
            float green = skyGreen + blockGreen;
            float blue = skyBlue + blockBlue;

            final float f = Math.max(skyFactor, blockFactor);
            min = 0.03f * f;
            red = red * (0.99F - min) + min;
            green = green * (0.99F - min) + min;
            blue = blue * (0.99F - min) + min;

            if (dimType == DimensionType.THE_END)
            {
                red = skyFactor * 0.22F + blockBase * 0.75f;
                green = skyFactor * 0.28F + blockGreen * 0.75f;
                blue = skyFactor * 0.25F + blockBlue * 0.75f;
            }

            red = MathHelper.clamp(red, 0f, 1f);
            green = MathHelper.clamp(green, 0f, 1f);
            blue = MathHelper.clamp(blue, 0f, 1f);

            final float gammaFactor = gamma * f;

            float invRed = 1.0F - red;
            float invGreen = 1.0F - green;
            float invBlue = 1.0F - blue;
            invRed = 1.0F - invRed * invRed * invRed * invRed;
            invGreen = 1.0F - invGreen * invGreen * invGreen * invGreen;
            invBlue = 1.0F - invBlue * invBlue * invBlue * invBlue;
            red = red * (1.0F - gammaFactor) + invRed * gammaFactor;
            green = green * (1.0F - gammaFactor) + invGreen * gammaFactor;
            blue = blue * (1.0F - gammaFactor) + invBlue * gammaFactor;

            min = 0.03f * f;
            red = red * (0.99F - min) + min;
            green = green * (0.99F - min) + min;
            blue = blue * (0.99F - min) + min;

            red = MathHelper.clamp(red, 0f, 1f);
            green = MathHelper.clamp(green, 0f, 1f);
            blue = MathHelper.clamp(blue, 0f, 1f);

            float lTarget = luminance(red, green, blue);
            int c = lightmapColors[i];
            lightmapColors[i] = darken(c, lTarget);
        }
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