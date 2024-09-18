package org.imesense.dynamicspawncontrol.technical.customlibrary;

import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 */
public final class Sender implements ICommandSender
{
    /**
     *
     */
    private final World world;

    /**
     *
     */
    private final EntityPlayer player;

    /**
     *
     * @param world
     * @param player
     */
    public Sender(World world, EntityPlayer player)
    {
        this.world = world;
        this.player = player;
    }

    /**
     *
     * @return
     */
    @Nonnull
    @Override
    public String getName()
    {
        return "Sender";
    }

    /**
     *
     * @return
     */
    @Nonnull
    @Override
    public ITextComponent getDisplayName()
    {
        return new TextComponentString("ISender");
    }

    /**
     *
     * @param component
     */
    @Override
    public void sendMessage(ITextComponent component)
    {
        System.out.println(component.getFormattedText());
    }

    /**
     *
     * @param permLevel
     * @param commandName
     * @return
     */
    @Override
    public boolean canUseCommand(int permLevel, @Nonnull String commandName)
    {
        return true;
    }

    /**
     *
     * @return
     */
    @Nonnull
    @Override
    public BlockPos getPosition()
    {
        return new BlockPos(0, 0, 0);
    }

    /**
     *
     * @return
     */
    @Nonnull
    @Override
    public Vec3d getPositionVector()
    {
        return new Vec3d(0, 0, 0);
    }

    /**
     *
     * @return
     */
    @Nonnull
    @Override
    public World getEntityWorld()
    {
        return this.world;
    }

    /**
     *
     * @return
     */
    @Nullable
    @Override
    public Entity getCommandSenderEntity()
    {
        return this.player;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean sendCommandFeedback()
    {
        return net.minecraft.command.ICommandSender.super.sendCommandFeedback();
    }

    /**
     *
     * @param type
     * @param amount
     */
    @Override
    public void setCommandStat(@Nonnull final CommandResultStats.Type type, int amount)
    {
    }

    /**
     *
     * @return
     */
    @Nullable
    @Override
    public MinecraftServer getServer()
    {
        return this.world.getMinecraftServer();
    }
}
