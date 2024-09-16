package org.imesense.emptymod;

import java.lang.ref.WeakReference;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main class of modification
 */
@Mod("emptymod")
public class EmptyMod
{
    /**
     * Instance of this class
     */
    public static EmptyMod Instance;

    /**
     * Directly reference log4j logger
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Weak reference to Minecraft instance to prevent memory leak
     */
    private WeakReference<Minecraft> minecraftWeakRef;

    /**
     * Constructs main class object
     */
    public EmptyMod()
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
     * @param event Common setup event
     */
    private void setup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    /**
     * Performs client actions
     * 
     * @param event Client setup event
     */
    private void doClientStuff(final FMLClientSetupEvent event)
    {
        // Use weak reference to prevent memory leak
        minecraftWeakRef = new WeakReference<>(event.getMinecraftSupplier().get());
        Minecraft mcInstance = minecraftWeakRef.get();
        if (mcInstance != null)
        {
            LOGGER.info("Got game settings {}", mcInstance.options);
        }
        else
        {
            LOGGER.warn("Minecraft instance is no longer available (garbage collected)");
        }
    }

    /**
     * Enqueues method
     * 
     * @param event Intermod enqueue event
     */
    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        InterModComms.sendTo("examplemod", "helloworld", () ->
        {
            LOGGER.info("Hello world from the MDK"); return "Hello world";
        });
    }

    /**
     * Processes method
     * 
     * @param event Intermod process event
     */
    private void processIMC(final InterModProcessEvent event)
    {
        LOGGER.info("Got IMC {}", event.getIMCStream().
            map(message -> message.getMessageSupplier().get()).
            collect(Collectors.toList()));
    }

    /**
     * Server starting action
     * 
     * @param event Server starting event
     */
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event)
    {
        LOGGER.info("HELLO from server starting");
    }

    /**
     * Registry events
     */
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
        /**
         * Blocks registry action
         * 
         * @param blockRegistryEvent Block registry event
         */
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent)
        {
            LOGGER.info("HELLO from Register Block");
        }
    }
}
