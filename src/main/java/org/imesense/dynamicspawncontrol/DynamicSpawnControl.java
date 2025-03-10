package org.imesense.dynamicspawncontrol;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.imesense.dynamicspawncontrol.core.baseregister.BaseEventRegister;
import org.imesense.dynamicspawncontrol.core.interfaces.IRecipes;
import org.imesense.dynamicspawncontrol.core.collection.CmdCallTypeCollection;
import org.imesense.dynamicspawncontrol.core.collection.TextColorCollection;
import org.imesense.dynamicspawncontrol.core.collection.UnicodeCharacterCollection;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.register.command.CommandRegister;
import org.imesense.dynamicspawncontrol.core.register.config.ConfigRegister;
import org.imesense.dynamicspawncontrol.core.register.parser.ParserRegister;
import org.imesense.dynamicspawncontrol.core.register.worldgenerator.WorldGeneratorRegister;
import org.imesense.dynamicspawncontrol.core.worldcache.CacheGeneralStorage;
import org.imesense.dynamicspawncontrol.eventdescriptions.WindowTitle;
import org.imesense.dynamicspawncontrol.plugin.webslinger_1_12_2_2_2_4.capability.WebSlingerCapability;
import org.imesense.dynamicspawncontrol.recipes.CraftItemWeb;
import org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.TimeEvents;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.network.*;
import org.imesense.dynamicspawncontrol.plugin.webslinger_1_12_2_2_2_4.webbing.PlayerInWebMessage;
import org.imesense.dynamicspawncontrol.core.worldcache.CacheEntityStorage;

import java.io.File;

/**
 * Main class of modification
 */
@Mod(
    modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID,
    name = DynamicSpawnControlStructure.STRUCT_INFO_MOD.NAME,
    version = DynamicSpawnControlStructure.STRUCT_INFO_MOD.VERSION
)
public final class DynamicSpawnControl
{
    /**
     *
     */
    @Mod.Instance
    public static DynamicSpawnControl Instance;

    /**
     *
     */
    private static File globalDirectory = null;

    /**
     *
     * @return
     */
    public static File getGlobalPathToConfigs()
    {
        return globalDirectory;
    }

    /**
     *
     */
    public static IRecipes IRecipes;

    /**
     *
     */
    public DynamicSpawnControl()
    {
        Instance = this;
    }

    /**
     *
     */
    public static SimpleNetworkWrapper networkWrapper = null;

    /**
     *
     * @param fmlPreInitializationEvent
     * @throws IllegalAccessException
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent fmlPreInitializationEvent) throws IllegalAccessException
    {
        globalDirectory = fmlPreInitializationEvent.getModConfigurationDirectory();

        Log.createLogFile(globalDirectory.getPath() +
                        File.separator + DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIRECTORY,
                UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG);

        File modFile = fmlPreInitializationEvent.getSourceFile();
        String expectedName = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID + "-0.1.jar";

        Log.writeDataToLogFile(3, "Checking the name of the mod: " + modFile + " " + "required: " + expectedName);

        if (!modFile.getName().equals(expectedName) && !UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG)
        {
            Log.writeDataToLogFile(2, "Renaming a mod is prohibited.");
            Log.writeDataToLogFile(2, "You can make an official fork and rename it in its original form:");
            Log.writeDataToLogFile(2, "https://github.com/imesense/minecraft-projects");

            FMLCommonHandler.instance().exitJava(1, false);
        }

        Log.writeDataToLogFile(1, "Is running in IDE (based on logging level): " +
                (UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG ? "true" : "false"));

        MessageHandler.init();

        WebSlingerCapability.register();
        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("dynamicspawncontrol");
        PlayerInWebMessage.register(networkWrapper);

        ConfigRegister.getInstance().initializeConfigs();
        WorldGeneratorRegister.getInstance().init(fmlPreInitializationEvent);

        CacheEntityStorage.Instance = new CacheEntityStorage();

        CacheGeneralStorage.Instance = new CacheGeneralStorage();

        ParserRegister.getInstance().init();
        BaseEventRegister.initialize();

        WindowTitle.getInstance().replace();

        CmdCallTypeCollection.instance = new CmdCallTypeCollection();
        TextColorCollection.instance = new TextColorCollection();
        UnicodeCharacterCollection.instance = new UnicodeCharacterCollection();
    }

    /**
     *
     * @param fmlInitializationEvent
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent fmlInitializationEvent)
    {
        IRecipes = new CraftItemWeb();

        IRecipes.registry();

        MinecraftForge.EVENT_BUS.register(TimeEvents.INSTANCE);
    }

    /**
     *
     * @param fmlPostInitializationEvent
     */
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent fmlPostInitializationEvent)
    {

    }

    /**
     *
     * @param fmlLoadCompleteEvent
     */
    @Mod.EventHandler
    public void onLoadComplete(FMLLoadCompleteEvent fmlLoadCompleteEvent)
    {

    }

    /**
     *
     * @param fmlServerStartingEvent
     */
    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent fmlServerStartingEvent)
    {
        CommandRegister.getInstance().registerCommands(fmlServerStartingEvent);
    }

    /**
     *
     * @param fmlServerStoppedEvent
     */
    @Mod.EventHandler
    public void serverStopped(FMLServerStoppedEvent fmlServerStoppedEvent)
    {
        CacheGeneralStorage.Instance.cleanActualCache();
        CacheGeneralStorage.Instance.cleanBufferCache();
    }

    /**
     *
     * @param fmlServerStoppingEvent
     */
    @Mod.EventHandler
    public static void onServerShutdown(FMLServerStoppingEvent fmlServerStoppingEvent)
    {

    }
}
