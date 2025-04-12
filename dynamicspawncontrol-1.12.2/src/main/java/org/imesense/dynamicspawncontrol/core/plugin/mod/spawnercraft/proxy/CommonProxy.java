package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.proxy;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.handler.ConfigHandler;
import org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.handler.DropsListener;
import org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.init.SpawnerCraftBlocks;
import org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.init.SpawnerCraftItems;
import org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.init.SpawnerCraftRecipes;

abstract class CommonProxy implements IProxy
{
    @Override
    @OverridingMethodsMustInvokeSuper
    public void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(ConfigHandler.instance);
        MinecraftForge.EVENT_BUS.register(DropsListener.instance);
        SpawnerCraftBlocks.registerBlocks();
        SpawnerCraftItems.registerItems();
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void init(FMLInitializationEvent event)
    {
        SpawnerCraftRecipes.registerRecipes();
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void postInit(FMLPostInitializationEvent event)
    {

    }
}
