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
     * @param param1
     * @param param2
     */
    void update(long param1, double param2);
}
