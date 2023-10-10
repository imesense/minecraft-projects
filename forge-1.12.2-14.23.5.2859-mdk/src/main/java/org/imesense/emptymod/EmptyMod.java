package org.imesense.emptymod;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(
    modid = EmptyMod.MODID,
    name = EmptyMod.NAME,
    version = EmptyMod.VERSION
)
public class EmptyMod
{
    public static final String MODID = "emptymod";
    public static final String NAME = "Empty Mod";
    public static final String VERSION = "1.12.2-14.23.5.2859";

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
}
