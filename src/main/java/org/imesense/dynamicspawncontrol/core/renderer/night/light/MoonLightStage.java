package org.imesense.dynamicspawncontrol.core.renderer.night.light;

import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.core.mixinconfig.nightrenderer.NightRendererData;

import static org.imesense.dynamicspawncontrol.core.renderer.night.math.FastMath.calculateLinear;

public abstract class MoonLightStage
{
    private static final float FULL_MOON_LOWER_BOUND = 0.25f;
    private static final float FULL_MOON_UPPER_BOUND = 0.75f;
    private static final float TRANSITION_LOWER_BOUND = 0.3f;
    private static final float TRANSITION_UPPER_BOUND = 0.7f;
    private static final float TRANSITION_SCALE = 20.0f;
    private static final float TRANSITION_OFFSET = 0.2f;
    private static final float CELESTIAL_CENTER = 0.5f;

    private static boolean hasSkyLight(World world)
    {
        return world.provider.hasSkyLight();
    }

    private static float getCelestialAngle(World world, float partialTicks)
    {
        return world.getCelestialAngle(partialTicks);
    }

    private static boolean isFullMoonVisible(float celestialAngle)
    {
        return celestialAngle <= FULL_MOON_LOWER_BOUND || celestialAngle >= FULL_MOON_UPPER_BOUND;
    }

    private static float getBaseMoonBrightness(World world)
    {
        if (!NightRendererData.isDependenceLightMoonPhase())
        {
            return 0.0f;
        }

        int moonPhase = getCurrentMoonPhase(world);
        return getMoonPhaseBrightness(moonPhase);
    }

    private static int getCurrentMoonPhase(World world)
    {
        return world.provider.getMoonPhase(world.getWorldTime());
    }

    private static float getMoonPhaseBrightness(int moonPhase)
    {
        return NightRendererData.getMoonPhaseFactorsArray()[moonPhase];
    }

    private static float calculateTransitionFactor(float celestialAngle)
    {
        if (isInTransitionZone(celestialAngle))
        {
            return calculateRawTransitionFactor(celestialAngle);
        }

        return 0.0f;
    }

    private static boolean isInTransitionZone(float celestialAngle)
    {
        return celestialAngle <= TRANSITION_LOWER_BOUND || celestialAngle >= TRANSITION_UPPER_BOUND;
    }

    private static float calculateRawTransitionFactor(float celestialAngle)
    {
        return TRANSITION_SCALE * (Math.abs(celestialAngle - CELESTIAL_CENTER) - TRANSITION_OFFSET);
    }

    private static float calculateFinalMoonBrightness(float transitionFactor, float moonBrightness)
    {
        float squaredTransitionFactor = transitionFactor * transitionFactor;
        return calculateLinear(squaredTransitionFactor, moonBrightness, 1.0f);
    }

    public static float finalPackDarkColor(float partialTicks, World world)
    {
        if (!hasSkyLight(world))
        {
            return 0.0f;
        }

        float celestialAngle = getCelestialAngle(world, partialTicks);

        if (isFullMoonVisible(celestialAngle))
        {
            return 1.0f;
        }

        float moonBrightness = getBaseMoonBrightness(world);
        float transitionFactor = calculateTransitionFactor(celestialAngle);

        return calculateFinalMoonBrightness(transitionFactor, moonBrightness);
    }

    public static float finalPackDarkColor(float partialTicks, World world, Integer forcedMoonPhase)
    {
        if (!hasSkyLight(world))
        {
            return 0.0f;
        }

        float celestialAngle = getCelestialAngle(world, partialTicks);

        if (isFullMoonVisible(celestialAngle))
        {
            return 1.0f;
        }

        float moonBrightness;

        if (forcedMoonPhase != null && NightRendererData.isDependenceLightMoonPhase())
        {
            moonBrightness = getMoonPhaseBrightness(forcedMoonPhase);
        }
        else
        {
            moonBrightness = getBaseMoonBrightness(world);
        }

        float transitionFactor = calculateTransitionFactor(celestialAngle);
        return calculateFinalMoonBrightness(transitionFactor, moonBrightness);
    }
}
