package org.imesense.dynamicspawncontrol.ai;

public interface IZombieLightProfile
{
    /** Может ли моб реагировать на источники света */
    boolean canReactToLight();

    /** Шанс реакции (0.0 – 1.0) */
    float getLightReactionChance();

    /** Максимальная дистанция поиска источника */
    int getLightSearchRadius();
}
