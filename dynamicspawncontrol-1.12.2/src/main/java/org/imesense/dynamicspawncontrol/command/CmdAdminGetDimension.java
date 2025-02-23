package org.imesense.dynamicspawncontrol.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import org.imesense.dynamicspawncontrol.core.collection.TextColorCollection;
import org.imesense.dynamicspawncontrol.core.collection.UnicodeCharacterCollection;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import javax.annotation.Nonnull;

/**
 *
 */
public final class CmdAdminGetDimension extends CommandBase
{
    /**
     *
     */
    public CmdAdminGetDimension()
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
        return "dsc_gd";
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
        return "/dsc_gd";
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
        if (args.length > 0)
        {
            iCommandSender.sendMessage(new TextComponentString(UnicodeCharacterCollection.instance.getDescription('\u00A7') +
                    TextColorCollection.instance.getCode("RED") +
                    "This command does not accept arguments."));

            return;
        }

        if (iCommandSender instanceof EntityPlayerMP)
        {
            EntityPlayerMP entityPlayerMP = (EntityPlayerMP) iCommandSender;

            int worldId = entityPlayerMP.world.provider.getDimension();
            entityPlayerMP.sendMessage(new TextComponentString("World ID: " + worldId));
        }
    }
}
