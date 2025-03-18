package org.imesense.dynamicspawncontrol.core.plugin.mod.time_control_mod_forge_1_12_2.handler;

import net.minecraft.world.World;

public interface ITimeHandler
{
    void tick(World world);

    void update(long var1, double var3);
}
