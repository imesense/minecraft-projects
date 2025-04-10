package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
/* loaded from: input.jar:cad97/spawnercraft/proxy/IProxy.class */
public interface IProxy {
    void preInit(FMLPreInitializationEvent fMLPreInitializationEvent);

    void init(FMLInitializationEvent fMLInitializationEvent);

    void postInit(FMLPostInitializationEvent fMLPostInitializationEvent);
}

