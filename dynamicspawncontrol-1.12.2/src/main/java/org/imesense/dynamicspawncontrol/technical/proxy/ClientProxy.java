package org.imesense.dynamicspawncontrol.technical.proxy;

import java.util.concurrent.Callable;

import com.google.common.util.concurrent.ListenableFuture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.imesense.dynamicspawncontrol.technical.register.RegisterOreGenerator;

/**
 * Client sided proxy
 */
public final class ClientProxy implements IProxy
{
    /**
     * Preinitialize modification
     * 
     * @param event Preinitialization event
     */
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        RegisterOreGenerator.init(event);
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
        return Minecraft.getMinecraft().world;
    }

    /**
     * Get client player instance
     *
     * @return Current player instance
     */
    @Override
    public EntityPlayerSP getClientPlayer()
    {
        return Minecraft.getMinecraft().player;
    }

    /**
     * Add task to schedule for execution on client
     * 
     * @param <T> Type of task result
     * @param callableToSchedule Task to complete
     * @return Task result
     */
    @Override
    public synchronized <T> ListenableFuture<T> addScheduledTaskClient(Callable<T> callableToSchedule)
    {
        return Minecraft.getMinecraft().addScheduledTask(callableToSchedule);
    }

    /**
     * Add task to schedule for execution on client
     * 
     * @param runnableToSchedule Task to complete
     * @return Task result
     */
    @Override
    public synchronized ListenableFuture<Object> addScheduledTaskClient(Runnable runnableToSchedule)
    {
        return Minecraft.getMinecraft().addScheduledTask(runnableToSchedule);
    }
}
