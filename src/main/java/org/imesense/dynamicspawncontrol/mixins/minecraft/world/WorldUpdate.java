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
        IWorldAccessor worldAccessor = (IWorldAccessor) this;

        float celestialAngle = worldAccessor.invokeGetCelestialAngle(partialTicks);
        float rawBrightness = 1.0F - (MathHelper.cos(celestialAngle * ((float)Math.PI * 2F)) * 2.0F + 0.2F);

        rawBrightness = MathHelper.clamp(rawBrightness, 0.0F, 1.0F);

        rawBrightness = 1.0F - rawBrightness;

        float rainStrength = worldAccessor.invokeGetRainStrength(partialTicks);
        float thunderStrength = worldAccessor.invokeGetThunderStrength(partialTicks);

        rawBrightness = (float)((double)rawBrightness * (1.0D - (double)(rainStrength * 5.0F) / 16.0D));
        rawBrightness = (float)((double)rawBrightness * (1.0D - (double)(thunderStrength * 5.0F) / 16.0D));

        return rawBrightness * 0.8F + 0.2F;
    }
}