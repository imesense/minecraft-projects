package org.imesense.emptymod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Main class of modification
 */
@Mod("dynamicspawncontrol")
public class DynamicSpawnControl
{
    /**
     * Instance of this class
     */
    public static DynamicSpawnControl Instance;

    /**
     * Constructs main class object
     */
    public DynamicSpawnControl()
    {
        // Initialize instance
        Instance = this;

        // Register `setup` method for modloading
        FMLJavaModLoadingContext
            .get()
            .getModEventBus()
            .addListener(this::setup);
        // Register `enqueueIMC` method for modloading
        FMLJavaModLoadingContext
            .get()
            .getModEventBus()
            .addListener(this::enqueueIMC);
        // Register `processIMC` method for modloading
        FMLJavaModLoadingContext
            .get()
            .getModEventBus()
            .addListener(this::processIMC);
        // Register `doClientStuff` method for modloading
        FMLJavaModLoadingContext
            .get()
            .getModEventBus()
            .addListener(this::doClientStuff);

        // Register ourselves for server and other game events
        MinecraftForge.EVENT_BUS.register(this);
    }

    /**
     * Setup action
     *
     * @param fmlCommonSetupEvent Common setup event
     */
    private void setup(FMLCommonSetupEvent fmlCommonSetupEvent)
    {

    }

    /**
     * Performs client actions
     * 
     * @param fmlClientSetupEvent Client setup event
     */
    private void doClientStuff(FMLClientSetupEvent fmlClientSetupEvent)
    {

    }

    /**
     * Enqueues method
     * 
     * @param interModEnqueueEvent Intermod enqueue event
     */
    private void enqueueIMC(InterModEnqueueEvent interModEnqueueEvent)
    {

    }

    /**
     * Processes method
     * 
     * @param interModProcessEvent Intermod process event
     */
    private void processIMC(InterModProcessEvent interModProcessEvent)
    {

    }

    /**
     * Server starting action
     * 
     * @param fmlServerStartingEvent Server starting event
     */
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent fmlServerStartingEvent)
    {

    }
}
