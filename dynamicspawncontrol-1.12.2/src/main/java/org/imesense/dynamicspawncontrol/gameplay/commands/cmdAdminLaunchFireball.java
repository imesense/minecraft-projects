package org.imesense.dynamicspawncontrol.gameplay.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.gameplay.items.CustomFireball;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

import javax.annotation.Nonnull;

/**
 *
 */
public final class cmdAdminLaunchFireball extends CommandBase
{
    /**
     *
     */
    public cmdAdminLaunchFireball()
    {
        CodeGenericUtils.printInitClassToLog(cmdAdminLaunchFireball.class);
    }

    /**
     *
     * @return
     */
    @Nonnull
    @Override
    public String getName()
    {
        return "dsc_lf";
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
        return "/dsc_lf";
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
        if (args.length != 2)
        {
            sender.sendMessage(new TextComponentString("-: " + getUsage(sender)));
            return;
        }

        if (!(sender instanceof EntityPlayer))
        {
            sender.sendMessage(new TextComponentString("This command can only be used by players!"));
            return;
        }

        EntityPlayer player = (EntityPlayer) sender;
        World world = player.getEntityWorld();
        double speed, explosionStrength;

        try
        {
            explosionStrength = Double.parseDouble(args[0]);
            speed = Double.parseDouble(args[1]);
        }
        catch (NumberFormatException exception)
        {
            sender.sendMessage(new TextComponentString("Incorrect arguments! Enter the numbers for the force of the explosion and the flight speed."));
            return;
        }

        if (explosionStrength <= 0 || speed <= 0)
        {
            sender.sendMessage(new TextComponentString("The values of the explosion force and flight speed must be positive!"));
            return;
        }

        CustomFireball fireball = new CustomFireball(world, player, 0, 0, 0);

        fireball.setExplosionStrength(explosionStrength);
        fireball.setPosition(player.posX, player.posY + player.getEyeHeight(), player.posZ);

        fireball.accelerationX = player.getLookVec().x * speed;
        fireball.accelerationY = player.getLookVec().y * speed;
        fireball.accelerationZ = player.getLookVec().z * speed;

        world.spawnEntity(fireball);
    }
}
