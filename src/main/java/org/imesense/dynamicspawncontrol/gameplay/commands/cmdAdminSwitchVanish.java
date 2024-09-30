package org.imesense.dynamicspawncontrol.gameplay.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 *
 */
public final class cmdAdminSwitchVanish extends CommandBase
{
    /**
     *
     */
    public cmdAdminSwitchVanish()
    {
        CodeGenericUtils.printInitClassToLog(this.getClass());
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
     * @param sender
     * @return
     */
    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender)
    {
        return "/dsc_sv";
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
        if (args.length != 1 || (!args[0].equalsIgnoreCase("1") && !args[0].equalsIgnoreCase("0")))
        {
            sender.sendMessage(new TextComponentString(getUsage(sender)));
            return;
        }

        if (!(sender instanceof EntityPlayerMP))
        {
            sender.sendMessage(new TextComponentString("This command can only be used by players!"));
            return;
        }

        EntityPlayerMP player = (EntityPlayerMP) sender;

        if (args[0].equalsIgnoreCase("1"))
        {
            this.apply(player);
        }
        else if (args[0].equalsIgnoreCase("0"))
        {
            this.remove(player);
        }
    }

    /**
     *
     * @param player
     */
    private void apply(EntityPlayerMP player)
    {
        player.addPotionEffect(new PotionEffect(Objects.requireNonNull(Potion.getPotionById(1)), Integer.MAX_VALUE, 1, false, false));
        player.addPotionEffect(new PotionEffect(Objects.requireNonNull(Potion.getPotionById(14)), Integer.MAX_VALUE, 0, false, false));
    }

    /**
     *
     * @param player
     */
    private void remove(EntityPlayerMP player)
    {
        player.removePotionEffect(Objects.requireNonNull(Potion.getPotionById(1)));
        player.removePotionEffect(Objects.requireNonNull(Potion.getPotionById(14)));
    }
}
