package org.imesense.dynamicspawncontrol.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 *
 */
@InitLog
public final class CmdAdminSwitchVanish extends CommandBase
{
    /**
     *
     */
    public CmdAdminSwitchVanish()
    {

    }

    /**
     *
     * @return
     */
    @Nonnull
    @Override
    public String getName()
    {
        return "dsc_sv";
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
        return "/dsc_sv";
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
        if (args.length != 1 || (!args[0].equalsIgnoreCase("1") && !args[0].equalsIgnoreCase("0")))
        {
            iCommandSender.sendMessage(new TextComponentString(getUsage(iCommandSender)));
            return;
        }

        if (!(iCommandSender instanceof EntityPlayerMP))
        {
            iCommandSender.sendMessage(new TextComponentString("This command can only be used by players!"));
            return;
        }

        EntityPlayerMP entityPlayerMP = (EntityPlayerMP) iCommandSender;

        if (args[0].equalsIgnoreCase("1"))
        {
            this.apply(entityPlayerMP);
        }
        else if (args[0].equalsIgnoreCase("0"))
        {
            this.remove(entityPlayerMP);
        }
    }

    /**
     *
     * @param entityPlayerMP
     */
    private void apply(EntityPlayerMP entityPlayerMP)
    {
        entityPlayerMP.addPotionEffect(new
                PotionEffect(Objects.requireNonNull(Potion.getPotionById
                    (1)), Integer.MAX_VALUE, 1, false, false));

        entityPlayerMP.addPotionEffect(new
                PotionEffect(Objects.requireNonNull(Potion.getPotionById
                    (14)), Integer.MAX_VALUE, 0, false, false));
    }

    /**
     *
     * @param entityPlayerMP
     */
    private void remove(EntityPlayerMP entityPlayerMP)
    {
        entityPlayerMP.removePotionEffect(Objects.requireNonNull(Potion.getPotionById(1)));
        entityPlayerMP.removePotionEffect(Objects.requireNonNull(Potion.getPotionById(14)));
    }
}
