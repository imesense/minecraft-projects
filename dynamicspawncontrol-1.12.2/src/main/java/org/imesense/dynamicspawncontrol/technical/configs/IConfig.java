package org.imesense.dynamicspawncontrol.technical.configs;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public interface IConfig
{
    void init(FMLPreInitializationEvent event, String nameClass);

    void readProperties(Configuration configuration);

    void read();
}
