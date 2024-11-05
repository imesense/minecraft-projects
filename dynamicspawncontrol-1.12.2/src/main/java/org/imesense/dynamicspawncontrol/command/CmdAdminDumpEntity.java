package org.imesense.dynamicspawncontrol.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

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
        CodeGeneric.printInitClassToLog(this.getClass());
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
     * @param iCommandSender
     * @return
     */
    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender iCommandSender)
    {
        return "/dsc_dump_entity";
    }

    /**
     *
     * @param minecraftServer
     * @param iCommandSender
     * @param args
     */
    @Override
    public void execute(@Nonnull MinecraftServer minecraftServer, @Nonnull ICommandSender iCommandSender, @Nonnull String... args)
    {
        if (iCommandSender instanceof EntityPlayerMP)
        {
            RayTraceResult rayTraceResult = UniqueField.CLIENT.objectMouseOver;

            if (rayTraceResult != null && rayTraceResult.typeOfHit == RayTraceResult.Type.ENTITY)
            {
                Entity entityHit = rayTraceResult.entityHit;
                String entityInfo = "Entity Info: ";

                entityInfo += "Name: " + entityHit.getName() + ", ";
                entityInfo += "ID: " + entityHit.getEntityId() + ", ";
                entityInfo += "Class: " + entityHit.getClass().getSimpleName();

                iCommandSender.sendMessage(new TextComponentString(entityInfo));
                Log.writeDataToLogFile(0, entityInfo);
            }
            else
            {
                iCommandSender.sendMessage(new TextComponentString(TextFormatting.RED + "The entity is not selected!"));
            }
        }
        else
        {
            iCommandSender.sendMessage(new TextComponentString(TextFormatting.RED + "This command can only be used by players!"));
        }
    }
}
