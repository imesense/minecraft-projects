package org.imesense.dynamicspawncontrol.gameplay.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

import javax.annotation.Nonnull;

/**
 *
 */
public final class cmdAdminDumpEntity extends CommandBase
{
    /**
     *
     * @param nameClass
     */
    public cmdAdminDumpEntity(final String nameClass)
    {
        Log.writeDataToLogFile(Log.TypeLog[0], nameClass);
    }

    /**
     *
     * @return
     */
    @Nonnull
    @Override
    public String getName()
    {
        return "dsc_dump_entity";
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
        return "/dsc_dump_entity";
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
        if (sender instanceof EntityPlayer)
        {
            RayTraceResult result = Minecraft.getMinecraft().objectMouseOver;

            if (result != null && result.typeOfHit == RayTraceResult.Type.ENTITY)
            {
                Entity target = result.entityHit;
                String entityInfo = "Entity Info: ";

                entityInfo += "Name: " + target.getName() + ", ";
                entityInfo += "ID: " + target.getEntityId() + ", ";
                entityInfo += "Class: " + target.getClass().getSimpleName();

                sender.sendMessage(new TextComponentString(entityInfo));
                Log.writeDataToLogFile(Log.TypeLog[0], entityInfo);
            }
            else
            {
                sender.sendMessage(new TextComponentString(TextFormatting.RED + "The entity is not selected!"));
            }
        }
        else
        {
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "This command can only be used by players!"));
        }
    }
}
