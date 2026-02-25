package org.imesense.dynamicspawncontrol.mixins.minecraft.world;

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
    private static final boolean IS_DEBUG = false;

    @Unique
    private static float[] moonPhaseFactors;

    @Unique
    private static float[] getMoonPhaseFactors()
    {
        if (moonPhaseFactors == null)
        {
            moonPhaseFactors = NightRendererData.getMoonPhaseFactorsArray();
        }

        return moonPhaseFactors;
    }

    @Overwrite
    public float getSunBrightnessBody(float partialTicks)
    {
        IWorldAccessor accessor = (IWorldAccessor) this;

        float f = accessor.invokeGetCelestialAngle(partialTicks);
        float f1 = 1.f - (MathHelper.cos(f * ((float)Math.PI * 2.f)) * 2.f + 0.2f);

        f1 = MathHelper.clamp(f1, 0.f, 1.f);
        f1 = 1.f - f1;

        f1 = (float)((double)f1 * (1.d - (double)(accessor.invokeGetRainStrength(partialTicks) * 5.f) / 16.d));
        f1 = (float)((double)f1 * (1.d - (double)(accessor.invokeGetThunderStrength(partialTicks) * 5.f) / 16.d));

        boolean enableDarkRenderer = NightRendererData.isEnableDarkNight();

        float[] factors = getMoonPhaseFactors();
        int moonPhase = accessor.invokeGetMoonPhase();
        float phaseFactor = (moonPhase >= 0 && moonPhase < factors.length) ? factors[moonPhase] : 0.f;

        float result = enableDarkRenderer ? MathHelper.clamp(f1 + phaseFactor, 0.f, 1.f) : (f1 * 0.8f + 0.2f);

        if (IS_DEBUG)
        {
            EarlyLogBuffer.log(Log.DEBUG, "result light: " + result);
        }

        return result;
    }
}
