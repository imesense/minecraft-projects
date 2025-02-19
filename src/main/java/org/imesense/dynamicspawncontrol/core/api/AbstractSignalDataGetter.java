package org.imesense.dynamicspawncontrol.core.api;

import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * TODO: Legacy code
 */
public abstract class AbstractSignalDataGetter
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
    public abstract EntityPlayerMP getPlayer();

    /**
     *
     * @return
     */
    public abstract EntityLivingBase getEntityLiving();
}
