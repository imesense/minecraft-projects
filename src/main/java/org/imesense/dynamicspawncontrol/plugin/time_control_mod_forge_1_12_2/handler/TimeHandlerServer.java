package org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.handler;

import java.util.Calendar;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.Numbers;
import org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.config.DataTimeControl;
import org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.network.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TimeHandlerServer implements ITimeHandler {
    private static final Logger log = LogManager.getLogger(TimeHandlerServer.class.getSimpleName());
    private static final Method wakeAllPlayers = ReflectionHelper.findMethod(WorldServer.class, "wakeAllPlayers", "func_73053_d", new Class[0]);
    private int lastMinute = 0;
    private long customtime;
    private double multiplier;
    private boolean wasDaytime = true;

    public void tick(World world) {
        if (DataTimeControl.ConfigDataWorldTime.Instance.getSyncToSystemTime()) {
            if (!world.isRemote && world.getMinecraftServer().getTickCounter() % DataTimeControl.ConfigDataWorldTime.Instance.getSyncToSystemTimeRate() == 0) {
                this.syncTimeWithSystem(world);
            }
        } else {
            long worldtime = world.getWorldTime();
            boolean isDaytime = Numbers.isDaytime(worldtime);
            if (isDaytime != this.wasDaytime) {
                this.reset(worldtime);
                this.wasDaytime = isDaytime;
            }

            long updatedWorldtime;
            try {
                if (world instanceof WorldServer && ((WorldServer)world).areAllPlayersAsleep()) {
                    updatedWorldtime = worldtime + 24000L;
                    updatedWorldtime -= updatedWorldtime % 24000L;
                    world.provider.setWorldTime(updatedWorldtime);
                    this.reset(updatedWorldtime);
                    this.wasDaytime = true;
                    wakeAllPlayers.invoke(world);
                }
            } catch (InvocationTargetException | IllegalAccessException var7) {
                log.error("Unable to wake players!", var7);
            }

            ++this.customtime;
            Numbers.setWorldtime(world, this.customtime, this.multiplier);
            if (world.getMinecraftServer().getTickCounter() % 20 == 0) {
                MessageHandler.INSTANCE.sendToAll(new PacketTime(this.customtime, this.multiplier));
                if (DataTimeControl.ConfigDataWorldTime.Instance.getTimeControlDebug()) {
                    updatedWorldtime = world.getWorldTime();
                    log.info(Numbers.progressString(updatedWorldtime, ""));
                    log.info(String.format("Server time update: %s -> %s (%s -> %s) (day %s) | multiplier: %s", worldtime, updatedWorldtime, this.customtime - 1L, this.customtime, Numbers.day(updatedWorldtime), this.multiplier));
                }
            }
        }

    }

    private void reset(long worldtime) {
        this.update(Numbers.customtime(worldtime), Numbers.multiplier(worldtime));
    }

    public void update(long customtime, double multiplier) {
        MessageHandler.INSTANCE.sendToAll(new PacketTime(customtime, multiplier));
        this.customtime = customtime;
        this.multiplier = multiplier;
    }

    private void syncTimeWithSystem(World world) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(11);
        int minute = calendar.get(12);
        if (minute != this.lastMinute) {
            this.lastMinute = minute;
            long worldtime = world.getWorldTime();
            long time = Numbers.systemtime(hour, minute, calendar.get(6));
            world.provider.setWorldTime(time);
            if (DataTimeControl.ConfigDataWorldTime.Instance.getTimeControlDebug()) {
                log.info(String.format("System time update: %d -> %d | day %s, %s:%s", worldtime, time, calendar.get(6), hour, minute));
            }
        }

    }
}
