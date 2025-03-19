package org.imesense.dynamicspawncontrol.core.plugin.mod.time_control_mod_forge_1_12_2.handler;

import net.minecraft.world.World;

import org.imesense.dynamicspawncontrol.core.plugin.mod.time_control_mod_forge_1_12_2.Numbers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.imesense.dynamicspawncontrol.core.pluginconfig.timecontrol.PluginTimeControlConfig;

public class TimeHandlerClient implements ITimeHandler {
    private static final Logger log = LogManager.getLogger(TimeHandlerClient.class.getSimpleName());
    private int debugLogDelay = 0;
    private long customtime = 0L;
    private double multiplier = 0.0D;

    public void tick(World world)
    {
        if (!PluginTimeControlConfig.getInstance(PluginTimeControlConfig.class).isSyncToSystemTime())
        {
            ++this.debugLogDelay;

            if (this.multiplier == 0.0D && this.debugLogDelay % 20 == 0)
            {
                log.info("Waiting for server time packet...");
                return;
            }

            ++this.customtime;

            Numbers.setWorldtime(world, this.customtime, this.multiplier);

            if (PluginTimeControlConfig.getInstance(PluginTimeControlConfig.class).isTimeControlDebug() && this.debugLogDelay % 20 == 0)
            {
                long worldtime = world.getWorldTime();
                log.info(String.format("Client time: %s | multiplier: %s | gamerules: %s, %s", worldtime, this.multiplier, world.getGameRules().getBoolean("doDaylightCycle"), world.getGameRules().getBoolean("doDaylightCycle_tc")));
            }
        }

    }

    public void update(long customtime, double multiplier)
    {
        this.multiplier = multiplier;
        this.customtime = customtime;
    }
}
