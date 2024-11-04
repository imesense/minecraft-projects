package org.imesense.emptymod.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

/**
 * Client sided proxy
 */
public class ClientProxy implements IProxy
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
        return Minecraft.getInstance().level;
    }

    /**
     * Get client player instance
     * 
     * @return Current player instance
     */
    @Override
    public PlayerEntity getClientPlayer()
    {
        return Minecraft.getInstance().player;
    }
}
