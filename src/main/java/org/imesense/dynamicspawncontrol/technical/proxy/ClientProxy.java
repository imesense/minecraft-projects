package org.imesense.dynamicspawncontrol.technical.proxy;

import java.util.concurrent.Callable;

import com.google.common.util.concurrent.ListenableFuture;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.imesense.dynamicspawncontrol.technical.configs.ConfigManager;

/**
 * Client sided proxy
 */
public class ClientProxy implements IProxy
{
    public static Configuration ConfigLogFile;

    /**
     * Preinitialize modification
     * 
     * @param event Preinitialization event
     */
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        ConfigManager.init(event);
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
        ConfigLogFile.save();
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
    public EntityPlayer getClientPlayer()
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
