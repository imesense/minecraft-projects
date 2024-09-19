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
public final class ConfigPlayer implements IConfig
{
    /**
     *
     */
    public static int ProtectRespawnPlayerRadius = 25;

    /**
     *
     * @param nameClass
     */
    public ConfigPlayer(final String nameClass)
    {

    }

    @Override
    public void init(FMLPreInitializationEvent event, final String nameClass)
    {
        Log.writeDataToLogFile(Log.TypeLog[0], nameClass);

        ClientProxy.ConfigPlayer = new Configuration(new File(DynamicSpawnControl.getGlobalPathToConfigs().getPath() +
                File.separator + DynamicSpawnControl.STRUCT_FILES_DIRS.NAME_DIRECTORY + File.separator +
                DynamicSpawnControl.STRUCT_FILES_DIRS.NAME_DIR_CONFIGS, "player" +
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
        ProtectRespawnPlayerRadius =
                configuration.getInt("Protect radius respawn", "respawn_player", ProtectRespawnPlayerRadius, 0, 65,
                        "Removing living entities when the player respawns.");
    }

    /**
     *
     */
    @Override
    public void read()
    {
        Configuration configuration = ClientProxy.ConfigPlayer;

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
