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
import org.imesense.dynamicspawncontrol.core.memory.Configuration;
import org.imesense.dynamicspawncontrol.core.memory.MemoryManager;
import org.imesense.dynamicspawncontrol.core.plugin.mod.time_control_mod_forge_1_12_2.network.MessageHandler;
import org.imesense.dynamicspawncontrol.core.register.command.CommandRegister;
import org.imesense.dynamicspawncontrol.core.register.config.ConfigRegister;
import org.imesense.dynamicspawncontrol.core.register.parser.ParserRegister;
import org.imesense.dynamicspawncontrol.core.register.pluginconfig.PluginConfigRegister;
import org.imesense.dynamicspawncontrol.core.register.worldgenerator.WorldGeneratorRegister;
import org.imesense.dynamicspawncontrol.core.worldcache.CacheGeneralStorage;
import org.imesense.dynamicspawncontrol.eventdescriptions.WindowTitle;
import org.imesense.dynamicspawncontrol.core.plugin.mod.webslinger_1_12_2_2_2_4.capability.WebSlingerCapability;
import org.imesense.dynamicspawncontrol.lootboxgenerator.GeneratorLootBoxInWorld;
import org.imesense.dynamicspawncontrol.recipes.CraftItemWeb;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.plugin.mod.webslinger_1_12_2_2_2_4.webbing.PlayerInWebMessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

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
     * @param event
     * @throws IllegalAccessException
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) throws IllegalAccessException
    {
        globalDirectory = event.getModConfigurationDirectory();

        Log.createLogFile(globalDirectory.getPath() +
                        File.separator + DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIRECTORY,
                UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG);

        File modFile = event.getSourceFile();
        String expectedName = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID + "-0.1.jar";

        Log.writeDataToLogFile(3, "Checking the name of the mod: " + modFile + " " + "required: " + expectedName);

        if (!modFile.getName().equals(expectedName) && !UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG)
        {
            Log.writeDataToLogFile(2, "Renaming a mod is prohibited.");
            Log.writeDataToLogFile(2, "You can make an official fork and rename it in its original form:");
            Log.writeDataToLogFile(2, "https://github.com/imesense/minecraft-projects");

            FMLCommonHandler.instance().exitJava(1, false);
        }

        try
        {
            String configFile = "mixins.dynamicspawncontrol.json";
            InputStream stream = getClass().getClassLoader().getResourceAsStream(configFile);

            if (stream != null)
            {
                Log.writeDataToLogFile(0, "File " + configFile + " found! Loading the mixins...");

                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder fileContent = new StringBuilder();

                String line;
                boolean isFirstLine = true;

                while ((line = reader.readLine()) != null)
                {
                    if (!isFirstLine)
                    {
                        fileContent.append("\n");
                    }
                    else
                    {
                        isFirstLine = false;
                    }
                    fileContent.append(line);
                }

                reader.close();

                Log.writeDataToLogFile(0, "File Contents " + configFile + ":\n" + fileContent.toString());

                Log.writeDataToLogFile(0, "Mixins uploaded successfully!");
                Log.writeDataToLogFile(0, "Mixins configuration loaded: " + configFile);
            }
            else
            {
                Log.writeDataToLogFile(0, "File " + configFile + " not found! Check the path and the name.");
            }
        }
        catch (Exception exception)
        {
            Log.writeDataToLogFile(0, "Download error Mixin: " + exception.getMessage());
            exception.printStackTrace();
        }

        Log.writeDataToLogFile(1, "Is running in IDE (based on logging level): " +
                (UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG ? "true" : "false"));

        MessageHandler.init();

        WebSlingerCapability.register();
        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("dynamicspawncontrol");
        PlayerInWebMessage.register(networkWrapper);

        ConfigRegister.getInstance().initializeConfigs();
        PluginConfigRegister.getInstance().initializeConfigs();

        WorldGeneratorRegister.getInstance().init(event);

        ParserRegister.getInstance().init();
        BaseEventRegister.initialize();

        WindowTitle.getInstance().replace();

        CmdCallTypeCollection.instance = new CmdCallTypeCollection();
        TextColorCollection.instance = new TextColorCollection();
        UnicodeCharacterCollection.instance = new UnicodeCharacterCollection();

        if (Configuration.cleanOnInit)
        {
            MemoryManager.cleanMemory();
        }
    }

    /**
     *
     * @param event
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        IRecipes = new CraftItemWeb();

        IRecipes.registry();

        if (Configuration.cleanOnInit)
        {
            MemoryManager.cleanMemory();
        }

        GeneratorLootBoxInWorld generatorLootBoxInWorld = new GeneratorLootBoxInWorld();

        MinecraftForge.EVENT_BUS.register(generatorLootBoxInWorld);
        generatorLootBoxInWorld.loadLootConfig();
    }

    /**
     *
     * @param event
     */
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        if (Configuration.cleanOnInit)
        {
            MemoryManager.cleanMemory();
        }
    }

    /**
     *
     * @param event
     */
    @Mod.EventHandler
    public void onLoadComplete(FMLLoadCompleteEvent event)
    {

    }

    /**
     *
     * @param event
     */
    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
        CommandRegister.getInstance().registerCommands(event);
    }

    /**
     *
     * @param event
     */
    @Mod.EventHandler
    public void serverStopped(FMLServerStoppedEvent event)
    {
        Log.writeDataToLogFile(0, "Cleaning up CacheGeneralStorage on server stop...");

        CacheGeneralStorage.getInstance().cleanActualCache();
        CacheGeneralStorage.getInstance().cleanBufferCache();

        Log.writeDataToLogFile(0, "CacheGeneralStorage cleaned up successfully.");
    }

    /**
     *
     * @param event
     */
    @Mod.EventHandler
    public static void onServerShutdown(FMLServerStoppingEvent event)
    {

    }
}
