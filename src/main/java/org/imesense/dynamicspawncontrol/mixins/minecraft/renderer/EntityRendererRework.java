package org.imesense.dynamicspawncontrol.mixins.minecraft.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.logfile.EarlyLogBuffer;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.mixins.interfaces.IEntityRendererAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.imesense.dynamicspawncontrol.core.pluginconfig.darkness.PluginDarknessConfig;

import java.util.Arrays;

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
            float skyGreen = skyRed;

            float blockGreen = blockLightBase * ((blockLightBase * 0.6F + 0.4F) * 0.6F + 0.4F);
            float blockBlue = blockLightBase * (blockLightBase * blockLightBase * 0.6F + 0.4F);

            float red = skyRed + blockLightBase;
            float green = skyGreen + blockGreen;
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
                        accessor.invokeGetNightVisionBrightness(minecraft.player, partialTicks);

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

            lightmapColors[index] = 0xFF000000 | (redInt << 16) | (greenInt << 8) | blueInt;
        }

        accessor.getLightmapTexture().updateDynamicTexture();
        accessor.setLightmapUpdateNeeded(false);
        minecraft.mcProfiler.endSection();
    }
}
