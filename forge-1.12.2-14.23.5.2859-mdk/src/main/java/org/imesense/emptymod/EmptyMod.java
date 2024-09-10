package org.imesense.emptymod;

import net.minecraft.init.Blocks;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import org.apache.logging.log4j.Logger;

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
    public static final String MODID = "emptymod";

    /**
     * Modification name
     */
    public static final String NAME = "Empty Mod";

    /**
     * Minecraft version
     */
    public static final String VERSION = "1.12.2-14.23.5.2859";

    /**
     * Logger object
     */
    private static Logger logger;

    /**
     * Preinitialize modification
     * 
     * @param event Preinitialization event
     */
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    /**
     * Initialize modification
     * 
     * @param event Initialization event
     */
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
}
