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

        float bossColorModifier = accessor.getBossColorModifier();
        float bossColorModifierPrev = accessor.getBossColorModifierPrev();

        float[] brightnessTable = world.provider.getLightBrightnessTable();
        float torchFlicker = accessor.getTorchFlickerX() * 0.1F + 1.5F;
        boolean lightningActive = world.getLastLightningBolt() > 0;
        boolean isTheEnd = world.provider.getDimensionType().getId() == 1;
        DimensionType dimensionType = world.provider.getDimensionType();
        float gammaSetting = minecraft.gameSettings.gammaSetting;
        boolean hasNightVision = minecraft.player.isPotionActive(MobEffects.NIGHT_VISION);

        int[] calculatedLightmap = new int[256];

        calculateLightmapColors(calculatedLightmap, brightnessTable, adjustedSunBrightness,
            sunBrightness, moonBrightness, torchFlicker, lightningActive, isTheEnd, gammaSetting, hasNightVision,
                bossColorModifier, bossColorModifierPrev, partialTicks, minecraft.player, dimensionType, accessor);

        IntStream.range(0, 256).forEach(i -> lightmapColors[i] = calculatedLightmap[i]);

        accessor.getLightmapTexture().updateDynamicTexture();
        accessor.setLightmapUpdateNeeded(false);
        minecraft.mcProfiler.endSection();
    }

    private void calculateLightmapColors(int[] outputColors, float[] brightnessTable, float adjustedSunBrightness,
                                         float sunBrightness, float moonBrightness, float torchFlicker, boolean lightningActive, boolean isTheEnd, float gammaSetting,
                                         boolean hasNightVision, float bossColorModifier, float bossColorModifierPrev,
                                         float partialTicks, EntityPlayer player, DimensionType dimensionType, IEntityRendererAccessor accessor)
    {
        //long startTime = System.nanoTime();

        IntStream.range(0, 256).parallel().forEach(index ->
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

            outputColors[index] = 0xFF000000 | (redInt << 16) | (greenInt << 8) | blueInt;

            /*
            {
                int skyIndexFinalized = index / 16;
                int blockIndexFinalized = index % 16;

                float skyFactorFinalized = 1f - skyIndexFinalized / 15f;
                skyFactorFinalized = 1 - skyFactorFinalized * skyFactorFinalized * skyFactorFinalized * skyFactorFinalized;
                skyFactorFinalized *= moonBrightness;

                float minFinalized = skyFactorFinalized * 0.05f;
                final float rawAmbientFinalized = sunBrightness * skyFactorFinalized;
                final float minAmbientFinalized = rawAmbientFinalized * (1 - minFinalized) + minFinalized;
                final float skyBaseFinalized = brightnessTable[skyIndexFinalized] * minAmbientFinalized;

                minFinalized = 0.35f * skyFactorFinalized;
                float skyRedFinalized = skyBaseFinalized * (rawAmbientFinalized * (1 - minFinalized) + minFinalized);
                float skyGreenFinalized = skyBaseFinalized * (rawAmbientFinalized * (1 - minFinalized) + minFinalized);
                float skyBlueFinalized = skyBaseFinalized;

                if (bossColorModifier > 0.0F)
                {
                    float deltaFinalized = bossColorModifier - bossColorModifierPrev;
                    float modifierFinalized = bossColorModifierPrev + partialTicks * deltaFinalized;

                    skyRedFinalized = skyRedFinalized * (1.0F - modifierFinalized) + skyRedFinalized * 0.7F * modifierFinalized;
                    skyGreenFinalized = skyGreenFinalized * (1.0F - modifierFinalized) + skyGreenFinalized * 0.6F * modifierFinalized;
                    skyBlueFinalized = skyBlueFinalized * (1.0F - modifierFinalized) + skyBlueFinalized * 0.6F * modifierFinalized;
                }

                float blockFactorFinalized = 1f - blockIndexFinalized / 15f;
                blockFactorFinalized = 1 - blockFactorFinalized * blockFactorFinalized * blockFactorFinalized * blockFactorFinalized;

                final float flickerFinalized = torchFlicker;
                final float blockBaseFinalized = blockFactorFinalized * brightnessTable[blockIndexFinalized] * flickerFinalized;
                minFinalized = 0.4f * blockFactorFinalized;

                final float blockGreenFinalized =
                        blockBaseFinalized * ((blockBaseFinalized * (1 - minFinalized) + minFinalized) * (1 - minFinalized) + minFinalized);

                final float blockBlueFinalized =
                        blockBaseFinalized * (blockBaseFinalized * blockBaseFinalized * (1 - minFinalized) + minFinalized);

                float redFinalized = skyRedFinalized + blockBaseFinalized;
                float greenFinalized = skyGreenFinalized + blockGreenFinalized;
                float blueFinalized = skyBlueFinalized + blockBlueFinalized;

                final float factorFinalized = Math.max(skyFactorFinalized, blockFactorFinalized);
                minFinalized = 0.03f * factorFinalized;

                redFinalized = redFinalized * (0.99F - minFinalized) + minFinalized;
                greenFinalized = greenFinalized * (0.99F - minFinalized) + minFinalized;
                blueFinalized = blueFinalized * (0.99F - minFinalized) + minFinalized;

                if (dimensionType == DimensionType.THE_END)
                {
                    redFinalized = skyFactorFinalized * 0.22F + blockBaseFinalized * 0.75f;
                    greenFinalized = skyFactorFinalized * 0.28F + blockGreenFinalized * 0.75f;
                    blueFinalized = skyFactorFinalized * 0.25F + blockBlueFinalized * 0.75f;
                }

                redFinalized = MathHelper.clamp(redFinalized, 0f, 1f);
                greenFinalized = MathHelper.clamp(greenFinalized, 0f, 1f);
                blueFinalized = MathHelper.clamp(blueFinalized, 0f, 1f);

                final float gammaFactorFinalized = gammaSetting * factorFinalized;

                float invRedFinalized = 1.0F - redFinalized;
                float invGreenFinalized = 1.0F - greenFinalized;
                float invBlueFinalized = 1.0F - blueFinalized;

                invRedFinalized = 1.0F - invRedFinalized * invRedFinalized * invRedFinalized * invRedFinalized;
                invGreenFinalized = 1.0F - invGreenFinalized * invGreenFinalized * invGreenFinalized * invGreenFinalized;
                invBlueFinalized = 1.0F - invBlueFinalized * invBlueFinalized * invBlueFinalized * invBlueFinalized;

                redFinalized = redFinalized * (1.0F - gammaFactorFinalized) + invRedFinalized * gammaFactorFinalized;
                greenFinalized = greenFinalized * (1.0F - gammaFactorFinalized) + invGreenFinalized * gammaFactorFinalized;
                blueFinalized = blueFinalized * (1.0F - gammaFactorFinalized) + invBlueFinalized * gammaFactorFinalized;

                minFinalized = 0.03f * factorFinalized;

                redFinalized = redFinalized * (0.99F - minFinalized) + minFinalized;
                greenFinalized = greenFinalized * (0.99F - minFinalized) + minFinalized;
                blueFinalized = blueFinalized * (0.99F - minFinalized) + minFinalized;

                redFinalized = MathHelper.clamp(redFinalized, 0f, 1f);
                greenFinalized = MathHelper.clamp(greenFinalized, 0f, 1f);
                blueFinalized = MathHelper.clamp(blueFinalized, 0f, 1f);

                float luminanceTargetFinalized = luminance(redFinalized, greenFinalized, blueFinalized);

                lightmapColors[i] = darken(lightmapColors[i], luminanceTargetFinalized);
            }
             */
        });

        //long endTime = System.nanoTime();
        //long durationMicro = (endTime - startTime) / 1_000;
        //long durationNs = (endTime - startTime);

        //System.out.println("Lightmap calculation took: " + durationMicro + " μs (" + durationNs + " ns)");
    }

    private void updateLuminance(int[] lightmapColors, float partialTicks, World world, IEntityRendererAccessor accessor)
    {
        WorldProvider dim = world.provider;
        DimensionType dimType = dim.getDimensionType();
        float[] brightnessTable = dim.getLightBrightnessTable();

        float sunBrightness = world.getSunBrightness(1.0F);
        float moonBrightness = getMoonBrightness(partialTicks, world);

        float bossColorModifier = accessor.getBossColorModifier();
        float bossColorModifierPrev = accessor.getBossColorModifierPrev();
        float torchFlickerX = accessor.getTorchFlickerX();
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

            lightmapColors[i] = darken(lightmapColors[i], lTarget);
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