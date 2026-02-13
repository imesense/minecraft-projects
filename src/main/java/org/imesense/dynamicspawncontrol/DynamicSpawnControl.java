package org.imesense.dynamicspawncontrol;

import net.minecraft.command.ICommand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.imesense.dynamicspawncontrol.core.baseregister.BaseEventRegister;
import org.imesense.dynamicspawncontrol.core.doccompiler.ChangelogHTMLCompiler;
import org.imesense.dynamicspawncontrol.core.doccompiler.DocJSONToHTMLCompiler;
import org.imesense.dynamicspawncontrol.core.interfaces.IRecipes;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.logfile.TodoTracker;
import org.imesense.dynamicspawncontrol.core.memory.Configuration;
import org.imesense.dynamicspawncontrol.core.memory.MemoryManager;

import org.imesense.dynamicspawncontrol.core.mixinconfig.MixinConfigInitializer;
import org.imesense.dynamicspawncontrol.core.mixinconfig.MixinConfigScanner;
import org.imesense.dynamicspawncontrol.core.mixinconfig.srparasites.SRParasitesMixinCreateConfig;
import org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.register.RegisterSpawnerCraft;
import org.imesense.dynamicspawncontrol.core.plugin.mod.time_control_mod_forge_1_12_2.network.MessageHandler;
import org.imesense.dynamicspawncontrol.core.register.command.CommandRegister;
import org.imesense.dynamicspawncontrol.core.register.config.ConfigRegister;
import org.imesense.dynamicspawncontrol.core.register.parser.ParserRegister;
import org.imesense.dynamicspawncontrol.core.register.pluginconfig.PluginConfigRegister;
import org.imesense.dynamicspawncontrol.core.register.worldgenerator.WorldGeneratorRegister;
import org.imesense.dynamicspawncontrol.core.script.processor.AIZombieHasShieldNBT;
import org.imesense.dynamicspawncontrol.core.threads.ThreadMonitor;
import org.imesense.dynamicspawncontrol.core.worldcache.CacheGeneralStorage;
import org.imesense.dynamicspawncontrol.entity.register.EntityRegister;
import org.imesense.dynamicspawncontrol.eventdescriptions.NewConceptTestEvent;
import org.imesense.dynamicspawncontrol.eventdescriptions.WindowTitle;
import org.imesense.dynamicspawncontrol.core.plugin.mod.webslinger_1_12_2_2_2_4.capability.WebSlingerCapability;
import org.imesense.dynamicspawncontrol.managercommands.CommandManager;
import org.imesense.dynamicspawncontrol.potion.ModPotions;
import org.imesense.dynamicspawncontrol.recipes.CraftItemWeb;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.plugin.mod.webslinger_1_12_2_2_2_4.webbing.PlayerInWebMessage;
import org.imesense.dynamicspawncontrol.satietymanager.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

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

    public static SimpleNetworkWrapper networkWrapper = null;

    private static final String[] MIXIN_CONFIGS =
    {
        "mixin.darkness.renderer.json",
        "mixin.fix.spawn.divinerpg.json",
        "mixin.ic2.exp.wireless.industry.json",
        "mixin.unlimited.enchantment.json",
        //"mixin.specialmobs.json",
        "mixin.srparasites.config.json",
        "mixin.minecraft.satiety.json"
    };

    private void loadMixins()
    {
        ClassLoader classLoader = DynamicSpawnControl.class.getClassLoader();

        Log.write(0, "Searching for mixin config files...");

        for (String config : MIXIN_CONFIGS)
        {
            Log.write(0, "Trying to load: " + config);

            try (InputStream inputStream = classLoader.getResourceAsStream(config))
            {
                if (inputStream == null)
                {
                    Log.write(2, "Mixin not found in resources: " + config);
                    continue;
                }

                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream)))
                {
                    String line;
                    StringBuilder stringBuilder = new StringBuilder();

                    while ((line = bufferedReader.readLine()) != null)
                    {
                        stringBuilder.append(line).append('\n');
                    }

                    Log.write(0, "Successfully loaded mixin: " + config);
                    Log.write(0, stringBuilder.toString());
                }
            }
            catch (Exception exception)
            {
                Log.write(2, "Error loading " + config + ": " + exception.getMessage());
                exception.printStackTrace();
            }
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        globalDirectory = event.getModConfigurationDirectory();

        Log.createLogFile(globalDirectory.getPath() +
                        File.separator + DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIRECTORY,
                UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG);

        MixinConfigScanner.scanAndRegister("org.imesense.dynamicspawncontrol.core.mixinconfig");
        MixinConfigInitializer.initializeAllConfigs(globalDirectory.getPath() + File.separator +
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIRECTORY);

        Log.write(0, "preInit: Basic registration phase - blocks/items/configs");

        TodoTracker.init(event);

        File modFile = event.getSourceFile();

        String expectedName = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID +
                DynamicSpawnControlStructure.STRUCT_INFO_MOD.RELEASE_VERSION;

        Log.write(3, "Checking the name of the mod: " + modFile + " " + "required: " + expectedName);

        if (!modFile.getName().equals(expectedName) && !UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG)
        {
            Log.write(2, "Renaming a mod is prohibited.");
            Log.write(2, "You can make an official fork and rename it in its original form:");
            Log.write(2, "https://github.com/imesense/minecraft-projects");

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
            this.loadMixins();
        }
        catch (Exception exception)
        {
            Log.write(2, "Mixin loading failed: " + exception.getMessage());
            exception.printStackTrace();
        }

        Log.write(1, "Is running in IDE (based on logging level): " +
                (UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG ? "true" : "false"));

        EntityRegister.init(() -> EntityRegister.create(this));
        EntityRegister entityRegister = EntityRegister.getInstance();
        entityRegister.preInitStartGame();

        MessageHandler.init();

        WebSlingerCapability.register();
        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("dynamicspawncontrol");
        PlayerInWebMessage.register(networkWrapper);

        ConfigRegister.getInstance().initializeConfigs();
        PluginConfigRegister.getInstance().initializeConfigs();

        WorldGeneratorRegister.getInstance().init(event);

        BaseEventRegister.initialize();

        WindowTitle.getInstance().replace();

        RegisterSpawnerCraft.getInstance().preInit(event);

        MinecraftForge.EVENT_BUS.register(ModPotions.class);

        SatietyConfig.createFile(globalDirectory.getPath() +
                        File.separator + DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIRECTORY,
                UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        Log.write(0, "init: Core setup - recipes, events, network packets");

        IRecipes = new CraftItemWeb();

        IRecipes.registry();

        RegisterSpawnerCraft.getInstance().init(event);

        ParserRegister.getInstance().init();

        // Merge THIS
        MinecraftForge.EVENT_BUS.register(new NewConceptTestEvent());
        MinecraftForge.EVENT_BUS.register(new AIZombieHasShieldNBT());
        MinecraftForge.EVENT_BUS.register(new SatietyTooltipHandler());
        MinecraftForge.EVENT_BUS.register(new RespawnSatietyModule());
        MinecraftForge.EVENT_BUS.register(new SatietyFoodHandler());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        Log.write(0, "postInit: Finalization - cross-mod integration");

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

            Log.write(0, "HTML отчет по настройкам сущностей успешно сгенерирован при запуске сервера");
        }
        catch (Exception exception)
        {
            Log.write(2, "Ошибка при генерации HTML отчета при запуске сервера: " + exception.getMessage());
        }

        event.registerServerCommand(new CommandSetHunger());
        event.registerServerCommand(new DivineRPGSummoner());

    }

    @Mod.EventHandler
    public void handleIMCMessages(FMLInterModComms.IMCEvent event)
    {

    }

    @Mod.EventHandler
    public void onServerStopped(FMLServerStoppedEvent event)
    {
        Log.write(0, "Cleaning up CacheGeneralStorage on server stop...");

        CacheGeneralStorage.getInstance().cleanActualCache();
        CacheGeneralStorage.getInstance().cleanBufferCache();

        Log.write(0, "CacheGeneralStorage cleaned up successfully.");
    }

    @Mod.EventHandler
    public static void onServerShutdown(FMLServerStoppingEvent event)
    {
        Log.shutdown();
        ThreadMonitor.getInstance().stop();
    }
}
