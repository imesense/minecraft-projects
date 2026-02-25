package org.imesense.dynamicspawncontrol.mixins.minecraft.world;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.mixins.interfaces.IWorldAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = World.class, remap = false)
public abstract class WorldUpdate
{
    @Overwrite
    public float getSunBrightnessBody(float partialTicks)
    {
        IWorldAccessor accessor = (IWorldAccessor) this;

        float f = accessor.invokeGetCelestialAngle(partialTicks);
        float f1 = 1.0F - (MathHelper.cos(f * ((float)Math.PI * 2F)) * 2.0F + 0.2F);

        f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
        f1 = 1.0F - f1;

        f1 = (float)((double)f1 * (1.0D - (double)(accessor.invokeGetRainStrength(partialTicks) * 5.0F) / 16.0D));
        f1 = (float)((double)f1 * (1.0D - (double)(accessor.invokeGetThunderStrength(partialTicks) * 5.0F) / 16.0D));

        return f1 * 1.F + 0.0F;
    }
}
