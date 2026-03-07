package org.imesense.dynamicspawncontrol.core.renderer.night.misc;

import org.imesense.dynamicspawncontrol.mixins.minecraft.IEntityRendererAccessor;

public abstract class BossColorStage
{
    public static float computeBossBlendFactor(IEntityRendererAccessor accessor, float partialTicks)
    {
        float current = accessor.accessorGetBossColorModifier();
        if (current <= 0.0f) return -1.0f;

        float prev = accessor.accessorGetBossColorModifierPrev();
        float d = current - prev;

        return prev + (partialTicks * d);
    }
}
