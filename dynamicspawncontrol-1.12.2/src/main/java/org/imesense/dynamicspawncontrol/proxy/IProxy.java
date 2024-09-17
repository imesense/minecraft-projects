package org.imesense.dynamicspawncontrol.proxy;

import java.util.concurrent.Callable;

import com.google.common.util.concurrent.ListenableFuture;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Sided proxy
 */
public interface IProxy
{
    /**
     * Preinitialize modification
     * 
     * @param event Preinitialization event
     */
    void preInit(FMLPreInitializationEvent event);

    /**
     * Initialize modification
     * 
     * @param event Initialization event
     */
    void init(FMLInitializationEvent event);

    /**
     * Postinitialize modification
     * 
     * @param event Postinitialization event
     */
    void postInit(FMLPostInitializationEvent event);

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
    EntityPlayer getClientPlayer();

    /**
     * Add task to schedule for execution on client
     * 
     * @param <T> Type of task result
     * @param callableToSchedule Task to complete
     * @return Task result
     */
    <T> ListenableFuture<T> addScheduledTaskClient(Callable<T> callableToSchedule);

    /**
     * Add task to schedule for execution on client
     * 
     * @param runnableToSchedule Task to complete
     * @return Task result
     */
    ListenableFuture<Object> addScheduledTaskClient(Runnable runnableToSchedule);
}
