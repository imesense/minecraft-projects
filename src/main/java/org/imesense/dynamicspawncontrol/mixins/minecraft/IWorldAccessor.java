package org.imesense.dynamicspawncontrol.mixins.minecraft;

import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(World.class)
@SuppressWarnings("UnusedMixin")
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

    @Accessor("rainingStrength")
    float getRainingStrength();

    @Accessor("rainingStrength")
    void setRainingStrength(float value);

    @Accessor("prevRainingStrength")
    float getPrevRainingStrength();

    @Accessor("prevRainingStrength")
    void setPrevRainingStrength(float value);

    @Accessor("thunderingStrength")
    float getThunderingStrength();

    @Accessor("thunderingStrength")
    void setThunderingStrength(float value);

    @Accessor("prevThunderingStrength")
    float getPrevThunderingStrength();

    @Accessor("prevThunderingStrength")
    void setPrevThunderingStrength(float value);

    @Accessor("lastLightningBolt")
    int getLastLightningBolt();

    @Accessor("lastLightningBolt")
    void setLastLightningBolt(int value);

    @Accessor("skylightSubtracted")
    int getSkylightSubtracted();

    @Accessor("skylightSubtracted")
    void setSkylightSubtracted(int value);

    @Accessor("updateLCG")
    int getUpdateLCG();

    @Accessor("updateLCG")
    void setUpdateLCG(int value);
}
