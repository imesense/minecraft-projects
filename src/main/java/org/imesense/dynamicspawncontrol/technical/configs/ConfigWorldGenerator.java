package org.imesense.dynamicspawncontrol.technical.configs;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControl;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.proxy.ClientProxy;

import java.io.File;

/**
 *
 */
public final class ConfigWorldGenerator implements IConfig
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public static int BlockNetherRackChanceSpawn = 20;

    /**
     *
     */
    public static int GetBlockNetherRackMinHeight = 5;

    /**
     *
     */
    public static int GetBlockNetherRackMaxHeight = 20;

    /**
     *
     */
    public ConfigWorldGenerator()
    {
        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;

        CodeGenericUtils.printInitClassToLog(ConfigWorldGenerator.class);
    }

    /**
     *
     * @param event
     */
    @Override
    public void init(FMLPreInitializationEvent event)
    {
        ClientProxy.ConfigOreGeneratorFile = this.createConfiguration("ore_generator");

        this.read();
    }

    /**
     *
     * @param configuration
     */
    @Override
    public void readProperties(Configuration configuration)
    {
        BlockNetherRackChanceSpawn = this.getConfigValueI(
                configuration,
                "Ore Netherrack Chance Spawn",
                "ore_generator_over_world",
                BlockNetherRackChanceSpawn,
                1,
                100,
                "The chance of 'netherrack' appearing"
        );

        GetBlockNetherRackMinHeight = this.getConfigValueI(
                configuration,
                "Ore Netherrack Min Height",
                "ore_generator_over_world",
                GetBlockNetherRackMinHeight,
                2,
                10,
                "The minimal height of 'netherrack' appearing"
        );

        GetBlockNetherRackMaxHeight = this.getConfigValueI(
                configuration,
                "Ore Netherrack Max Height",
                "ore_generator_over_world",
                GetBlockNetherRackMaxHeight,
                10,
                20,
                "The maximal height of 'netherrack' appearing"
        );
    }

    /**
     *
     */
    @Override
    public void read()
    {
        Configuration configuration = ClientProxy.ConfigOreGeneratorFile;

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
