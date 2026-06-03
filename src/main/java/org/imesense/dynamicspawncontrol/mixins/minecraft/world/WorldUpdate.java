package org.imesense.dynamicspawncontrol.mixins.minecraft.world;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import org.imesense.dynamicspawncontrol.mechanic.bloodmoon.ClientBloodmoonHandler;
import org.imesense.dynamicspawncontrol.core.logfile.EarlyLogBuffer;
import org.imesense.dynamicspawncontrol.core.logfile.LogManager;
import org.imesense.dynamicspawncontrol.core.mixinconfig.nightrenderer.NightRendererData;
import org.imesense.dynamicspawncontrol.mixins.minecraft.IWorldAccessor;

@Mixin(value = World.class, remap = false)
@SuppressWarnings("UnusedMixin")
public abstract class WorldUpdate
{
    @Unique
    private static final boolean DEBUG_MODE = false;

    @Unique
    private float lastBlendFactor = 0.0f;

    @Unique
    private float $$calculateDarkBrightness(float baseBrightness)
    {
        return baseBrightness;
    }

    @Unique
    private float $$calculateVanillaBrightness(float baseBrightness)
    {
        return baseBrightness * 0.8f + 0.2f;
    }

    @Unique
    private float $$calculateDarkNightBrightness(float baseBrightness, int moonPhase)
    {
        float[] moonPhaseFactors = NightRendererData.getMoonPhaseFactorsArray();

        float phaseFactor = (moonPhase >= 0 && moonPhase < moonPhaseFactors.length)
                ? moonPhaseFactors[moonPhase] : 0.f;

        return MathHelper.clamp(baseBrightness + phaseFactor, 0.f, 1.f);
    }

    @Unique
    private float $$calculateBlendedBrightness(float rawBrightness, int moonPhase, float blendFactor)
    {
        float darkNightBrightness = $$calculateDarkNightBrightness(rawBrightness, moonPhase);
        float vanillaBrightness = $$calculateVanillaBrightness(rawBrightness);

        float result = darkNightBrightness * (1.0f - blendFactor) + vanillaBrightness * blendFactor;

        if (DEBUG_MODE) EarlyLogBuffer.log(LogManager.DEBUG, "[Blended] rawBrightness: " + rawBrightness +
                ", blendFactor: " + blendFactor + ", result: " + result);

        return MathHelper.clamp(result, 0.0f, 1.0f);
    }

    @Overwrite
    public float getSunBrightnessBody(float partialTicks)
    {
        IWorldAccessor worldAccessor = (IWorldAccessor) this;

        float celestialAngle = worldAccessor.invokeGetCelestialAngle(partialTicks);
        float rawBrightness = 1.0F - (MathHelper.cos(celestialAngle * ((float)Math.PI * 2F)) * 2.0F + 0.2F);

        rawBrightness = MathHelper.clamp(rawBrightness, 0.0F, 1.0F);

        rawBrightness = 1.0F - rawBrightness;

        float rainStrength = worldAccessor.invokeGetRainStrength(partialTicks);
        float thunderStrength = worldAccessor.invokeGetThunderStrength(partialTicks);

        rawBrightness = (float)((double)rawBrightness * (1.0D - (double)(rainStrength * 5.0F) / 16.0D));
        rawBrightness = (float)((double)rawBrightness * (1.0D - (double)(thunderStrength * 5.0F) / 16.0D));

        float finalRawBrightness;
        int moonPhase = worldAccessor.invokeGetMoonPhase();

        boolean isDarkNightEnabled = NightRendererData.isEnableDarkNight();
        boolean isBloodmoonActive = ClientBloodmoonHandler.INSTANCE.isBloodmoonActive();

        float bloodmoonBlendFactor = ClientBloodmoonHandler.INSTANCE.getSmoothBlendFactor();

        float targetBlendFactor;

        if (isDarkNightEnabled)
        {
            if (isBloodmoonActive)
            {
                targetBlendFactor = MathHelper.clamp(bloodmoonBlendFactor, 0.0f, 1.0f);
            }
            else
            {
                targetBlendFactor = 0.0f;
            }
        }
        else
        {
            targetBlendFactor = 1.0f;
        }

        float smoothingSpeed = 0.3f;
        lastBlendFactor = lastBlendFactor + (targetBlendFactor - lastBlendFactor) * smoothingSpeed;
        lastBlendFactor = MathHelper.clamp(lastBlendFactor, 0.0f, 1.0f);

        if (isDarkNightEnabled)
        {
            finalRawBrightness = $$calculateBlendedBrightness(rawBrightness, moonPhase, lastBlendFactor);

            if (DEBUG_MODE) EarlyLogBuffer.log(LogManager.DEBUG,
                    "[Smooth Transition] isBloodmoonActive: " + ClientBloodmoonHandler.INSTANCE.isBloodmoonActive() +
                            ", bloodmoonBlendFactor: " + bloodmoonBlendFactor +
                            ", lastBlendFactor: " + lastBlendFactor +
                            ", finalRawBrightness: " + finalRawBrightness);

            return finalRawBrightness;
        }
        else
        {
            finalRawBrightness = $$calculateVanillaBrightness(rawBrightness);
            if (DEBUG_MODE) EarlyLogBuffer.log(LogManager.DEBUG, "finalRawBrightness: " + finalRawBrightness);
            return finalRawBrightness;
        }
    }

    @Inject(method = "getSkyColor",
            at = @At("RETURN"),
            cancellable = true)
    public void onGetSkyColor(Entity entity, float partialTicks, CallbackInfoReturnable<Vec3d> cir)
    {
        if (ClientBloodmoonHandler.INSTANCE.isBloodmoonActive())
        {
            Vec3d originalColor = cir.getReturnValue();
            Vec3d modifiedColor = ClientBloodmoonHandler.INSTANCE.skyColorHook(originalColor);
            cir.setReturnValue(modifiedColor);
        }
    }

    @Inject(method = "getMoonPhase",
            at = @At("RETURN"))
    public void onGetMoonPhase(CallbackInfoReturnable<Integer> cir)
    {
        if (ClientBloodmoonHandler.INSTANCE.isBloodmoonActive())
        {
            ClientBloodmoonHandler.INSTANCE.moonColorHook();
        }
    }

    @Overwrite
    @SideOnly(Side.CLIENT)
    public Vec3d getSkyColorBody(Entity entity, float partialTicks)
    {
        World world = (World) (Object) this;
        IWorldAccessor worldAccessor = (IWorldAccessor) world;

        float celestialAngle = worldAccessor.invokeGetCelestialAngle(partialTicks);
        float timeFactor = MathHelper.cos(celestialAngle * ((float)Math.PI * 2F)) * 2.0F + 0.5F;
        timeFactor = MathHelper.clamp(timeFactor, 0.0F, 1.0F);

        int posX = MathHelper.floor(entity.posX);
        int posY = MathHelper.floor(entity.posY);
        int posZ = MathHelper.floor(entity.posZ);
        BlockPos blockPos = new BlockPos(posX, posY, posZ);

        int blendedColor = ForgeHooksClient.getSkyBlendColour(world, blockPos);

        float red = (float)(blendedColor >> 16 & 255) / 255.0F;
        float green = (float)(blendedColor >> 8 & 255) / 255.0F;
        float blue = (float)(blendedColor & 255) / 255.0F;

        red *= timeFactor;
        green *= timeFactor;
        blue *= timeFactor;

        float rainStrength = worldAccessor.invokeGetRainStrength(partialTicks);
        if (rainStrength > 0.0F)
        {
            float grayScale = (red * 0.3F + green * 0.59F + blue * 0.11F) * 0.6F;
            float rainFactor = 1.0F - rainStrength * 0.75F;
            red = red * rainFactor + grayScale * (1.0F - rainFactor);
            green = green * rainFactor + grayScale * (1.0F - rainFactor);
            blue = blue * rainFactor + grayScale * (1.0F - rainFactor);
        }

        float thunderStrength = worldAccessor.invokeGetThunderStrength(partialTicks);
        if (thunderStrength > 0.0F)
        {
            float grayScale = (red * 0.3F + green * 0.59F + blue * 0.11F) * 0.2F;
            float thunderFactor = 1.0F - thunderStrength * 0.75F;
            red = red * thunderFactor + grayScale * (1.0F - thunderFactor);
            green = green * thunderFactor + grayScale * (1.0F - thunderFactor);
            blue = blue * thunderFactor + grayScale * (1.0F - thunderFactor);
        }

        int lightningBolt = worldAccessor.getLastLightningBolt();
        if (lightningBolt > 0)
        {
            float lightningIntensity = (float)lightningBolt - partialTicks;
            if (lightningIntensity > 1.0F)
            {
                lightningIntensity = 1.0F;
            }
            lightningIntensity *= 0.45F;
            red = red * (1.0F - lightningIntensity) + 0.8F * lightningIntensity;
            green = green * (1.0F - lightningIntensity) + 0.8F * lightningIntensity;
            blue = blue * (1.0F - lightningIntensity) + 1.0F * lightningIntensity;
        }

        return new Vec3d(red, green, blue);
    }
}
