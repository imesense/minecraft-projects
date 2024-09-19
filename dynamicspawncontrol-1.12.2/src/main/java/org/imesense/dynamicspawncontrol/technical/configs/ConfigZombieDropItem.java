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
public final class ConfigZombieDropItem implements IConfig
{
    /**
     *
     */
    public static double BreakItem = 0.15;

    /**
     *
     */
    public static double HandItemDamageFactor = 0.85;

    /**
     *
     */
    public static double HeadDamageFactor = 0.9;

    /**
     *
     */
    public static double ChestDamageFactor = 0.9;

    /**
     *
     */
    public static double LegsDamageFactor = 0.9;

    /**
     *
     */
    public static double FeetDamageFactor = 0.9;

    /**
     *
     */
    public static double DamageSpreadFactor = 0.2;

    /**
     *
     * @param nameClass
     */
    public ConfigZombieDropItem(final String nameClass)
    {

    }

    @Override
    public void init(FMLPreInitializationEvent event, final String nameClass)
    {
        Log.writeDataToLogFile(Log.TypeLog[0], nameClass);

        ClientProxy.ConfigZombieDropItem = new Configuration(new File(DynamicSpawnControl.getGlobalPathToConfigs().getPath() +
                File.separator + DynamicSpawnControl.STRUCT_FILES_DIRS.NAME_DIRECTORY + File.separator +
                DynamicSpawnControl.STRUCT_FILES_DIRS.NAME_DIR_CONFIGS, "zombie_drop" +
                DynamicSpawnControl.STRUCT_FILES_EXTENSION.CONFIG_FILE_EXTENSION));

        this.read();
    }

    /**
     *
     * @param configuration
     */
    @Override
    public void readProperties(Configuration configuration)
    {
        BreakItem =
                configuration.getFloat
                        ("Chance item break", "zombie_drop", (float)BreakItem, 0, 1.0f,
                                "The parameter is responsible for the chance that the item will be destroyed when dropped.");

        HandItemDamageFactor =
                configuration.getFloat
                        ("Condition of hand item", "zombie_drop", (float)HandItemDamageFactor, 0, 1.0f,
                                "The parameter is responsible for the state of the weapon when it drops.");

        HeadDamageFactor =
                configuration.getFloat
                        ("Condition of headgear", "zombie_drop", (float)HeadDamageFactor, 0, 1.0f,
                                "The parameter is responsible for the state of the headdress when it falls out.");

        ChestDamageFactor =
                configuration.getFloat
                        ("Condition of the bib", "zombie_drop", (float)ChestDamageFactor, 0, 1.0f,
                                "The parameter is responsible for the state of the chest plate when it drops.");

        LegsDamageFactor =
                configuration.getFloat
                        ("Condition of the leggings", "zombie_drop", (float)LegsDamageFactor, 0, 1.0f,
                                "The parameter is responsible for the state of the leggings when it falls.");

        FeetDamageFactor =
                configuration.getFloat
                        ("Condition of boots", "zombie_drop", (float)FeetDamageFactor, 0, 1.0f,
                                "The parameter is responsible for the state of the boots when they fall.");

        DamageSpreadFactor =
                configuration.getFloat("Damage spread factor", "zombie_drop", (float)DamageSpreadFactor, 0, 1.0f,
                        "The parameter controls the spread of damage when an item drops.");
    }

    /**
     *
     */
    @Override
    public void read()
    {
        Configuration configuration = ClientProxy.ConfigZombieDropItem;

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
