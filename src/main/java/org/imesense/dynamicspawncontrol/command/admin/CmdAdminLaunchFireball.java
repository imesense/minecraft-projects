package org.imesense.dynamicspawncontrol.command.admin;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.item.InfernoSphereEntityFireball;

import javax.annotation.Nonnull;

@InitLog
public final class CmdAdminLaunchFireball extends CommandBase
{
    public CmdAdminLaunchFireball()
    {

    }

    @Nonnull
    @Override
    public String getName()
    {
        return "dsc_lf";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender iCommandSender)
    {
        return "/dsc_lf";
    }

    @Override
    public void execute(@Nonnull MinecraftServer minecraftServer, @Nonnull ICommandSender iCommandSender, @Nonnull String... args)
    {
        if (args.length != 2)
        {
            iCommandSender.sendMessage(new TextComponentString("-: " + getUsage(iCommandSender)));
            return;
        }

        if (!(iCommandSender instanceof EntityPlayerMP))
        {
            iCommandSender.sendMessage(new TextComponentString("This command can only be used by players!"));
            return;
        }

        EntityPlayerMP entityPlayerMP = (EntityPlayerMP) iCommandSender;
        World world = entityPlayerMP.getEntityWorld();
        double speed, explosionStrength;

        try
        {
            explosionStrength = Double.parseDouble(args[0]);
            speed = Double.parseDouble(args[1]);
        }
        catch (NumberFormatException exception)
        {
            iCommandSender.sendMessage(new TextComponentString("Incorrect arguments! Enter the numbers for the force of the explosion and the flight speed."));
            return;
        }

        if (explosionStrength <= 0.00 || speed <= 0.00)
        {
            iCommandSender.sendMessage(new TextComponentString("The values of the explosion force and flight speed must be positive!"));
            return;
        }

        InfernoSphereEntityFireball dscFireball = new InfernoSphereEntityFireball(world, entityPlayerMP, 0.00, 0.00, 0.00);

        dscFireball.setExplosionStrength(explosionStrength);
        dscFireball.setPosition(entityPlayerMP.posX, entityPlayerMP.posY + entityPlayerMP.getEyeHeight(), entityPlayerMP.posZ);

        dscFireball.accelerationX = entityPlayerMP.getLookVec().x * speed;
        dscFireball.accelerationY = entityPlayerMP.getLookVec().y * speed;
        dscFireball.accelerationZ = entityPlayerMP.getLookVec().z * speed;

        world.spawnEntity(dscFireball);
    }
}
