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
import org.imesense.dynamicspawncontrol.technical.config.gameworldtime.DataPluginWorldTime;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
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
    private long customTime = 0L;

    /**
     *
     */
    private double multiplier = 0.0;

    /**
     *
     */
    private int lastMinute = 0;

    /**
     *
     */
    private boolean wasDaytime = true;

    /**
     *
     */
    private static final Method WAKE_ALL_PLAYERS =
            ReflectionHelper.findMethod(WorldServer.class, "wakeAllPlayers", "func_73053_d", new Class[0]);

    /**
     *
     * TODO: Time out of sync
     * @param world
     */
    @Override
    public void tick(World world)
    {
        if (DataPluginWorldTime.ConfigDataWorldTime.instance.getSyncToSystemTime())
        {
            if (!world.isRemote && world.getMinecraftServer().getTickCounter() % DataPluginWorldTime.ConfigDataWorldTime.instance.getSyncToSystemTimeRate() == 0)
            {
                this.syncTimeWithSystem(world);
            }
        }
        else
        {
            long worldTime = world.getWorldTime();
            boolean isDaytime = WorldTime.isDaytime(worldTime);

            if (isDaytime != this.wasDaytime)
            {
                this.reset(worldTime);
                this.wasDaytime = isDaytime;
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
                    this.wasDaytime = true;

                    WAKE_ALL_PLAYERS.invoke(world);
                }
            }
            catch (InvocationTargetException | IllegalAccessException exception)
            {
                Log.writeDataToLogFile(2, "Unable to wake players!");
                Log.writeDataToLogFile(2, "exception: " + exception);
            }

            ++this.customTime;

            WorldTime.setWorldTime(world, this.customTime, this.multiplier);

            if (world.getMinecraftServer().getTickCounter() % 20 == 0)
            {
                MessageHandler.instance.sendToAll(new PacketTime(this.customTime, this.multiplier));

                if (DataPluginWorldTime.ConfigDataWorldTime.instance.getTimeControlDebug())
                {
                    updatedWorldTime = world.getWorldTime();

                    Log.writeDataToLogFile(0, WorldTime.progressString(updatedWorldTime, ""));

                    Log.writeDataToLogFile(0, String.format("Server time update: %s -> %s (%s -> %s) (day %s) | multiplier: %s",
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
        MessageHandler.instance.sendToAll(new PacketTime(customTime, multiplier));

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

            if (DataPluginWorldTime.ConfigDataWorldTime.instance.getTimeControlDebug())
            {
                Log.writeDataToLogFile(0, String.format("System time update: %d -> %d | day %s, %s:%s", worldTime, time, calendar.get(Calendar.DAY_OF_YEAR), hour, minute));
            }
        }
    }
}
