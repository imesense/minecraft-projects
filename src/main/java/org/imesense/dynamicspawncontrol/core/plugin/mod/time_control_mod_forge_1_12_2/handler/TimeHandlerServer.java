package org.imesense.dynamicspawncontrol.core.plugin.mod.time_control_mod_forge_1_12_2.handler;

import java.util.Calendar;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.plugin.mod.time_control_mod_forge_1_12_2.Numbers;
import org.imesense.dynamicspawncontrol.core.plugin.mod.time_control_mod_forge_1_12_2.network.MessageHandler;
import org.imesense.dynamicspawncontrol.core.plugin.mod.time_control_mod_forge_1_12_2.network.PacketTime;
import org.imesense.dynamicspawncontrol.core.pluginconfig.timecontrol.PluginTimeControlConfig;

public final class TimeHandlerServer implements ITimeHandler
{
    private int lastMinute = 0;
    private long customTime;
    private double multiplier;
    private boolean wasDaytime = true;
    private static final Method wakeAllPlayers = ReflectionHelper.findMethod(WorldServer.class, "wakeAllPlayers", "func_73053_d", new Class[0]);

    public void tick(World world)
    {
        if (PluginTimeControlConfig.getInstance(PluginTimeControlConfig.class).isSyncToSystemTime())
        {
            if (!world.isRemote && world.getMinecraftServer().getTickCounter() % PluginTimeControlConfig.getInstance(PluginTimeControlConfig.class).getSyncToSystemTimeRate() == 0) {
                this.syncTimeWithSystem(world);
            }
        }
        else
        {
            long worldTime = world.getWorldTime();
            boolean isDaytime = Numbers.isDaytime(worldTime);

            if (isDaytime != this.wasDaytime)
            {
                this.reset(worldTime);
                this.wasDaytime = isDaytime;
            }

            long updatedWorldtime;

            try
            {
                if (world instanceof WorldServer && ((WorldServer)world).areAllPlayersAsleep())
                {
                    updatedWorldtime = worldTime + 24000L;
                    updatedWorldtime -= updatedWorldtime % 24000L;
                    world.provider.setWorldTime(updatedWorldtime);

                    this.reset(updatedWorldtime);
                    this.wasDaytime = true;

                    wakeAllPlayers.invoke(world);
                }
            }
            catch (InvocationTargetException | IllegalAccessException exception)
            {
                Log.writeDataToLogFile(2, "Unable to wake players! Exception: " + exception);
            }

            ++this.customTime;

            Numbers._setWorldTime(world, this.customTime, this.multiplier);

            if (world.getMinecraftServer().getTickCounter() % 20 == 0)
            {
                MessageHandler.INSTANCE.sendToAll(new PacketTime(this.customTime, this.multiplier));

                if (PluginTimeControlConfig.getInstance(PluginTimeControlConfig.class).isTimeControlDebug())
                {
                    updatedWorldtime = world.getWorldTime();

                    Log.writeDataToLogFile(0,Numbers.progressString(updatedWorldtime, ""));

                    Log.writeDataToLogFile(0,String.format("Server time update: %s -> %s (%s -> %s) (day %s) | " +
                            "multiplier: %s", worldTime, updatedWorldtime,
                            this.customTime - 1L, this.customTime, Numbers.day(updatedWorldtime), this.multiplier));
                }
            }
        }
    }

    private void reset(long worldTime)
    {
        this.update(Numbers._customTime(worldTime), Numbers.multiplier(worldTime));
    }

    public void update(long customTime, double multiplier)
    {
        MessageHandler.INSTANCE.sendToAll(new PacketTime(customTime, multiplier));

        this.customTime = customTime;
        this.multiplier = multiplier;
    }

    private void syncTimeWithSystem(World world)
    {
        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(11);
        int minute = calendar.get(12);

        if (minute != this.lastMinute)
        {
            this.lastMinute = minute;

            long worldTime = world.getWorldTime();
            long time = Numbers._systemTime(hour, minute, calendar.get(6));

            world.provider.setWorldTime(time);

            if (PluginTimeControlConfig.getInstance(PluginTimeControlConfig.class).isTimeControlDebug())
            {
                Log.writeDataToLogFile(0, String.format("System time update: %d -> %d | day %s, %s:%s",
                        worldTime, time, calendar.get(6), hour, minute));
            }
        }
    }
}
