package org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandGameRule;
import net.minecraft.command.CommandTime;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.config.DataTimeControl;
import org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.handler.ITimeHandler;
import org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.handler.TimeHandlerClient;
import org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.handler.TimeHandlerServer;
import org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.network.*;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

/**
 *
 */
@EventBusSubscriber
public class TimeEvents {
    public static final TimeEvents INSTANCE = new TimeEvents();
    public static final String doDaylightCycle = "doDaylightCycle";
    public static final String doDaylightCycle_tc = "doDaylightCycle_tc";
    private static final ITimeHandler serverTime = new TimeHandlerServer();
    private static final ITimeHandler clientTime = new TimeHandlerClient();

    private TimeEvents() {
    }

    @SubscribeEvent
    public void onWorldLoad(Load event) {
        World world = event.getWorld();
        if (world.provider.getDimension() == 0) {
            world.getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
            if (!world.getGameRules().hasRule("doDaylightCycle_tc")) {
                world.getGameRules().setOrCreateGameRule("doDaylightCycle_tc", "true");
            }

            if (!world.isRemote && !DataTimeControl.ConfigDataWorldTime.Instance.getSyncToSystemTime()) {
                this.serverUpdate(world.getWorldTime());
            }
        }

    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            MessageHandler.INSTANCE.sendTo(new PacketGameRule(event.player.world.getGameRules().getBoolean("doDaylightCycle_tc")), (EntityPlayerMP)event.player);
        }

    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event) {
        if (event.side == Side.CLIENT && event.phase == Phase.START && event.player.world.provider.getDimension() == 0 && event.player.world.getGameRules().getBoolean("doDaylightCycle_tc")) {
            clientTime.tick(event.player.world);
        }

    }

    @SubscribeEvent
    public void onWorldTick(WorldTickEvent event) {
        if (event.world.provider.getDimension() == 0 && event.phase == Phase.START && event.world.getGameRules().getBoolean("doDaylightCycle_tc")) {
            serverTime.tick(event.world);
        }

    }

    @SubscribeEvent
    public void onCommand(CommandEvent event) {
        try {
            if (event.getException() == null) {
                if (event.getCommand() instanceof CommandGameRule && event.getParameters().length >= 1 && (event.getParameters()[0].equals("doDaylightCycle") || event.getParameters()[0].equals("doDaylightCycle_tc")) && event.getSender().getServer() != null) {
                    event.getParameters()[0] = "doDaylightCycle_tc";
                    (new CommandGameRule()).execute(event.getSender().getServer(), event.getSender(), event.getParameters());
                    if (event.getParameters().length >= 2) {
                        MessageHandler.INSTANCE.sendToAll(new PacketGameRule(CommandBase.parseBoolean(event.getParameters()[1])));
                    }

                    event.setCanceled(true);
                } else if (event.getCommand() instanceof CommandTime && event.getParameters().length == 2) {
                    String[] args = event.getParameters();
                    if (args[0].equals("set") || args[0].equals("add")) {
                        String arg = args[1];
                        long time;
                        if (args[0].equals("add")) {
                            time = CommandBase.parseLong(arg);
                            time += event.getSender().getServer().getWorld(0).getWorldTime();
                        } else {
                            byte var7 = -1;
                            switch(arg.hashCode()) {
                                case 99228:
                                    if (arg.equals("day")) {
                                        var7 = 0;
                                    }
                                    break;
                                case 104817688:
                                    if (arg.equals("night")) {
                                        var7 = 1;
                                    }
                            }

                            switch(var7) {
                                case 0:
                                    time = 1000L;
                                    break;
                                case 1:
                                    time = 13000L;
                                    break;
                                default:
                                    time = CommandBase.parseLong(arg);
                            }
                        }

                        if (DataTimeControl.ConfigDataWorldTime.Instance.getSyncToSystemTime()) {
                            event.getSender().sendMessage(new TextComponentString(TextFormatting.RED + "Disable system time synchronization to " + args[0] + " time!"));
                            event.setCanceled(true);
                        } else {
                            this.serverUpdate(time);
                        }
                    }
                }
            }
        } catch (CommandException var8) {
            event.setException(var8);
        }

    }

    public void clientUpdate(long customtime, double multiplier) {
        clientTime.update(customtime, multiplier);
    }

    private void serverUpdate(long worldtime) {
        serverTime.update(Numbers.customtime(worldtime), Numbers.multiplier(worldtime));
    }
}
