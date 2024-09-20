package org.imesense.dynamicspawncontrol.gameplay.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public final class cmdServerMobsKiller extends CommandBase
{
    /**
     *
     * @param nameClass
     */
    public cmdServerMobsKiller(final String nameClass)
    {
        Log.writeDataToLogFile(0, nameClass);
    }

    /**
     *
     * @return
     */
    @Nonnull
    @Override
    public String getName()
    {
        return "dsc_mob_killer";
    }

    /**
     *
     * @param sender
     * @return
     */
    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender)
    {
        return "/dsc_mob_killer <'all', 'entity' (id), 'animals' or 'monsters'>";
    }

    /**
     *
     * @param server
     * @param sender
     * @param args
     */
    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String... args)
    {
        if (args.length > 0)
        {
            String argument0 = args[0].toLowerCase();
            WorldServer worldServer = server.getWorld((sender instanceof EntityPlayer) ? sender.getEntityWorld().provider.getDimension() : 0);
            List<Entity> entityList = new ArrayList<>();

            if ("all".equals(argument0))
            {
                for (Entity entity : worldServer.loadedEntityList)
                {
                    if (!(entity instanceof EntityPlayer))
                    {
                        entityList.add(entity);
                    }
                }
            }
            else if ("entity".equals(argument0) && args.length > 1)
            {
                for (int i = 1; i < args.length; i++)
                {
                    int entityId;

                    try
                    {
                        entityId = Integer.parseInt(args[i]);
                    }
                    catch (NumberFormatException e)
                    {
                        sender.sendMessage(new TextComponentString(TextFormatting.RED + "Invalid entity ID: " + args[i]));
                        return;
                    }

                    Entity entity = worldServer.getEntityByID(entityId);

                    if (entity != null)
                    {
                        entityList.add(entity);
                    }
                    else
                    {
                        sender.sendMessage(new TextComponentString(TextFormatting.RED + "An entity with an ID " + entityId + " not found."));
                    }
                }
            }
            else if ("animals".equals(argument0))
            {
                for (Entity entity : worldServer.loadedEntityList)
                {
                    if (entity instanceof EntityAnimal || entity instanceof EntityAmbientCreature)
                    {
                        entityList.add(entity);
                    }
                }
            }
            else if ("monsters".equals(argument0))
            {
                for (Entity entity : worldServer.loadedEntityList)
                {
                    if (entity instanceof IMob)
                    {
                        entityList.add(entity);
                    }
                }
            }
            else
            {
                sender.sendMessage(new TextComponentString(TextFormatting.RED + "An invalid argument. Use 'all', 'animals', 'monsters' или 'entity <id>'"));
                return;
            }

            for (Entity entity : entityList)
            {
                worldServer.removeEntity(entity);
            }

            sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "Total deleted entities: " + entityList.size()));
        }
        else
        {
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "No arguments are provided. Use 'all', 'animals', 'monsters' или 'entity <id>'"));
        }
    }
}
