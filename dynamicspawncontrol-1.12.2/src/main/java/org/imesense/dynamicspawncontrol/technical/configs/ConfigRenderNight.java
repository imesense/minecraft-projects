package org.imesense.dynamicspawncontrol.technical.configs;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControl;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.proxy.ClientProxy;

import java.io.File;

/**
 *
 */
public class ConfigRenderNight implements IConfig
{
    /**
     *
     */
    public static boolean DarknessOverWorld = true;

    /**
     *
     */
    public static boolean DarknessNether = true;

    /**
     *
     */
    public static boolean DarknessEnd = true;

    /**
     *
     */
    public static boolean DarknessDefault = true;

    /**
     *
     */
    public static boolean DarknessSkyLess = true;

    /**
     *
     */
    public static boolean DarknessNetherFog = true;

    /**
     *
     */
    public static boolean DarknessEndFog = true;

    /**
     *
     */
    public static boolean IgnoreMoonLight = false;

    /**
     *
     */
    public static boolean InvertBlacklist = false;

    /**
     *
     */
    public static int[] BlacklistByID = {};

    /**
     *
     */
    public static double[] MoonPhaseFactors = { 0.6, 0.4, 0.3, 0.2, 0.0, 0.1, 0.2, 0.4 };

    /**
     *
     */
    public static String[] BlacklistByName = {};

    /**
     *
     * @param nameClass
     */
    public ConfigRenderNight(final String nameClass)
    {

    }

    /**
     *
     * @param event
     * @param nameClass
     */
    @Override
    public void init(FMLPreInitializationEvent event, final String nameClass)
    {
        Log.writeDataToLogFile(Log.TypeLog[0], nameClass);

        ClientProxy.ConfigNights = new Configuration(new File(DynamicSpawnControl.getGlobalPathToConfigs().getPath() +
                File.separator + DynamicSpawnControl.STRUCT_FILES_DIRS.NAME_DIRECTORY + File.separator +
                DynamicSpawnControl.STRUCT_FILES_DIRS.NAME_DIR_CONFIGS, "nights" + DynamicSpawnControl.STRUCT_FILES_EXTENSION.CONFIG_FILE_EXTENSION));

        this.read();
    }

    /**
     *
     * @param configuration
     */
    @Override
    public void readProperties(Configuration configuration)
    {
        DarknessOverWorld =
                configuration.getBoolean
                        ("Darkness OverWorld", "nights", DarknessOverWorld,
                                "Darker illumination of the light from the blocks. (Over World)");

        DarknessNether =
                configuration.getBoolean
                        ("Darkness Nether", "nights", DarknessNether,
                                "Darker illumination of the light from the blocks. (Nether)");

        DarknessEnd =
                configuration.getBoolean
                        ("Darkness End", "nights", DarknessEnd,
                                "Darker illumination of the light from the blocks. (End)");

        DarknessDefault =
                configuration.getBoolean
                        ("Darkness Default", "nights", DarknessDefault,
                                "Darker skies (Default)");

        DarknessSkyLess =
                configuration.getBoolean
                        ("Darkness Sky Less", "nights", DarknessSkyLess,
                                "Darker sky less (Default)");

        DarknessNetherFog =
                configuration.getBoolean
                        ("Darkness Nether Fog", "nights", DarknessNetherFog,
                                "The darkness of the fog in the lower world");

        DarknessEndFog =
                configuration.getBoolean
                        ("Darkness End Fog", "nights", DarknessEndFog,
                                "Dark fog in the ender world");

        IgnoreMoonLight =
                configuration.getBoolean
                        ("Ignore Moon Light", "nights", IgnoreMoonLight,
                                "Ignoring the phases of the moon. It's always dark");

        InvertBlacklist =
                configuration.getBoolean
                        ("Invert Black list", "nights", InvertBlacklist,
                                "Inverting the list, ignoring the worlds where darkness should be disabled");

        BlacklistByID =
                configuration.get
                        ("nights", "Black List By ID", BlacklistByID,
                                "A leaf representing ignoring worlds where darkness should not be present").getIntList();

        MoonPhaseFactors =
                configuration.get
                        ("nights", "Moon Phase Factors", MoonPhaseFactors,
                                "The illumination factor of the night from the phases of the moons").getDoubleList();

        BlacklistByName =
                configuration.get
                        ("nights", "Black list By Name", BlacklistByName,
                                "A leaf representing ignoring worlds where darkness should not be present").getStringList();
    }

    /**
     *
     */
    @Override
    public void read()
    {
        Configuration configuration = ClientProxy.ConfigNights;

        try
        {
            configuration.load();

            this.readProperties(configuration);
        }
        finally
        {
            if (configuration.hasChanged())
            {
                configuration.save();
            }
        }
    }
}
