package org.imesense.emptymod.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

/**
 * Sided proxy
 */
public interface IProxy
{
    /**
     * Initialize modification
     * 
     * @param event Initialization event
     */
    void init();

    /**
     * Get client world instance
     * 
     * @return Current world instance
     */
    World getClientWorld();

    /**
     * Get client player instance
     * 
     * @return Current player instance
     */
    PlayerEntity getClientPlayer();
}
