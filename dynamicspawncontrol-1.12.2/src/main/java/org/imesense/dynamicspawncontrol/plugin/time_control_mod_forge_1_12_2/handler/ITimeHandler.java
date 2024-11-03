package org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.handler;

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
     * @param customWorldTime
     * @param multiplierWorldTime
     */
    void update(long customWorldTime, double multiplierWorldTime);
}
