package org.imesense.dynamicspawncontrol.technical.eventprocessor.primitive;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandGameRule;
import net.minecraft.command.CommandTime;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.gameplay.gameworld.WorldTime;
import org.imesense.dynamicspawncontrol.technical.config.gameworldtime.DataPluginWorldTime;
import org.imesense.dynamicspawncontrol.technical.handlers.ITimeHandler;
import org.imesense.dynamicspawncontrol.technical.handlers.TimeHandlerClient;
import org.imesense.dynamicspawncontrol.technical.handlers.TimeHandlerServer;
import org.imesense.dynamicspawncontrol.technical.network.MessageHandler;
import org.imesense.dynamicspawncontrol.technical.network.PacketGameRule;

/**
 *
 */
@Mod.EventBusSubscriber
public final class OnUpdateTimeWorld
{
    /**
     *
     */
    private static final ITimeHandler SERVER_TIME = new TimeHandlerServer();

    /**
     *
     */
    private static final ITimeHandler CLIENT_TIME = new TimeHandlerClient();

    /**
     *
     */
    public static final OnUpdateTimeWorld INSTANCE = new OnUpdateTimeWorld();

    /**
     *
     */
    private OnUpdateTimeWorld()
    {
        CodeGenericUtils.printInitClassToLog(OnUpdateTimeWorld.class);
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent
    public synchronized void onUpdateWorldLoad_0(ChunkDataEvent.Load event)
    {
        World world = event.getWorld();

        if (world.provider.getDimension() == 0)
        {
            world.getGameRules().setOrCreateGameRule("doDaylightCycle", "false");

            if (!world.getGameRules().hasRule("doDaylightCycle_tc"))
            {
                world.getGameRules().setOrCreateGameRule("doDaylightCycle_tc", "true");
            }

            if (!world.isRemote && !DataPluginWorldTime.worldTime.instance.getSyncToSystemTime())
            {
                this.serverUpdate(world.getWorldTime());
            }
        }
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent
    public synchronized void onUpdatePlayerJoin_1(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (event.player instanceof EntityPlayerMP)
        {
            MessageHandler.instance.sendTo(new PacketGameRule(event.player.world.getGameRules().getBoolean("doDaylightCycle_tc")),
                    (EntityPlayerMP)event.player);
        }
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent
    public synchronized void onUpdatePlayerTick_2(TickEvent.PlayerTickEvent event)
    {
        if (event.side == Side.CLIENT && event.phase == TickEvent.Phase.START &&
                event.player.world.provider.getDimension() == 0 &&
                event.player.world.getGameRules().getBoolean("doDaylightCycle_tc"))
        {
            CLIENT_TIME.tick(event.player.world);
        }
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent
    public synchronized void onUpdateWorldTick_4(TickEvent.WorldTickEvent event)
    {
        if (event.world.provider.getDimension() == 0 && event.phase == TickEvent.Phase.START
                && event.world.getGameRules().getBoolean("doDaylightCycle_tc"))
        {
            SERVER_TIME.tick(event.world);
        }
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent
    public synchronized void onUpdateCommand_5(CommandEvent event)
    {
        try
        {
            if (event.getException() == null)
            {
                if (event.getCommand() instanceof CommandGameRule && event.getParameters().length >= 1 &&
                        (event.getParameters()[0].equals("doDaylightCycle") ||
                                event.getParameters()[0].equals("doDaylightCycle_tc")) && event.getSender().getServer() != null)
                {
                    event.getParameters()[0] = "doDaylightCycle_tc";
                    (new CommandGameRule()).execute(event.getSender().getServer(), event.getSender(), event.getParameters());

                    if (event.getParameters().length >= 2)
                    {
                        MessageHandler.instance.sendToAll(new PacketGameRule(CommandBase.parseBoolean(event.getParameters()[1])));
                    }

                    event.setCanceled(true);
                }
                else if (event.getCommand() instanceof CommandTime && event.getParameters().length == 2)
                {
                    String[] args = event.getParameters();

                    if (args[0].equals("set") || args[0].equals("add"))
                    {
                        String arg = args[1];
                        long time;

                        if (args[0].equals("add"))
                        {
                            time = CommandBase.parseLong(arg);
                            time += event.getSender().getServer().getWorld(0).getWorldTime();
                        }
                        else
                        {
                            byte var7 = -1;

                            switch(arg.hashCode())
                            {
                                case 99228:
                                {
                                    if (arg.equals("day"))
                                    {
                                        var7 = 0;
                                    }
                                }
                                break;

                                case 104817688:
                                {
                                    if (arg.equals("night"))
                                    {
                                        var7 = 1;
                                    }
                                }
                            }

                            switch(var7)
                            {
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

                        if (DataPluginWorldTime.worldTime.instance.getSyncToSystemTime())
                        {
                            event.getSender().sendMessage(new TextComponentString
                                    (TextFormatting.RED + "Disable system time synchronization to " + args[0] + " time!"));
                            event.setCanceled(true);
                        }
                        else
                        {
                            this.serverUpdate(time);
                        }
                    }
                }
            }
        }
        catch (CommandException exception)
        {
            event.setException(exception);
        }
    }

    /**
     *
     * @param customTime
     * @param multiplier
     */
    public void clientUpdate(long customTime, double multiplier)
    {
        CLIENT_TIME.update(customTime, multiplier);
    }

    /**
     *
     * @param worldTime
     */
    private void serverUpdate(long worldTime)
    {
        SERVER_TIME.update(WorldTime.customTime(worldTime), WorldTime.multiplier(worldTime));
    }
}
