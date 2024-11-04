package org.imesense.dynamicspawncontrol;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.imesense.dynamicspawncontrol.core.LogFile;
import org.imesense.dynamicspawncontrol.core.UniqueField;
import org.imesense.dynamicspawncontrol.core.register.RegisterConfigClass;

import java.io.File;

/**
 * Main class of modification
 */
@Mod("dynamicspawncontrol")
public class DynamicSpawnControl
{
    /**
     *
     */
    private static File globalDirectory = null;

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
            .addListener((FMLCommonSetupEvent fmlCommonSetupEvent) ->
            {
                try
                {
                    setup(fmlCommonSetupEvent);
                }
                catch (IllegalAccessException exception)
                {
                    throw new RuntimeException(exception);
                }
            });
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
    private void setup(FMLCommonSetupEvent fmlCommonSetupEvent) throws IllegalAccessException
    {
        globalDirectory = FMLPaths.CONFIGDIR.get().toFile();

        String logPath = globalDirectory.getPath() + File.separator + DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIRECTORY;

        LogFile.createLogFile(logPath, UniqueField.IDEA_RT);
        LogFile.writeDataToLogFile(1, "Launching from Intellij Idea: " + (UniqueField.IDEA_RT ? "true" : "false"));

        UniqueField uniqueField = new UniqueField();
        LogFile.writeDataToLogFile(0, "Object create [UniqueField]: " + uniqueField.hashCode());

        RegisterConfigClass.initializeConfigs();
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

    /**
     *
     * @return
     */
    public static File getGlobalPathToConfigs()
    {
        return globalDirectory;
    }
}
