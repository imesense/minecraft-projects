package org.imesense.dynamicspawncontrol.technical.configs;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 *
 */
public interface IConfig
{
    /**
     *
     * @param event
     * @param nameClass
     */
    void init(FMLPreInitializationEvent event, String nameClass);

    /**
     *
     * @param configuration
     */
    void readProperties(Configuration configuration);

    /**
     *
     */
    void read();
}
