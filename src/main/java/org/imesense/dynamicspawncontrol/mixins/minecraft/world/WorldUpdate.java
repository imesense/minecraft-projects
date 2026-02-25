package org.imesense.dynamicspawncontrol.mixins.minecraft.world;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.core.logfile.EarlyLogBuffer;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.mixinconfig.nightrenderer.NightRendererData;
import org.imesense.dynamicspawncontrol.mixins.interfaces.IWorldAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = World.class, remap = false)
public abstract class WorldUpdate
{
    @Unique
    private static float[] cachedMoonPhaseFactors;

    @Unique
    private static final boolean DEBUG_MODE = true;

    @Unique
    private static boolean useVanillaLighting = false;

    @Unique
    private static int lastCheckedDimension = Integer.MIN_VALUE;

    @Unique
    private static float[] getMoonPhaseFactors()
    {
        if (cachedMoonPhaseFactors == null)
        {
            cachedMoonPhaseFactors = NightRendererData.getMoonPhaseFactorsArray();
        }

        return cachedMoonPhaseFactors;
    }

    @Unique
    private float calculateVanillaBrightness(float baseBrightness)
    {
        return baseBrightness * 0.8f + 0.2f;
    }

    @Unique
    private float calculateDarkNightBrightness(float baseBrightness, int moonPhase)
    {
        float[] moonPhaseFactors = getMoonPhaseFactors();
        float phaseFactor = (moonPhase >= 0 && moonPhase < moonPhaseFactors.length)
                ? moonPhaseFactors[moonPhase]
                : 0.f;

        return MathHelper.clamp(baseBrightness + phaseFactor, 0.f, 1.f);
    }

    @Overwrite
    public float getSunBrightnessBody(float partialTicks)
    {
        World world = (World) (Object) this;
        IWorldAccessor worldAccessor = (IWorldAccessor) this;

        float gamma = Minecraft.getMinecraft().gameSettings.gammaSetting;

        int currentDimension = world.provider.getDimension();
        if (currentDimension != lastCheckedDimension)
        {
            useVanillaLighting = NightRendererData.isDimensionBlacklisted(currentDimension);
            lastCheckedDimension = currentDimension;
        }

        float celestialAngle = worldAccessor.invokeGetCelestialAngle(partialTicks);
        float baseBrightness = 1.f - (MathHelper.cos(celestialAngle * ((float)Math.PI * 2.f)) * 2.f + 0.2f);

        baseBrightness = MathHelper.clamp(baseBrightness, 0.f, 1.f);
        baseBrightness = 1.f - baseBrightness;

        float rainStrength = worldAccessor.invokeGetRainStrength(partialTicks);
        float thunderStrength = worldAccessor.invokeGetThunderStrength(partialTicks);

        baseBrightness = (float)((double)baseBrightness * (1.d - (double)(rainStrength * 5.f) / 16.d));
        baseBrightness = (float)((double)baseBrightness * (1.d - (double)(thunderStrength * 5.f) / 16.d));

        boolean enableDarkNight = NightRendererData.isEnableDarkNight();

        float finalBrightness;

        if (useVanillaLighting)
        {
            finalBrightness = calculateVanillaBrightness(baseBrightness);
        }
        else if (enableDarkNight)
        {
            int moonPhase = worldAccessor.invokeGetMoonPhase();
            finalBrightness = calculateDarkNightBrightness(baseBrightness, moonPhase);
        }
        else
        {
            finalBrightness = calculateVanillaBrightness(baseBrightness);
        }

        if (DEBUG_MODE)
        {
            EarlyLogBuffer.log(Log.DEBUG, "Final brightness: " + finalBrightness);
        }

        return finalBrightness;
    }
}