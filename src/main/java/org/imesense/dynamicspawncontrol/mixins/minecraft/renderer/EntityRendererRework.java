package org.imesense.dynamicspawncontrol.mixins.minecraft.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.mixins.interfaces.IEntityRendererAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.imesense.dynamicspawncontrol.core.pluginconfig.darkness.PluginDarknessConfig;

import java.util.Arrays;

@TODO(
        value = "Обновить диаграмму классов для пакета",
        showOnce = false,
        priority = TODO.TodoPriority.HIGH)
@Mixin(EntityRenderer.class)
public abstract class EntityRendererRework
{
    @Inject(
            method = "updateLightmap",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/texture/DynamicTexture;updateDynamicTexture()V",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true
    )
    public void $updateLightmap(float partialTicks, CallbackInfo callbackInfo)
    {
        IEntityRendererAccessor accessor = (IEntityRendererAccessor) this;
        int[] lightmapColors = accessor.getLightmapColors();

        if (lightmapColors == null || Arrays.stream(lightmapColors).allMatch(value -> value == 0))
        {
            return;
        }

        if (accessor.getLightmapUpdateNeeded())
        {
            Minecraft mc = accessor.getMinecraft();
            World world = mc.world;

            if (world == null)
            {
                return;
            }

            if (mc.player.isPotionActive(MobEffects.NIGHT_VISION))
            {
                return;
            }

            if (world.getLastLightningBolt() > 0)
            {
                return;
            }

            if (this.isDimensionBlacklisted(world.provider))
            {
                return;
            }

            this.updateLuminance(partialTicks, world, accessor);
            accessor.getLightmapTexture().updateDynamicTexture();
            accessor.setLightmapUpdateNeeded(false);

            callbackInfo.cancel();
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
