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
import org.imesense.dynamicspawncontrol.ai.spider.util.event.WebAttackEvent;
import org.imesense.dynamicspawncontrol.ai.spider.util.attackweb.WebSlingerCapability;
import org.imesense.dynamicspawncontrol.ai.zombie.event.BreakTorchEvent;
import org.imesense.dynamicspawncontrol.debug.CheckDebugger;
import org.imesense.dynamicspawncontrol.gameplay.recipes.IRecipes;
import org.imesense.dynamicspawncontrol.gameplay.recipes.CraftItemWeb;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.primitive.OnUpdateTimeWorld;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.primitive.OnWindowTitle;
import org.imesense.dynamicspawncontrol.technical.register.RegisterConfigClass;
import org.imesense.dynamicspawncontrol.technical.register.RegisterGameplayClass;
import org.imesense.dynamicspawncontrol.technical.register.RegisterCommandClass;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.gamestructure.Structure;
import org.imesense.dynamicspawncontrol.technical.register.RegisterTechnicalClass;
import org.imesense.dynamicspawncontrol.technical.network.MessageHandler;
import org.imesense.dynamicspawncontrol.technical.network.PlayerInWebMessage;
import org.imesense.dynamicspawncontrol.technical.parser.GeneralStorageData;
import org.imesense.dynamicspawncontrol.technical.parser.ParserGenericJsonScript;
import org.imesense.dynamicspawncontrol.technical.parser.ParserManager;
import org.imesense.dynamicspawncontrol.technical.proxy.IProxy;
import org.imesense.dynamicspawncontrol.technical.worldcache.Cache;
import org.imesense.dynamicspawncontrol.technical.worldcache.CacheStorage;

import java.io.File;

/**
 * Main class of modification
 */
@Mod(
    modid = ProjectStructure.STRUCT_INFO_MOD.MOD_ID,
    name = ProjectStructure.STRUCT_INFO_MOD.NAME,
    version = ProjectStructure.STRUCT_INFO_MOD.VERSION
)
public class DynamicSpawnControl
{
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
        Log.createLogFile(globalDirectory.getPath() + File.separator + ProjectStructure.STRUCT_FILES_DIRS.NAME_DIRECTORY, CheckDebugger.instance.IsRunDebugger);
        Log.writeDataToLogFile(1, "Debugger is running: " + (CheckDebugger.instance.IsRunDebugger ? "true" : "false"));

        //
        MessageHandler.init();

        WebSlingerCapability.register();
        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("dynamicspawncontrol");
        PlayerInWebMessage.register(networkWrapper);

        //
        RegisterConfigClass.initializeConfigs();

        //
        generalStorageData = new GeneralStorageData();

        //
        CacheStorage.instance = new CacheStorage();

        //
        Cache.instance = new Cache();

        //
        ParserGenericJsonScript.setRulePath(event.getModConfigurationDirectory());

        //
        RegisterTechnicalClass.registerClasses();

        //
        OnWindowTitle.replace();

        //
        RegisterGameplayClass.registerClasses();

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

        //-' TODO: перенести это в отдельную инициализацию
        MinecraftForge.EVENT_BUS.register(new WebAttackEvent());
        MinecraftForge.EVENT_BUS.register(new BreakTorchEvent());
    }

    /**
     * Load complete action
     * 
     * @param event Load complete event
     */
    @Mod.EventHandler
    public synchronized void onLoadComplete(FMLLoadCompleteEvent event)
    {
        ParserGenericJsonScript.readRules();

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
        RegisterCommandClass.registerCommands(event);
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

        Structure.STRUCTURES_CACHE.clean();
    }
}
