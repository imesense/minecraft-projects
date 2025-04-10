package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.proxy;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
/* loaded from: input.jar:cad97/spawnercraft/proxy/CommonProxy.class */
abstract class CommonProxy implements IProxy {
    @Override // cad97.spawnercraft.proxy.IProxy
    @OverridingMethodsMustInvokeSuper
    public void preInit(FMLPreInitializationEvent event) {
        ConfigHandler.init(event.getSuggestedConfigurationFile());
        MinecraftForge.EVENT_BUS.register(ConfigHandler.instance);
        MinecraftForge.EVENT_BUS.register(DropsListener.instance);
        SpawnerCraftBlocks.registerBlocks();
        SpawnerCraftItems.registerItems();
    }

    @Override // cad97.spawnercraft.proxy.IProxy
    @OverridingMethodsMustInvokeSuper
    public void init(FMLInitializationEvent event) {
        SpawnerCraftRecipes.registerRecipes();
    }

    @Override // cad97.spawnercraft.proxy.IProxy
    @OverridingMethodsMustInvokeSuper
    public void postInit(FMLPostInitializationEvent event) {
    }
}
