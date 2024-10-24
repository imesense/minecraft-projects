package org.imesense.dynamicspawncontrol.gameplay.command;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.imesense.dynamicspawncontrol.UniqueField;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

import javax.annotation.Nonnull;

/**
 *
 */
public final class CmdAdminDumpEntity extends CommandBase
{
    /**
     *
     */
    public CmdAdminDumpEntity()
    {
        CodeGenericUtil.printInitClassToLog(this.getClass());
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
        if (sender instanceof EntityPlayerMP)
        {
            RayTraceResult result = UniqueField.CLIENT.objectMouseOver;

            if (result != null && result.typeOfHit == RayTraceResult.Type.ENTITY)
            {
                Entity target = result.entityHit;
                String entityInfo = "Entity Info: ";

                entityInfo += "Name: " + target.getName() + ", ";
                entityInfo += "ID: " + target.getEntityId() + ", ";
                entityInfo += "Class: " + target.getClass().getSimpleName();

                sender.sendMessage(new TextComponentString(entityInfo));
                Log.writeDataToLogFile(0, entityInfo);
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
