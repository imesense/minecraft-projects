package org.imesense.dynamicspawncontrol.core.renderer;

import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.core.renderer.unpack.LightmapUnpack;
import org.imesense.dynamicspawncontrol.mixins.interfaces.IEntityRendererAccessor;

import static org.imesense.dynamicspawncontrol.core.renderer.block.BlockLightStage.*;
import static org.imesense.dynamicspawncontrol.core.renderer.color.ColorClamp.*;
import static org.imesense.dynamicspawncontrol.core.renderer.color.ColorCombineStage.*;
import static org.imesense.dynamicspawncontrol.core.renderer.color.LuminanceStage.*;
import static org.imesense.dynamicspawncontrol.core.renderer.color.ColorCombineDark.finalPackDarkColor;
import static org.imesense.dynamicspawncontrol.core.renderer.dimension.DimensionStage.*;
import static org.imesense.dynamicspawncontrol.core.renderer.gamma.GammaStage.*;
import static org.imesense.dynamicspawncontrol.core.renderer.misc.BossColorStage.*;
import static org.imesense.dynamicspawncontrol.core.renderer.sky.SkyLightStage.*;

public abstract class DarkCalculateLightMapColor
{
    public static int calculateFinalLightMapColor(IEntityRendererAccessor accessor, World world, float partialTicks, int i, float sunBrightness, float moonBrightness, float[] brightnessTable, DimensionType dimType, int color)
    {
        int skyIndex = LightmapUnpack.unpackSkyIndex(i);
        int blockIndex = LightmapUnpack.unpackBlockIndex(i);

        float skyFactor = computeSkyFactor(skyIndex);
        float skyFactor2 = computeSkyFactor2(skyFactor, moonBrightness);

        float rawAmbient = computeRawAmbient(sunBrightness, skyFactor2);
        float minAmbient = computeMinAmbient(rawAmbient, skyFactor2);

        float skyBase = computeSkyBase(brightnessTable, skyIndex, minAmbient);
        float skyModifier = computeSkyColorModifier(rawAmbient, skyFactor2);

        float skyRed = skyBase * skyModifier;
        float skyGreen = skyBase * skyModifier;
        float skyBlue = skyBase;

        float bossBlend = computeBossBlendFactor(accessor, partialTicks);

        if (bossBlend > 0.0f)
        {
            skyRed = (skyRed * (1.0f - bossBlend)) + (skyRed * 0.7f * bossBlend);
            skyGreen = (skyGreen * (1.0f - bossBlend)) + (skyGreen * 0.6f * bossBlend);
            skyBlue = (skyBlue * (1.0f - bossBlend)) + (skyBlue * 0.6f * bossBlend);
        }

        float blockFactor = computeBlockFactor(blockIndex);
        float flicker = computeTorchFlicker(accessor);
        float blockBase = computeBlockBase(blockFactor, brightnessTable, blockIndex, flicker);
        float blockGreen = computeBlockGreen(blockBase, blockFactor);
        float blockBlue = computeBlockBlue(blockBase, blockFactor);

        float red = combineChannel(skyRed, blockBase);
        float green = combineChannel(skyGreen, blockGreen);
        float blue = combineChannel(skyBlue, blockBlue);

        float fa = computeMaxLightFactor(skyFactor2, blockFactor);

        float red2 = applySoftFloor(red, fa);
        float green2 = applySoftFloor(green, fa);
        float blue2 = applySoftFloor(blue, fa);

        if (isEndDimension(world))
        {
            red2 = overrideEndRed(skyFactor2, blockBase);
            green2 = overrideEndGreen(skyFactor2, blockGreen);
            blue2 = overrideEndBlue(skyFactor2, blockBlue);
        }

        float red3 = clamp01(red2);
        float green3 = clamp01(green2);
        float blue3 = clamp01(blue2);

        float gamma = computeGamma(fa);

        float red4 = gammaCorrectChannel(red3, gamma);
        float green4 = gammaCorrectChannel(green3, gamma);
        float blue4 = gammaCorrectChannel(blue3, gamma);

        float finalRed = applyFinalSoftFloor(red4, fa);
        float finalGreen = applyFinalSoftFloor(green4, fa);
        float finalBlue = applyFinalSoftFloor(blue4, fa);

        float lTarget = calculateLuminance(finalRed, finalGreen, finalBlue);

        return finalPackDarkColor(color, lTarget);
    }
}
