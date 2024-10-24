package org.imesense.dynamicspawncontrol.technical.handler;

import net.minecraft.world.World;

import org.imesense.dynamicspawncontrol.gameplay.gameworld.WorldTime;
import org.imesense.dynamicspawncontrol.technical.config.gameworldtime.DataPluginWorldTime;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

/**
 *
 */
public final class TimeHandlerClient implements ITimeHandler
{
    /**
     *
     */
    private int debugLogDelay = 0;

    /**
     *
     */
    private long customTime = 0L;

    /**
     *
     */
    private double multiplier = 0.0;

    /**
     *
     * @param world
     */
    @Override
    public void tick(World world)
    {
        if (!DataPluginWorldTime.ConfigDataWorldTime.instance.getSyncToSystemTime())
        {
            ++this.debugLogDelay;

            if (this.multiplier == 0.0D && this.debugLogDelay % 20 == 0)
            {
                Log.writeDataToLogFile(0, "Waiting for server time packet...");
                return;
            }

            ++this.customTime;

            WorldTime.setWorldTime(world, this.customTime, this.multiplier);

            if (DataPluginWorldTime.ConfigDataWorldTime.instance.getTimeControlDebug() && this.debugLogDelay % 20 == 0)
            {
                long worldTime = world.getWorldTime();

                Log.writeDataToLogFile(0, String.format("Client time: %s | multiplier: %s | game_rules: %s, %s",
                        worldTime, this.multiplier, world.getGameRules().getBoolean("doDaylightCycle"),
                        world.getGameRules().getBoolean("doDaylightCycle_tc")));
            }
        }
    }

    /**
     *
     * @param customTime
     * @param multiplier
     */
    @Override
    public void update(long customTime, double multiplier)
    {
        this.multiplier = multiplier;
        this.customTime = customTime;
    }
}
