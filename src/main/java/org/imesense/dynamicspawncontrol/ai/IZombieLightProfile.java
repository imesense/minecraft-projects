package org.imesense.dynamicspawncontrol.ai;

public interface IZombieLightProfile
{
    boolean canReactToLight();

    float getLightReactionChance();

    int getLightSearchRadius();
}
