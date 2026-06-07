package org.imesense.dynamicspawncontrol.entity.zombie;

public interface IZombieLightProfile
{
    boolean canReactToLight();

    float getLightReactionChance();

    int getLightSearchRadius();
}
