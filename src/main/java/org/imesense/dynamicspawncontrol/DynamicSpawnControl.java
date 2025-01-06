package org.imesense.dynamicspawncontrol;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.imesense.dynamicspawncontrol.ai.spider.event.OnEventAvoidLight;
import org.imesense.dynamicspawncontrol.core.api.IRecipes;
import org.imesense.dynamicspawncontrol.core.collection.CmdCallTypeCollection;
import org.imesense.dynamicspawncontrol.core.collection.TextColorCollection;
import org.imesense.dynamicspawncontrol.core.collection.UnicodeCharacterCollection;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.register.*;
import org.imesense.dynamicspawncontrol.plugin.webslinger_1_12_2_2_2_4.capability.EventHandler;
import org.imesense.dynamicspawncontrol.plugin.webslinger_1_12_2_2_2_4.capability.WebSlingerCapability;
import org.imesense.dynamicspawncontrol.ai.zombie.event.OnEventBreakTorch;
import org.imesense.dynamicspawncontrol.recipes.CraftItemWeb;
import org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.TimeEvents;
import org.imesense.dynamicspawncontrol.event.OnEventWindowTitle;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.worldstructure.Structure;
import org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.network.*;
import org.imesense.dynamicspawncontrol.plugin.webslinger_1_12_2_2_2_4.webbing.PlayerInWebMessage;
import org.imesense.dynamicspawncontrol.parser.algo.GeneralStorageData;
import org.imesense.dynamicspawncontrol.parser.algo.ParserGenericJsonScript;
import org.imesense.dynamicspawncontrol.core.register.RegisterParserManager;
import org.imesense.dynamicspawncontrol.core.worldcache.Cache;
import org.imesense.dynamicspawncontrol.core.worldcache.CacheStorage;

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
    public GeneralStorageData GeneralStorageData = null;

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

        Log.writeDataToLogFile(1, "Is running in IDE (based on logging level): " +
                (UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG ? "true" : "false"));

        Log.writeDataToLogFile(0, "Object create [UniqueField]: " +
                UniqueField.getInstance().hashCode());

        MessageHandler.init();

        WebSlingerCapability.register();
        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("dynamicspawncontrol");
        PlayerInWebMessage.register(networkWrapper);

        RegisterConfigClass.initializeConfigs();

        GeneralStorageData = new GeneralStorageData();

        CacheStorage.Instance = new CacheStorage();

        Cache.Instance = new Cache();

        ParserGenericJsonScript.setRulePath(fmlPreInitializationEvent.getModConfigurationDirectory());

        RegisterTechnicalClass.registerClasses();

        OnEventWindowTitle.replace();

        RegisterGameplayClass.registerClasses();

        RegisterOreGenerator.init(fmlPreInitializationEvent);

        CmdCallTypeCollection.instance = new CmdCallTypeCollection();
        TextColorCollection.instance = new TextColorCollection();
        UnicodeCharacterCollection.instance = new UnicodeCharacterCollection();

        RegisterAIClass.registerClasses();
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
        ParserGenericJsonScript.readRules();

        RegisterParserManager.init();
    }

    /**
     *
     * @param fmlServerStartingEvent
     */
    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent fmlServerStartingEvent)
    {
        RegisterCommandClass.registerCommands(fmlServerStartingEvent);
    }

    /**
     *
     * @param fmlServerStoppedEvent
     */
    @Mod.EventHandler
    public void serverStopped(FMLServerStoppedEvent fmlServerStoppedEvent)
    {
        Cache.Instance.cleanActualCache();
        Cache.Instance.cleanBufferCache();

        Structure.STRUCTURES_CACHE.clean();
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
