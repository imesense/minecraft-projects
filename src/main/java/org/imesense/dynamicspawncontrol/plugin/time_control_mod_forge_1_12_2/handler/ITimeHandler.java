package org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.handler;

import net.minecraft.world.World;

/**
 *
 */
public interface ITimeHandler {
    void tick(World var1);

    void update(long var1, double var3);
}
