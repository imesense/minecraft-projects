package org.imesense.dynamicspawncontrol;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;

import org.imesense.dynamicspawncontrol.debug.CheckDebugger;
import org.imesense.dynamicspawncontrol.gameplay.EventGameplayManager;
import org.imesense.dynamicspawncontrol.gameplay.RegisterCommandsManager;
import org.imesense.dynamicspawncontrol.gameplay.events.OnUpdateTorchLogic;
import org.imesense.dynamicspawncontrol.technical.configs.IConfig;
import org.imesense.dynamicspawncontrol.technical.configs.SettingsLogFile;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.single.OnUpdateTimeWorld;
import org.imesense.dynamicspawncontrol.technical.gamestructures.Structures;
import org.imesense.dynamicspawncontrol.technical.network.MessageHandler;
import org.imesense.dynamicspawncontrol.technical.proxy.IProxy;
import org.imesense.dynamicspawncontrol.technical.worldcache.CacheConfig;
import org.imesense.dynamicspawncontrol.technical.worldcache.CacheEvents;
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
        public static final String NAME_DIRECTORY = "DynamicsSpawnControl";

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
        public static final String CONFIG_FILE_EXTENSION = ".cfg";

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
    static CheckDebugger checkDebugger;

    /**
     *
     */
    private static File globalDirectory = null;

    /**
     *
     */
    static CacheConfig cacheConfig = null;

    /**
     *
     */
    static CacheStorage cacheStorage = null;

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
     * Constructor
     */
    public DynamicSpawnControl()
    {
        //
        Instance = this;
    }

    /**
     * Preinitialize modification
     * 
     * @param event Preinitialization event
     */
    @EventHandler
    public synchronized void preInit(FMLPreInitializationEvent event)
    {
        //
        checkDebugger = new CheckDebugger();

        //
        globalDirectory = event.getModConfigurationDirectory();

        //
        Log.createLogFile(globalDirectory.getPath() + File.separator + STRUCT_FILES_DIRS.NAME_DIRECTORY);
        Log.writeDataToLogFile(Log.TypeLog[0], "Check debugger -> " + checkDebugger.IsRunDebugger);

        //
        MessageHandler.init();

        //
        cacheStorage = new CacheStorage("CacheStorage");

        //
        EventGameplayManager.registerClasses();

        //
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

        MinecraftForge.EVENT_BUS.register(new CacheEvents());
        MinecraftForge.EVENT_BUS.register(OnUpdateTimeWorld.ClassInstance);
    }

    /**
     * Postinitialize modification
     * 
     * @param event Postinitialization event
     */
    @EventHandler
    public synchronized void postInit(FMLPostInitializationEvent event)
    {
        //
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
        cacheConfig = new CacheConfig("onLoadComplete -> CacheConfig");
        cacheConfig.loadConfig(true);
    }

    /**
     * Server load action
     * 
     * @param event Server starting event
     */
    @EventHandler
    public synchronized void serverLoad(FMLServerStartingEvent event)
    {
        RegisterCommandsManager.registerCommands(event);
    }

    /**
     * Server stopped action
     * 
     * @param event Server stopped action
     */
    @EventHandler
    public synchronized void serverStopped(FMLServerStoppedEvent event)
    {
        Structures.StructuresCache.clean();

        Log.closeExecutor();
    }
}
