package org.imesense.emptymod.proxy;

import java.util.concurrent.Callable;

import com.google.common.util.concurrent.ListenableFuture;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Server sided proxy
 */
public class ServerProxy implements IProxy
{
    /**
     * Preinitialize modification
     * 
     * @param event Preinitialization event
     */
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
    }

    /**
     * Initialize modification
     * 
     * @param event Initialization event
     */
    @Override
    public void init(FMLInitializationEvent event)
    {
    }

    /**
     * Postinitialize modification
     * 
     * @param event Postinitialization event
     */
    @Override
    public void postInit(FMLPostInitializationEvent event)
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
    public EntityPlayer getClientPlayer()
    {
        throw new IllegalStateException("The server cannot process this function, the call occurs from the client side!");
    }

    /**
     * Add task to schedule for execution on client
     * 
     * @param <T> Type of task result
     * @param callableToSchedule Task to complete
     * @return Task result
     */
    @Override
    public <T> ListenableFuture<T> addScheduledTaskClient(Callable<T> callableToSchedule)
    {
        throw new IllegalStateException("The server cannot process this function, the call occurs from the client side!");
    }

    /**
     * Add task to schedule for execution on client
     * 
     * @param runnableToSchedule Task to complete
     * @return Task result
     */
    @Override
    public ListenableFuture<Object> addScheduledTaskClient(Runnable runnableToSchedule)
    {
        throw new IllegalStateException("The server cannot process this function, the call occurs from the client side!");
    }
}
