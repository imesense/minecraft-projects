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
public final class ConfigPlayer implements IConfig
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public static int ProtectRespawnPlayerRadius = 25;

    /**
     *
     */
    public ConfigPlayer()
    {
        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;

        CodeGenericUtils.printInitClassToLog(ConfigPlayer.class);
    }

    /**
     *
     * @param event
     */
    @Override
    public void init(FMLPreInitializationEvent event)
    {
        ClientProxy.ConfigPlayer = this.createConfiguration("player");

        this.read();
    }

    /**
     *
     * @param configuration
     */
    @Override
    public void readProperties(Configuration configuration)
    {
        ProtectRespawnPlayerRadius = this.getConfigValueI(
                configuration,
                "Protect radius respawn",
                "respawn_player",
                ProtectRespawnPlayerRadius,
                0, 65,
                "Removing living entities when the player respawns."
        );
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
