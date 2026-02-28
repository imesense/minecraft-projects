package org.imesense.dynamicspawncontrol.core.renderer.block;

import org.imesense.dynamicspawncontrol.mixins.interfaces.IEntityRendererAccessor;

import static org.imesense.dynamicspawncontrol.core.renderer.math.FastMath.pow4;

public abstract class BlockLightStage
{
    public static float computeBlockFactor(int blockIndex)
    {
        float blockFactor2 = 1.0f - (blockIndex / 15.0f);
        return 1.0f - pow4(blockFactor2);
    }

    public static float computeTorchFlicker(IEntityRendererAccessor accessor)
    {
        return (accessor.accessorGetTorchFlickerX() * 0.1f) + 1.5f;
    }

    public static float computeBlockBase(float blockFactor, float[] brightnessTable, int blockIndex, float flicker)
    {
        return blockFactor * brightnessTable[blockIndex] * flicker;
    }

    public static float computeBlockGreen(float blockBase, float blockFactor)
    {
        float min = 0.4f * blockFactor;
        return blockBase * ((((blockBase * (1.0f - min)) + min) * (1.0f - min)) + min);
    }

    public static float computeBlockBlue(float blockBase, float blockFactor)
    {
        float min = 0.4f * blockFactor;
        return blockBase * ((blockBase * blockBase * (1.0f - min)) + min);
    }
}
