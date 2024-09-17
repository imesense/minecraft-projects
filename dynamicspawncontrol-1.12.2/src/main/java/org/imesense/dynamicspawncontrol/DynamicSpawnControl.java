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
import org.imesense.dynamicspawncontrol.technical.proxy.IProxy;

import java.io.File;

/**
 * Main class of modification
 */
@Mod(
    modid = DynamicSpawnControl.MODID,
    name = DynamicSpawnControl.NAME,
    version = DynamicSpawnControl.VERSION
)
public class DynamicSpawnControl
{
    /**
     * Modification ID
     */
    public static final String MODID = "dynamicspawncontrol";

    /**
     * Modification name
     */
    public static final String NAME = "Dynamic Spawn Control";

    /**
     * Minecraft version
     */
    public static final String VERSION = "1.12.2-14.23.5.2860";

    /**
     *
     */
    public static final String NAME_DIRECTORY = "DynamicsSpawnControl";

    /**
     *
     */
    public static final String CONFIG_FILE_EXTENSION = ".cfg";

    /**
     *
     */
    public static final String SCRIPT_FILE_EXTENSION = ".json";

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
        Log.createLogFile(globalDirectory.getPath() + File.separator + NAME_DIRECTORY);
        Log.writeDataToLogFile(Log.TypeLog[0], "Check debugger -> " + checkDebugger.IsRunDebugger);

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
        //
        Proxy.init(event);
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
        //
        Log.closeExecutor();
    }
}
