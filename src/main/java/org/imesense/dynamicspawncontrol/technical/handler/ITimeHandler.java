package org.imesense.dynamicspawncontrol.technical.handler;

import net.minecraft.world.World;

/**
 *
 */
public interface ITimeHandler
{
    /**
     *
     * @param world
     */
    void tick(World world);

    /**
     *
     * @param param1
     * @param param2
     */
    void update(long param1, double param2);
}
