package org.imesense.dynamicspawncontrol;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.imesense.dynamicspawncontrol.core.api.IRecipes;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.register.*;
import org.imesense.dynamicspawncontrol.plugin.webslinger_1_12_2_2_2_4.capability.EventHandler;
import org.imesense.dynamicspawncontrol.plugin.webslinger_1_12_2_2_2_4.capability.WebSlingerCapability;
import org.imesense.dynamicspawncontrol.ai.zombie.event.OnBreakTorchEvent;
import org.imesense.dynamicspawncontrol.recipes.CraftItemWeb;
import org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.TimeEvents;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.primitive.OnWindowTitle;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.technical.gamestructure.Structure;
import org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.network.*;
import org.imesense.dynamicspawncontrol.plugin.webslinger_1_12_2_2_2_4.webbing.PlayerInWebMessage;
import org.imesense.dynamicspawncontrol.technical.parser.GeneralStorageData;
import org.imesense.dynamicspawncontrol.technical.parser.ParserGenericJsonScript;
import org.imesense.dynamicspawncontrol.technical.parser.ParserManager;
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
    public synchronized void preInit(FMLPreInitializationEvent fmlPreInitializationEvent) throws IllegalAccessException
    {
        globalDirectory = fmlPreInitializationEvent.getModConfigurationDirectory();

        UniqueField uniqueField = new UniqueField();

        Log.createLogFile(globalDirectory.getPath() + File.separator + DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIRECTORY, UniqueField.IDEA_RT);
        Log.writeDataToLogFile(1, "Launching from Intellij Idea: " + (UniqueField.IDEA_RT ? "true" : "false"));
        Log.writeDataToLogFile(0, "Object create [UniqueField]: " + uniqueField.hashCode());

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

        OnWindowTitle.replace();

        RegisterGameplayClass.registerClasses();

        RegisterOreGenerator.init(fmlPreInitializationEvent);
    }

    /**
     *
     * @param fmlInitializationEvent
     */
    @Mod.EventHandler
    public synchronized void init(FMLInitializationEvent fmlInitializationEvent)
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
    public synchronized void postInit(FMLPostInitializationEvent fmlPostInitializationEvent)
    {
        //-' TODO: перенести это в отдельную инициализацию
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        MinecraftForge.EVENT_BUS.register(new OnBreakTorchEvent());
    }

    /**
     *
     * @param fmlLoadCompleteEvent
     */
    @Mod.EventHandler
    public synchronized void onLoadComplete(FMLLoadCompleteEvent fmlLoadCompleteEvent)
    {
        ParserGenericJsonScript.readRules();

        ParserManager.init();
    }

    /**
     *
     * @param fmlServerStartingEvent
     */
    @Mod.EventHandler
    public synchronized void serverLoad(FMLServerStartingEvent fmlServerStartingEvent)
    {
        RegisterCommandClass.registerCommands(fmlServerStartingEvent);
    }

    /**
     *
     * @param fmlServerStoppedEvent
     */
    @Mod.EventHandler
    public synchronized void serverStopped(FMLServerStoppedEvent fmlServerStoppedEvent)
    {
        Cache.Instance.cleanActualCache();
        Cache.Instance.cleanBufferCache();

        Structure.STRUCTURES_CACHE.clean();
    }
}
