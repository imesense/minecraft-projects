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
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.imesense.dynamicspawncontrol.ProjectStructure;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.config.DataTimeControl;
import org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.handler.ITimeHandler;
import org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.handler.TimeHandlerClient;
import org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.handler.TimeHandlerServer;
import org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.network.*;

import java.util.Objects;

/**
 *
 */
@Mod.EventBusSubscriber(modid = ProjectStructure.STRUCT_INFO_MOD.MOD_ID)
public final class TimeEvents
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
    public static final TimeEvents INSTANCE = new TimeEvents();

    /**
     *
     */
    private TimeEvents()
    {
        CodeGenericUtil.printInitClassToLog(this.getClass());
    }

    /**
     *
     * @param chunkDataEvent
     */
    @SubscribeEvent
    public synchronized void onUpdateWorldLoad_0(ChunkDataEvent.Load chunkDataEvent)
    {
        World world = chunkDataEvent.getWorld();

        if (world.provider.getDimension() == 0)
        {
            world.getGameRules().setOrCreateGameRule("doDaylightCycle", "false");

            if (!world.getGameRules().hasRule("doDaylightCycle_tc"))
            {
                world.getGameRules().setOrCreateGameRule("doDaylightCycle_tc", "true");
            }

            if (!world.isRemote && !DataTimeControl.ConfigDataWorldTime.Instance.getSyncToSystemTime())
            {
                this.serverUpdate(world.getWorldTime());
            }
        }
    }

    /**
     *
     * @param playerLoggedInEvent
     */
    @SubscribeEvent
    public synchronized void onUpdatePlayerJoin_1(PlayerEvent.PlayerLoggedInEvent playerLoggedInEvent)
    {
        if (playerLoggedInEvent.player instanceof EntityPlayerMP)
        {
            MessageHandler.Instance.sendTo(new PacketGameRule(playerLoggedInEvent.player.world.getGameRules().getBoolean("doDaylightCycle_tc")),
                    (EntityPlayerMP)playerLoggedInEvent.player);
        }
    }

    /**
     *
     * @param playerTickEvent
     */
    @SubscribeEvent
    public static void onUpdatePlayerTick_2(TickEvent.PlayerTickEvent playerTickEvent)
    {
        if (playerTickEvent.side == Side.CLIENT && playerTickEvent.phase == TickEvent.Phase.START &&
                playerTickEvent.player.world.provider.getDimension() == 0 &&
                playerTickEvent.player.world.getGameRules().getBoolean("doDaylightCycle_tc"))
        {
            CLIENT_TIME.tick(playerTickEvent.player.world);
        }
    }

    /**
     *
     * @param worldTickEvent
     */
    @SubscribeEvent
    public static void onUpdateWorldTick_4(TickEvent.WorldTickEvent worldTickEvent)
    {
        if (worldTickEvent.world.provider.getDimension() == 0 && worldTickEvent.phase == TickEvent.Phase.START
                && worldTickEvent.world.getGameRules().getBoolean("doDaylightCycle_tc"))
        {
            SERVER_TIME.tick(worldTickEvent.world);
        }
    }

    /**
     *
     * @param commandEvent
     */
    @SubscribeEvent
    public synchronized void onUpdateCommand_5(CommandEvent commandEvent)
    {
        try
        {
            if (commandEvent.getException() == null)
            {
                if (commandEvent.getCommand() instanceof CommandGameRule && commandEvent.getParameters().length >= 1 &&
                        (commandEvent.getParameters()[0].equals("doDaylightCycle") ||
                                commandEvent.getParameters()[0].equals("doDaylightCycle_tc")) && commandEvent.getSender().getServer() != null)
                {
                    commandEvent.getParameters()[0] = "doDaylightCycle_tc";

                    (new CommandGameRule()).execute(commandEvent.getSender().getServer(), commandEvent.getSender(), commandEvent.getParameters());

                    if (commandEvent.getParameters().length >= 2)
                    {
                        MessageHandler.Instance.sendToAll(new PacketGameRule(CommandBase.parseBoolean(commandEvent.getParameters()[1])));
                    }

                    commandEvent.setCanceled(true);
                }
                else if (commandEvent.getCommand() instanceof CommandTime && commandEvent.getParameters().length == 2)
                {
                    String[] args = commandEvent.getParameters();

                    if (args[0].equals("set") || args[0].equals("add"))
                    {
                        String arg = args[1];
                        long time;

                        if (args[0].equals("add"))
                        {
                            time = CommandBase.parseLong(arg);

                            time += Objects.requireNonNull(
                                    commandEvent.getSender().getServer()).getWorld(0).getWorldTime();
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

                        if (DataTimeControl.ConfigDataWorldTime.Instance.getSyncToSystemTime())
                        {
                            commandEvent.getSender().sendMessage(new TextComponentString
                                    (TextFormatting.RED + "Disable system time synchronization to " + args[0] + " time!"));

                            commandEvent.setCanceled(true);
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
            commandEvent.setException(exception);
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
        SERVER_TIME.update(Numbers.customTime(worldTime), Numbers.multiplier(worldTime));
    }
}
