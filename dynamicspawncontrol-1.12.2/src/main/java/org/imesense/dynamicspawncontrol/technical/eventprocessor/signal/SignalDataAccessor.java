package org.imesense.dynamicspawncontrol.technical.eventprocessor.signal;

import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayer;

/**
 *
 * @param <ClassEvent>
 */
public abstract class SignalDataAccessor<ClassEvent>
{
    /**
     *
     * @param classEvent
     * @return
     */
    public abstract int getY(ClassEvent classEvent);

    /**
     *
     * @param classEvent
     * @return
     */
    public abstract World getWorld(ClassEvent classEvent);

    /**
     *
     * @param classEvent
     * @return
     */
    public abstract BlockPos getPos(ClassEvent classEvent);

    /**
     *
     * @param classEvent
     * @return
     */
    public abstract Entity getEntity(ClassEvent classEvent);

    /**
     *
     * @param classEvent
     * @return
     */
    public abstract ItemStack getItem(ClassEvent classEvent);

    /**
     *
     * @param classEvent
     * @return
     */
    public abstract Entity getAttacker(ClassEvent classEvent);

    /**
     *
     * @param classEvent
     * @return
     */
    public abstract EntityPlayer getPlayer(ClassEvent classEvent);

    /**
     *
     * @param classEvent
     * @return
     */
    public abstract DamageSource getSource(ClassEvent classEvent);

    /**
     *
     * @param classEvent
     * @return
     */
    public abstract BlockPos getValidBlockPos(ClassEvent classEvent);
}