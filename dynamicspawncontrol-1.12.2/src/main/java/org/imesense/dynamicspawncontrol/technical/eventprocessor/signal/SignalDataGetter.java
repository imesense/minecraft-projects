package org.imesense.dynamicspawncontrol.technical.eventprocessor.signal;

import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 *
 */
public abstract class SignalDataGetter
{
    /**
     *
     * @return
     */
    public abstract World getWorld();

    /**
     *
     * @return
     */
    public abstract Entity getEntity();

    /**
     *
     * @return
     */
    public abstract BlockPos getPosition();

    /**
     *
     * @return
     */
    public abstract EntityPlayer getPlayer();

    /**
     *
     * @return
     */
    public abstract EntityLivingBase getEntityLiving();
}
