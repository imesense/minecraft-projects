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
 * OldSerpskiStalker
 * TODO: Legacy format.
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
    public static final class settingsGenerationBlockNetherRack
    {
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
    }

    /**
     *
     */
    public static final class settingsGenerationBlockMossyCobblestone
    {
        /**
         *
         */
        public static int BlockMossyCobblestoneChanceSpawn = 35;

        /**
         *
         */
        public static int GetBlockMossyCobblestoneMinHeight = 10;

        /**
         *
         */
        public static int GetBlockMossyCobblestoneMaxHeight = 45;
    }

    /**
     *
     */
    public static final class settingsGenerationBlockMonsterEgg
    {
        /**
         *
         */
        public static int BlockMonsterEggChanceSpawn = 10;

        /**
         *
         */
        public static int GetBlockMonsterEggMinHeight = 7;

        /**
         *
         */
        public static int GetBlockMonsterEggMaxHeight = 40;
    }

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
        //-' ***************************************************************************************************

        settingsGenerationBlockNetherRack.BlockNetherRackChanceSpawn = this.getConfigValueI(
                configuration,
                "Ore Netherrack Chance Spawn",
                "config_netherrack",
                settingsGenerationBlockNetherRack.BlockNetherRackChanceSpawn,
                1,
                100,
                "The chance of 'netherrack' appearing"
        );

        settingsGenerationBlockNetherRack.GetBlockNetherRackMinHeight = this.getConfigValueI(
                configuration,
                "Ore Netherrack Min Height",
                "config_netherrack",
                settingsGenerationBlockNetherRack.GetBlockNetherRackMinHeight,
                2,
                10,
                "The minimal height of 'netherrack' appearing"
        );

        settingsGenerationBlockNetherRack.GetBlockNetherRackMaxHeight = this.getConfigValueI(
                configuration,
                "Ore Netherrack Max Height",
                "config_netherrack",
                settingsGenerationBlockNetherRack.GetBlockNetherRackMaxHeight,
                10,
                20,
                "The maximal height of 'netherrack' appearing"
        );

        //-' ***************************************************************************************************

        settingsGenerationBlockMossyCobblestone.BlockMossyCobblestoneChanceSpawn = this.getConfigValueI(
                configuration,
                "Ore Mossy Cobblestone Chance Spawn",
                "config_mossy_cobblestone",
                settingsGenerationBlockMossyCobblestone.BlockMossyCobblestoneChanceSpawn,
                1,
                100,
                "The chance of 'mossy_cobblestone' appearing"
        );

        settingsGenerationBlockMossyCobblestone.GetBlockMossyCobblestoneMinHeight = this.getConfigValueI(
                configuration,
                "Ore Mossy Cobblestone Min Height",
                "config_mossy_cobblestone",
                settingsGenerationBlockMossyCobblestone.GetBlockMossyCobblestoneMinHeight,
                5,
                25,
                "The minimal height of 'mossy_cobblestone' appearing"
        );

        settingsGenerationBlockMossyCobblestone.GetBlockMossyCobblestoneMaxHeight = this.getConfigValueI(
                configuration,
                "Ore Mossy Cobblestone Max Height",
                "config_mossy_cobblestone",
                settingsGenerationBlockMossyCobblestone.GetBlockMossyCobblestoneMaxHeight,
                25,
                65,
                "The maximal height of 'mossy_cobblestone' appearing"
        );

        //-' ***************************************************************************************************

        settingsGenerationBlockMonsterEgg.BlockMonsterEggChanceSpawn = this.getConfigValueI(
                configuration,
                "Ore Monster Egg Chance Spawn",
                "config_monster_egg",
                settingsGenerationBlockMonsterEgg.BlockMonsterEggChanceSpawn,
                1,
                100,
                "The chance of 'monster_egg' appearing"
        );

        settingsGenerationBlockMonsterEgg.GetBlockMonsterEggMinHeight = this.getConfigValueI(
                configuration,
                "Ore Monster Egg Min Height",
                "config_monster_egg",
                settingsGenerationBlockMonsterEgg.GetBlockMonsterEggMinHeight,
                5,
                25,
                "The minimal height of 'monster_egg' appearing"
        );

        settingsGenerationBlockMonsterEgg.GetBlockMonsterEggMaxHeight = this.getConfigValueI(
                configuration,
                "Ore Monster Egg Max Height",
                "config_monster_egg",
                settingsGenerationBlockMonsterEgg.GetBlockMonsterEggMaxHeight,
                25,
                65,
                "The maximal height of 'monster_egg' appearing"
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
