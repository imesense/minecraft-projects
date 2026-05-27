package org.imesense.dynamicspawncontrol;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;

//import net.minecraftforge.fml.common.network.NetworkRegistry;
//import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.imesense.dynamicspawncontrol.bloodmoonmanager.*;
import org.imesense.dynamicspawncontrol.core.baseregister.BaseEventRegister;
import org.imesense.dynamicspawncontrol.core.doccompiler.ChangelogHTMLCompiler;
import org.imesense.dynamicspawncontrol.core.doccompiler.DocJSONToHTMLCompiler;
import org.imesense.dynamicspawncontrol.core.interfaces.IRecipes;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.logfile.LogManager;
import org.imesense.dynamicspawncontrol.core.logfile.MixinLoadLog;
import org.imesense.dynamicspawncontrol.core.logfile.TodoTracker;
import org.imesense.dynamicspawncontrol.core.memory.Configuration;
import org.imesense.dynamicspawncontrol.core.memory.MemoryManager;

import org.imesense.dynamicspawncontrol.core.mixinconfig.MixinConfigInitializer;
import org.imesense.dynamicspawncontrol.core.mixinconfig.MixinConfigScanner;
//import org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.register.RegisterSpawnerCraft;
//import org.imesense.dynamicspawncontrol.core.plugin.mod.time_control_mod_forge_1_12_2.network.MessageHandler;
import org.imesense.dynamicspawncontrol.core.register.command.CommandRegister;
import org.imesense.dynamicspawncontrol.core.register.config.ConfigRegister;
import org.imesense.dynamicspawncontrol.core.register.parser.ParserRegister;
import org.imesense.dynamicspawncontrol.core.register.pluginconfig.PluginConfigRegister;
import org.imesense.dynamicspawncontrol.core.register.worldgenerator.WorldGeneratorRegister;
import org.imesense.dynamicspawncontrol.core.worldcache.CacheGeneralStorage;
import org.imesense.dynamicspawncontrol.entity.register.EntityRegister;
//import org.imesense.dynamicspawncontrol.core.plugin.mod.webslinger_1_12_2_2_2_4.capability.WebSlingerCapability;
import org.imesense.dynamicspawncontrol.managercommands.CommandManager;
import org.imesense.dynamicspawncontrol.potion.ModPotions;
import org.imesense.dynamicspawncontrol.recipes.CraftItemWeb;
//import org.imesense.dynamicspawncontrol.core.plugin.mod.webslinger_1_12_2_2_2_4.webbing.PlayerInWebMessage;
import org.imesense.dynamicspawncontrol.satietymanager.*;

import java.io.File;

@Mod(
    modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID,
    name = DynamicSpawnControlStructure.STRUCT_INFO_MOD.NAME,
    version = DynamicSpawnControlStructure.STRUCT_INFO_MOD.FORGE_VERSION,
    dependencies = "required-after:fermiumbooter;",
    guiFactory = "org.imesense.dynamicspawncontrol.DynamicSpawnControlGuiFactory"
)
public final class DynamicSpawnControl
{
    // ./gradlew genIntellijRuns
    // ./gradlew build
    @Mod.Instance
    public static DynamicSpawnControl Instance;

    private static File globalDirectory = null;

    public static File getGlobalPathToConfigs()
    {
        return globalDirectory;
    }

    public static IRecipes IRecipes;

    public DynamicSpawnControl()
    {
        Instance = this;
    }

    //public static SimpleNetworkWrapper networkWrapper = null;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        globalDirectory = event.getModConfigurationDirectory();

        LogManager.createLogFile(globalDirectory.getPath() +
                        File.separator + DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIRECTORY,
                UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG);

        MixinConfigScanner.scanAndRegister("org.imesense.dynamicspawncontrol.core.mixinconfig");
        MixinConfigInitializer.initializeAllConfigs(globalDirectory.getPath() + File.separator +
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIRECTORY);

        LogManager.info("preInit: Basic registration phase - blocks/items/configs");

        TodoTracker.init(event);

        File modFile = event.getSourceFile();

        String expectedName = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID +
                DynamicSpawnControlStructure.STRUCT_INFO_MOD.RELEASE_VERSION;

        LogManager.info("Checking the name of the mod: " + modFile + " " + "required: " + expectedName);

        if (!modFile.getName().equals(expectedName) && !UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG)
        {
            LogManager.error("Renaming a mod is prohibited.");
            LogManager.error("You can make an official fork and rename it in its original form:");
            LogManager.error("https://github.com/imesense/minecraft-projects");

            FMLCommonHandler.instance().exitJava(1, false);
        }

        DocJSONToHTMLCompiler.createHTMLFile(globalDirectory.getPath() +
                File.separator + DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIRECTORY, UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG);

        ChangelogHTMLCompiler.createChangelogHTML(
                globalDirectory.getPath() + File.separator +
                        DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIRECTORY,
                UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG
        );

        try
        {
            MixinLoadLog.loadMixins();
        }
        catch (Exception exception)
        {
            LogManager.error("Mixin loading failed: " + exception.getMessage());
            exception.printStackTrace();
        }

        LogManager.warn("Is running in IDE (based on logging level): " +
                (UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG ? "true" : "false"));

        EntityRegister.init(() -> EntityRegister.create(this));
        EntityRegister entityRegister = EntityRegister.getInstance();
        entityRegister.preInitStartGame();

        //MessageHandler.init();

        //WebSlingerCapability.register();
        //networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("dynamicspawncontrol");
        //PlayerInWebMessage.register(networkWrapper);

        ConfigRegister.getInstance().initializeConfigs();
        PluginConfigRegister.getInstance().initializeConfigs();

        WorldGeneratorRegister.getInstance().init(event);

        BaseEventRegister.initialize();

        //RegisterSpawnerCraft.getInstance().preInit(event);

        MinecraftForge.EVENT_BUS.register(ModPotions.class);

        SatietyConfig.createFile(globalDirectory.getPath() +
                        File.separator + DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIRECTORY,
                UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG);

        BloodmoonEventHandler handler = new BloodmoonEventHandler();
        MinecraftForge.EVENT_BUS.register(handler);
        FMLCommonHandler.instance().bus().register(handler);
        PacketHandler.init();
    }

    public static boolean isBloodmoon() {
        if (BloodmoonHandler.INSTANCE == null) {
            return false;
        }
        return BloodmoonHandler.INSTANCE.isBloodmoonActive();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        LogManager.info("init: Core setup - recipes, events, network packets");

        IRecipes = new CraftItemWeb();

        IRecipes.registry();

        //RegisterSpawnerCraft.getInstance().init(event);

        ParserRegister.getInstance().init();

        // Merge THIS
        MinecraftForge.EVENT_BUS.register(new SatietyTooltipHandler());
        MinecraftForge.EVENT_BUS.register(new RespawnSatietyModule());
        MinecraftForge.EVENT_BUS.register(new SatietyFoodHandler());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        LogManager.info("postInit: Finalization - cross-mod integration");

        if (Configuration.isCleanOnInit())
        {
            MemoryManager.cleanMemory();
        }
    }

    @Mod.EventHandler
    public void onLoadComplete(FMLLoadCompleteEvent event)
    {

    }

    @Mod.EventHandler
    public void onServerLoad(FMLServerStartingEvent event)
    {
        CommandManager.registerCommands(event);

        CommandRegister.getInstance().registerCommands(event);

        try
        {
            File minecraftDir = FMLCommonHandler.instance().getMinecraftServerInstance().getDataDirectory();

            DocJSONToHTMLCompiler.createHTMLFile(minecraftDir.getAbsolutePath(), true);

            LogManager.info("HTML отчет по настройкам сущностей успешно сгенерирован при запуске сервера");
        }
        catch (Exception exception)
        {
            LogManager.error("Ошибка при генерации HTML отчета при запуске сервера: " + exception.getMessage());
        }

        event.registerServerCommand(new CommandSetHunger());

        event.registerServerCommand(new CommandBloodmoon());
    }

    @Mod.EventHandler
    public void handleIMCMessages(FMLInterModComms.IMCEvent event)
    {

    }

    @Mod.EventHandler
    public void onServerStopped(FMLServerStoppedEvent event)
    {
        LogManager.info("Cleaning up CacheGeneralStorage on server stop...");

        CacheGeneralStorage.getInstance().cleanActualCache();
        CacheGeneralStorage.getInstance().cleanBufferCache();

        LogManager.info("CacheGeneralStorage cleaned up successfully.");
    }

    @Mod.EventHandler
    public static void onServerShutdown(FMLServerStoppingEvent event)
    {

    }
}
