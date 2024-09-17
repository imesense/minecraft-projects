package org.imesense.dynamicspawncontrol;

import net.minecraft.init.Blocks;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;

import org.apache.logging.log4j.Logger;

import org.imesense.dynamicspawncontrol.proxy.IProxy;

/**
 * Main class of modification
 */
@Mod(
    modid = EmptyMod.MODID,
    name = EmptyMod.NAME,
    version = EmptyMod.VERSION
)
public class EmptyMod
{
    /**
     * Modification ID
     */
    public static final String MODID = "dynamicspawncontrol";

    /**
     * Modification name
     */
    public static final String NAME = "Empty Mod";

    /**
     * Minecraft version
     */
    public static final String VERSION = "1.12.2-14.23.5.2860";

    /**
     * Logger object
     */
    private static Logger logger;

    /**
     * Main class instance
     */
    @Mod.Instance
    public static EmptyMod Instance;

    /**
     * Sided proxy settings
     */
    @SidedProxy(
        clientSide = "org.imesense.dynamicspawncontrol.proxy.ClientProxy",
        serverSide = "org.imesense.dynamicspawncontrol.proxy.ServerProxy"
    )
    public static IProxy Proxy;

    /**
     * Preinitialize modification
     * 
     * @param event Preinitialization event
     */
    @EventHandler
    public synchronized void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();

        Proxy.preInit(event);
    }

    /**
     * Initialize modification
     * 
     * @param event Initialization event
     */
    @EventHandler
    public synchronized void init(FMLInitializationEvent event)
    {
        Proxy.init(event);

        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    /**
     * Postinitialize modification
     * 
     * @param event Postinitialization event
     */
    @EventHandler
    public synchronized void postInit(FMLPostInitializationEvent event)
    {
        Proxy.postInit(event);
    }

    /**
     * Load complete action
     * 
     * @param event Load complete event
     */
    @EventHandler
    public synchronized void onLoadComplete(FMLLoadCompleteEvent event)
    {
    }

    /**
     * Server load action
     * 
     * @param event Server starting event
     */
    @EventHandler
    public synchronized void serverLoad(FMLServerStartingEvent event)
    {
    }

    /**
     * Server stopped action
     * 
     * @param event Server stopped action
     */
    @EventHandler
    public synchronized void serverStopped(FMLServerStoppedEvent event)
    {
    }
}
