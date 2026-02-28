package org.imesense.dynamicspawncontrol.mixins.interfaces;

import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(World.class)
public interface IWorldAccessor
{
    @Invoker("getCelestialAngle")
    float invokeGetCelestialAngle(float partialTicks);

    @Invoker("getRainStrength")
    float invokeGetRainStrength(float delta);

    @Invoker("getThunderStrength")
    float invokeGetThunderStrength(float delta);

    @Invoker("getMoonPhase")
    int invokeGetMoonPhase();
}
