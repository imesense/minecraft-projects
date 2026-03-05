package org.imesense.dynamicspawncontrol.mixins.minecraft.world;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.imesense.dynamicspawncontrol.bloodmoonmanager.ClientBloodmoonHandler;
import org.imesense.dynamicspawncontrol.core.logfile.EarlyLogBuffer;
import org.imesense.dynamicspawncontrol.core.logfile.LogManager;
import org.imesense.dynamicspawncontrol.core.mixinconfig.nightrenderer.NightRendererData;
import org.imesense.dynamicspawncontrol.mixins.interfaces.IWorldAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = World.class, remap = false)
public abstract class WorldUpdate
{
    @Unique
    private static final boolean DEBUG_MODE = false;

    @Unique
    private float calculateDarkBrightness(float baseBrightness)
    {
        return baseBrightness;
    }

    @Unique
    private float calculateVanillaBrightness(float baseBrightness)
    {
        return baseBrightness * 0.8f + 0.2f;
    }

    @Unique
    private float calculateDarkNightBrightness(float baseBrightness, int moonPhase)
    {
        float[] moonPhaseFactors = NightRendererData.getMoonPhaseFactorsArray();

        float phaseFactor = (moonPhase >= 0 && moonPhase < moonPhaseFactors.length)
                ? moonPhaseFactors[moonPhase] : 0.f;

        return MathHelper.clamp(baseBrightness + phaseFactor, 0.f, 1.f);
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

        if (NightRendererData.isEnableDarkNight() && !ClientBloodmoonHandler.INSTANCE.isBloodmoonActive())
        {
            if (NightRendererData.isDependenceLightMoonPhase())
            {
                finalRawBrightness = calculateDarkNightBrightness(rawBrightness, moonPhase);

                if (DEBUG_MODE) EarlyLogBuffer.log(LogManager.DEBUG, "[isDependenceLightMoonPhase] finalRawBrightness: " + finalRawBrightness);

                return finalRawBrightness;
            }
            else
            {
                finalRawBrightness = calculateDarkBrightness(rawBrightness);

                if (DEBUG_MODE) EarlyLogBuffer.log(LogManager.DEBUG, "[isEnableDarkNight] finalRawBrightness: " + finalRawBrightness);

                return calculateDarkBrightness(rawBrightness);
            }
        }
        else
        {
            finalRawBrightness = calculateVanillaBrightness(rawBrightness);

            if (DEBUG_MODE) EarlyLogBuffer.log(LogManager.DEBUG, "finalRawBrightness: " + finalRawBrightness);

            return calculateVanillaBrightness(rawBrightness);
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
}