package org.imesense.emptymod.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

/**
 * Server sided proxy
 */
public class ServerProxy implements IProxy
{
    /**
     * Initialize modification
     */
    @Override
    public void init()
    {
    }

    /**
     * Get client world instance
     * 
     * @return Current world instance
     */
    @Override
    public World getClientWorld()
    {
        throw new IllegalStateException("The server cannot process this function, the call occurs from the client side!");
    }

    /**
     * Get client player instance
     * 
     * @return Current player instance
     */
    @Override
    public PlayerEntity getClientPlayer()
    {
        throw new IllegalStateException("The server cannot process this function, the call occurs from the client side!");
    }
}
