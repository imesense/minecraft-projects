package org.imesense.dynamicspawncontrol.technical.handlers;

import java.util.Calendar;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import org.imesense.dynamicspawncontrol.gameplay.gameworld.WorldTime;
import org.imesense.dynamicspawncontrol.technical.configs.ConfigWorldTime;
import org.imesense.dynamicspawncontrol.technical.network.MessageHandler;
import org.imesense.dynamicspawncontrol.technical.network.PacketTime;

/**
 *
 */
public final class TimeHandlerServer implements ITimeHandler
{
    /**
     *
     */
    private long customTime;

    /**
     *
     */
    private double multiplier;

    /**
     *
     */
    private int lastMinute = 0;

    /**
     *
     */
    private boolean _wasDaytime = true;

    /**
     *
     */
    private static final Logger log = LogManager.getLogger(TimeHandlerServer.class.getSimpleName());

    /**
     *
     */
    private static final Method wakeAllPlayers = ReflectionHelper.findMethod(WorldServer.class, "wakeAllPlayers", "func_73053_d", new Class[0]);

    /**
     *
     * @param world
     */
    @Override
    public void tick(World world)
    {
        if (ConfigWorldTime.SyncToSystemTime)
        {
            if (!world.isRemote && world.getMinecraftServer().getTickCounter() % ConfigWorldTime.SyncToSystemTimeRate == 0)
            {
                this.syncTimeWithSystem(world);
            }
        }
        else
        {
            long worldTime = world.getWorldTime();
            boolean isDaytime = WorldTime.isDaytime(worldTime);

            if (isDaytime != this._wasDaytime)
            {
                this.reset(worldTime);
                this._wasDaytime = isDaytime;
            }

            long updatedWorldTime;

            try
            {
                if (world instanceof WorldServer && ((WorldServer)world).areAllPlayersAsleep())
                {
                    updatedWorldTime = worldTime + 24000L;
                    updatedWorldTime -= updatedWorldTime % 24000L;

                    world.provider.setWorldTime(updatedWorldTime);

                    this.reset(updatedWorldTime);
                    this._wasDaytime = true;

                    wakeAllPlayers.invoke(world);
                }
            }
            catch (InvocationTargetException | IllegalAccessException var)
            {
                log.error("Unable to wake players!", var);
            }

            ++this.customTime;

            WorldTime.setWorldTime(world, this.customTime, this.multiplier);

            if (world.getMinecraftServer().getTickCounter() % 20 == 0)
            {
                MessageHandler.INSTANCE.sendToAll(new PacketTime(this.customTime, this.multiplier));

                if (ConfigWorldTime.TimeControlDebug)
                {
                    updatedWorldTime = world.getWorldTime();

                    log.info(WorldTime.progressString(updatedWorldTime, ""));
                    log.info(String.format("Server time update: %s -> %s (%s -> %s) (day %s) | multiplier: %s",
                            worldTime, updatedWorldTime, this.customTime - 1L, this.customTime, WorldTime.day(updatedWorldTime), this.multiplier));
                }
            }
        }
    }

    /**
     *
     * @param worldTime
     */
    private void reset(long worldTime)
    {
        this.update(WorldTime.customTime(worldTime), WorldTime.multiplier(worldTime));
    }

    /**
     *
     * @param customTime
     * @param multiplier
     */
    @Override
    public void update(long customTime, double multiplier)
    {
        MessageHandler.INSTANCE.sendToAll(new PacketTime(customTime, multiplier));

        this.customTime = customTime;
        this.multiplier = multiplier;
    }

    /**
     *
     * @param world
     */
    private void syncTimeWithSystem(World world)
    {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        if (minute != this.lastMinute)
        {
            this.lastMinute = minute;

            long worldTime = world.getWorldTime();
            long time = WorldTime.systemTime(hour, minute, calendar.get(Calendar.DAY_OF_YEAR));

            world.provider.setWorldTime(time);

            if (ConfigWorldTime.TimeControlDebug)
            {
                log.info(String.format("System time update: %d -> %d | day %s, %s:%s", worldTime, time, calendar.get(Calendar.DAY_OF_YEAR), hour, minute));
            }
        }
    }
}
