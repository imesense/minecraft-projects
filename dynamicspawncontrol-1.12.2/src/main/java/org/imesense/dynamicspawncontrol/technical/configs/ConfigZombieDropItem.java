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
    public static float BreakItem = 0.15f;

    /**
     *
     */
    public static float HandItemDamageFactor = 0.85f;

    /**
     *
     */
    public static float HeadDamageFactor = 0.9f;

    /**
     *
     */
    public static float ChestDamageFactor = 0.9f;

    /**
     *
     */
    public static float LegsDamageFactor = 0.9f;

    /**
     *
     */
    public static float FeetDamageFactor = 0.9f;

    /**
     *
     */
    public static float DamageSpreadFactor = 0.2f;

    /**
     *
     * @param nameClass
     */
    public ConfigZombieDropItem(final String nameClass)
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
        Log.writeDataToLogFile(0, nameClass);

        ClientProxy.ConfigZombieDropItem = this.createConfiguration("zombie_drop");

        this.read();
    }

    /**
     *
     * @param configuration
     */
    @Override
    public void readProperties(Configuration configuration)
    {
        BreakItem = this.getConfigValueF(
                configuration,
                "Chance item break",
                "zombie_drop",
                BreakItem,
                0f, 1.0f,
                "The parameter is responsible for the chance that the item will be destroyed when dropped.");

        HandItemDamageFactor = this.getConfigValueF(
                configuration,
                "Condition of hand item",
                "zombie_drop",
                HandItemDamageFactor,
                0f, 1.0f,
                "The parameter is responsible for the state of the weapon when it drops.");

        HeadDamageFactor = this.getConfigValueF(
                configuration,
                "Condition of headgear",
                "zombie_drop",
                HeadDamageFactor,
                0f, 1.0f,
                "The parameter is responsible for the state of the headdress when it falls out.");

        ChestDamageFactor = this.getConfigValueF(
                configuration,
                "Condition of the bib",
                "zombie_drop",
                ChestDamageFactor,
                0f, 1.0f,
                "The parameter is responsible for the state of the chest plate when it drops.");

        LegsDamageFactor = this.getConfigValueF(
                configuration,
                "Condition of the leggings",
                "zombie_drop",
                LegsDamageFactor,
                0f, 1.0f,
                "The parameter is responsible for the state of the leggings when it falls.");

        FeetDamageFactor = this.getConfigValueF(
                configuration,
                "Condition of boots",
                "zombie_drop",
                FeetDamageFactor,
                0f, 1.0f,
                "The parameter is responsible for the state of the boots when they fall.");

        DamageSpreadFactor = this.getConfigValueF(
                configuration,
                "Damage spread factor",
                "zombie_drop",
                DamageSpreadFactor,
                0f, 1.0f,
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
