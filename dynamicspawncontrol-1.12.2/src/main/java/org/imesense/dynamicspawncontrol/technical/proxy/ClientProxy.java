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
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.imesense.dynamicspawncontrol.gameplay.worldgenerator.NetherRackGenerator;
import org.imesense.dynamicspawncontrol.technical.initializer.RegisterConfigClasses;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;

/**
 * Client sided proxy
 */
public final class ClientProxy implements IProxy
{
    /**
     *
     */
    public static Configuration ConfigLogFile;

    /**
     *
     */
    public static Configuration ConfigGameDebugger;

    /**
     *
     */
    public static Configuration ConfigOreGeneratorFile;

    /**
     *
     */
    public static Configuration ConfigNights;

    /**
     *
     */
    public static Configuration ConfigWorldTime;

    /**
     *
     */
    public static Configuration ConfigPlayer;

    /**
     *
     */
    public static Configuration ConfigZombieDropItem;

    /**
     *
     */
    public static Configuration ConfigDebugSingleEvents;

    /**
     * Preinitialize modification
     * 
     * @param event Preinitialization event
     */
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        RegisterConfigClasses.init(event);

        GameRegistry.registerWorldGenerator(new NetherRackGenerator("NetherRackGenerator"), 3);
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
        CodeGenericUtils.checkObjectNotNull(ConfigLogFile, "ConfigLogFile").save();
        CodeGenericUtils.checkObjectNotNull(ConfigGameDebugger, "ConfigGameDebugger").save();
        CodeGenericUtils.checkObjectNotNull(ConfigOreGeneratorFile, "ConfigOreGeneratorFile").save();
        CodeGenericUtils.checkObjectNotNull(ConfigNights, "ConfigNights").save();
        CodeGenericUtils.checkObjectNotNull(ConfigWorldTime, "ConfigWorldTime").save();
        CodeGenericUtils.checkObjectNotNull(ConfigPlayer, "ConfigPlayer").save();
        CodeGenericUtils.checkObjectNotNull(ConfigZombieDropItem, "ConfigZombieDropItem").save();
        CodeGenericUtils.checkObjectNotNull(ConfigDebugSingleEvents, "ConfigDebugSingleEvents").save();
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
