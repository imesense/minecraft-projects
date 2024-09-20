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
public final class ConfigRenderNight implements IConfig
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
     */
    @Override
    public void init(FMLPreInitializationEvent event)
    {
        ClientProxy.ConfigNights = this.createConfiguration("nights");

        this.read();
    }

    /**
     *
     * @param configuration
     */
    @Override
    public void readProperties(Configuration configuration)
    {
        DarknessOverWorld = this.getConfigValueB(
                configuration,
                "Darkness OverWorld",
                "nights",
                DarknessOverWorld,
                "Darker illumination of the light from the blocks. (Over World)"
        );

        DarknessNether = this.getConfigValueB(
                configuration,
                "Darkness Nether",
                "nights",
                DarknessNether,
                "Darker illumination of the light from the blocks. (Nether)"
        );

        DarknessEnd = this.getConfigValueB(
                configuration,
                "Darkness End",
                "nights",
                DarknessEnd,
                "Darker illumination of the light from the blocks. (End)"
        );

        DarknessDefault = this.getConfigValueB(
                configuration,
                "Darkness Default",
                "nights",
                DarknessDefault,
                "Darker skies (Default)"
        );

        DarknessSkyLess = this.getConfigValueB(
                configuration,
                "Darkness Sky Less",
                "nights",
                DarknessSkyLess,
                "Darker sky less (Default)"
        );

        DarknessNetherFog = this.getConfigValueB(
                configuration,
                "Darkness Nether Fog",
                "nights",
                DarknessNetherFog,
                "The darkness of the fog in the lower world"
        );

        DarknessEndFog = this.getConfigValueB(
                configuration,
                "Darkness End Fog",
                "nights",
                DarknessEndFog,
                "Dark fog in the ender world"
        );

        IgnoreMoonLight = this.getConfigValueB(
                configuration,
                "Ignore Moon Light",
                "nights",
                IgnoreMoonLight,
                "Ignoring the phases of the moon. It's always dark"
        );

        InvertBlacklist = this.getConfigValueB(
                configuration,
                "Invert Black list",
                "nights",
                InvertBlacklist,
                "Inverting the list, ignoring the worlds where darkness should be disabled"
        );

        BlacklistByID = this.getIntListConfigValue(
                configuration,
                "Black List By ID",
                "nights",
                BlacklistByID,
                "A leaf representing ignoring worlds where darkness should not be present"
        );

        MoonPhaseFactors = this.getDoubleListConfigValue(
                configuration,
                "Moon Phase Factors",
                "nights",
                MoonPhaseFactors,
                "The illumination factor of the night from the phases of the moons"
        );

        BlacklistByName = this.getStringListConfigValue(
                configuration,
                "Black list By Name",
                "nights",
                BlacklistByName,
                "A leaf representing ignoring worlds where darkness should not be present"
        );
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
