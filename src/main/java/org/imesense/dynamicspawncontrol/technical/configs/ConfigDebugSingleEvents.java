package org.imesense.dynamicspawncontrol.technical.configs;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.gameplay.worldgenerator.NetherRackGenerator;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.proxy.ClientProxy;

/**
 *
 */
public final class ConfigDebugSingleEvents implements IConfig
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public static boolean DebugActionAddEnemyToLog = false;

    /**
     *
     */
    public static boolean DebugActionPanicToIdLog = false;

    /**
     *
     */
    public ConfigDebugSingleEvents()
    {
        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;

        CodeGenericUtils.printInitClassToLog(ConfigDebugSingleEvents.class);
    }

    /**
     *
     * @param event
     */
    @Override
    public void init(FMLPreInitializationEvent event)
    {
        ClientProxy.ConfigDebugSingleEvents = this.createConfiguration("debug_actions_single_events");

        this.read();
    }

    @Override
    public void readProperties(Configuration configuration)
    {
        DebugActionAddEnemyToLog = this.getConfigValueB(
                configuration,
                "Debug Action 'AddEnemy'",
                "actions_single_events",
                DebugActionAddEnemyToLog,
                "Tracks the logical action of 'addEnemy'");

        DebugActionPanicToIdLog = this.getConfigValueB(
                configuration,
                "Debug Action 'AddPanicToId'",
                "actions_single_events",
                DebugActionPanicToIdLog,
                "Tracks the logical action of 'addPanicToId'");
    }

    /**
     *
     */
    @Override
    public void read()
    {
        Configuration configuration = ClientProxy.ConfigDebugSingleEvents;

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
