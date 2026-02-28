package org.imesense.dynamicspawncontrol.core.renderer.misc;

import org.imesense.dynamicspawncontrol.mixins.interfaces.IEntityRendererAccessor;

public abstract class BossColorStage
{
    public static float computeBossBlendFactor(IEntityRendererAccessor accessor, float partialTicks)
    {
        float current = accessor.getBossColorModifier();
        if (current <= 0.0f) return -1.0f;

        float prev = accessor.getBossColorModifierPrev();
        float d = current - prev;
        return prev + (partialTicks * d);
    }
}
