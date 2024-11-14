package org.imesense.dynamicspawncontrol.core.sender;

import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
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
    private final World WORLD;

    /**
     *
     */
    private final EntityPlayerMP ENTITY_PLAYER_MP;

    /**
     *
     * @param world
     * @param entityPlayerMP
     */
    public Sender(World world, EntityPlayerMP entityPlayerMP)
    {
        this.WORLD = world;
        this.ENTITY_PLAYER_MP = entityPlayerMP;
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
     * @param iTextComponent
     */
    @Override
    public void sendMessage(ITextComponent iTextComponent)
    {
        System.out.println(iTextComponent.getFormattedText());
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
        return new Vec3d(0.00, 0.00, 0.00);
    }

    /**
     *
     * @return
     */
    @Nonnull
    @Override
    public World getEntityWorld()
    {
        return this.WORLD;
    }

    /**
     *
     * @return
     */
    @Nullable
    @Override
    public Entity getCommandSenderEntity()
    {
        return this.ENTITY_PLAYER_MP;
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
     * @param TYPE
     * @param amount
     */
    @Override
    public void setCommandStat(@Nonnull final CommandResultStats.Type TYPE, int amount)
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
        return this.WORLD.getMinecraftServer();
    }
}
