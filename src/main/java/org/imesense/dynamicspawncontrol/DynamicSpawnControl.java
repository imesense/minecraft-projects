package org.imesense.dynamicspawncontrol;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.imesense.dynamicspawncontrol.ai.spider.utils.event.EventHandler;
import org.imesense.dynamicspawncontrol.ai.spider.utils.attackweb.WebSlingerCapability;
import org.imesense.dynamicspawncontrol.debug.CheckDebugger;
import org.imesense.dynamicspawncontrol.gameplay.recipes.IRecipes;
import org.imesense.dynamicspawncontrol.gameplay.recipes.CraftItemWeb;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.primitive.OnUpdateTimeWorld;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.primitive.OnWindowTitle;
import org.imesense.dynamicspawncontrol.technical.initializer.RegisterCfgClasses;
import org.imesense.dynamicspawncontrol.technical.initializer.RegisterGameplayClasses;
import org.imesense.dynamicspawncontrol.technical.initializer.RegisterCommandsClasses;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.gamestructures.Structures;
import org.imesense.dynamicspawncontrol.technical.initializer.RegisterTechnicalClasses;
import org.imesense.dynamicspawncontrol.technical.network.MessageHandler;
import org.imesense.dynamicspawncontrol.technical.network.PlayerInWebMessage;
import org.imesense.dynamicspawncontrol.technical.parsers.GeneralStorageData;
import org.imesense.dynamicspawncontrol.technical.parsers.ParserGenericJsonScripts;
import org.imesense.dynamicspawncontrol.technical.parsers.ParserManager;
import org.imesense.dynamicspawncontrol.technical.proxy.IProxy;
import org.imesense.dynamicspawncontrol.technical.worldcache.Cache;
import org.imesense.dynamicspawncontrol.technical.worldcache.CacheStorage;

import java.io.File;

/**
 * Main class of modification
 */
@Mod(
    modid = DynamicSpawnControl.STRUCT_INFO_MOD.MOD_ID,
    name = DynamicSpawnControl.STRUCT_INFO_MOD.NAME,
    version = DynamicSpawnControl.STRUCT_INFO_MOD.VERSION
)
public class DynamicSpawnControl
{
    /**
     *
     */
    public static final class STRUCT_INFO_MOD
    {
        /**
         * Modification ID
         */
        public static final String MOD_ID = "dynamicspawncontrol";

        /**
         * Modification name
         */
        public static final String NAME = "Dynamic Spawn Control";

        /**
         * Minecraft version
         */
        public static final String VERSION = "1.12.2-14.23.5.2860";
    }

    /**
     *
     */
    public static final class STRUCT_FILES_DIRS
    {
        /**
         *
         */
        public static final String NAME_DIRECTORY = "DynamicSpawnControl";

        /**
         *
         */
        public static final String NAME_DIR_CONFIGS = "configs";

        /**
         *
         */
        public static final String NAME_DIR_SCRIPTS = "scripts";

        /**
         *
         */
        public static final String NAME_DIR_SINGLE_SCRIPTS = "single_scripts";

        /**
         *
         */
        public static final String NAME_DIR_LOGS = "logs";

        /**
         *
         */
        public static final String NAME_DIR_CACHE = "cache";
    }

    /**
     *
     */
    public static final class STRUCT_FILES_EXTENSION
    {
        /**
         *
         */
        public static final String SCRIPT_FILE_EXTENSION = ".json";

        /**
         *
         */
        public static final String LOG_FILE_EXTENSION = ".txt";
    }

    /**
     * Main class instance
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
        //
        return globalDirectory;
    }

    /**
     * Sided proxy settings
     */
    @SidedProxy(
        clientSide = "org.imesense.dynamicspawncontrol.technical.proxy.ClientProxy",
        serverSide = "org.imesense.dynamicspawncontrol.technical.proxy.ServerProxy"
    )
    public static IProxy Proxy;

    /**
     *
     */
    public static IRecipes Recipes;

    /**
     * Constructor
     */
    public DynamicSpawnControl()
    {
        //
        Instance = this;
    }

    /**
     *
     */
    public GeneralStorageData generalStorageData = null;

    /**
     *
     */
    public static SimpleNetworkWrapper networkWrapper = null;

    /**
     * Preinitialize modification
     * 
     * @param event Preinitialization event
     */
    @Mod.EventHandler
    public synchronized void preInit(FMLPreInitializationEvent event)
    {
        //
        CheckDebugger.instance = new CheckDebugger();

        //
        globalDirectory = event.getModConfigurationDirectory();

        //
        Log.createLogFile(globalDirectory.getPath() + File.separator + STRUCT_FILES_DIRS.NAME_DIRECTORY, CheckDebugger.instance.IsRunDebugger);
        Log.writeDataToLogFile(1, "Debugger is running: " + (CheckDebugger.instance.IsRunDebugger ? "true" : "false"));

        //
        MessageHandler.init();

        WebSlingerCapability.register();
        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("dynamicspawncontrol");
        PlayerInWebMessage.register(networkWrapper);

        //
        RegisterCfgClasses.initializeConfigs();

        //
        generalStorageData = new GeneralStorageData();

        //
        CacheStorage.instance = new CacheStorage();

        //
        Cache.instance = new Cache();

        //
        ParserGenericJsonScripts.setRulePath(event.getModConfigurationDirectory());

        //
        RegisterTechnicalClasses.registerClasses();

        //
        OnWindowTitle.replace();

        //
        RegisterGameplayClasses.registerClasses();

        //
        Proxy.preInit(event);
    }

    /**
     * Initialize modification
     * 
     * @param event Initialization event
     */
    @Mod.EventHandler
    public synchronized void init(FMLInitializationEvent event)
    {
        //
        Proxy.init(event);

        //
        Recipes = new CraftItemWeb();

        //
        Recipes.registry();

        MinecraftForge.EVENT_BUS.register(OnUpdateTimeWorld.INSTANCE);
    }

    /**
     * Postinitialize modification
     * 
     * @param event Postinitialization event
     */
    @Mod.EventHandler
    public synchronized void postInit(FMLPostInitializationEvent event)
    {
        //
        Proxy.postInit(event);

        MinecraftForge.EVENT_BUS.register( new EventHandler());
    }

    /**
     * Load complete action
     * 
     * @param event Load complete event
     */
    @Mod.EventHandler
    public synchronized void onLoadComplete(FMLLoadCompleteEvent event)
    {
        ParserGenericJsonScripts.readRules();

        ParserManager.init();
    }

    /**
     * Server load action
     * 
     * @param event Server starting event
     */
    @Mod.EventHandler
    public synchronized void serverLoad(FMLServerStartingEvent event)
    {
        RegisterCommandsClasses.registerCommands(event);
    }

    /**
     * Server stopped action
     * 
     * @param event Server stopped action
     */
    @Mod.EventHandler
    public synchronized void serverStopped(FMLServerStoppedEvent event)
    {
        Cache.instance.cleanActualCache();
        Cache.instance.cleanBufferCache();

        Structures.STRUCTURES_CACHE.clean();
    }
}
