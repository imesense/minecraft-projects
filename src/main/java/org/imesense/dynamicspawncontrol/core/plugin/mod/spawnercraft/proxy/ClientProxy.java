package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.proxy;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.init.SpawnerCraftBlocks;
import org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.init.SpawnerCraftItems;

public final class ClientProxy extends CommonProxy
{
    @Override
    @OverridingMethodsMustInvokeSuper
    public void postInit(FMLPostInitializationEvent event)
    {
        super.postInit(event);
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
        SpawnerCraftBlocks.registerModels();
        SpawnerCraftItems.registerModels();
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        super.init(event);
        SpawnerCraftItems.registerColors(Minecraft.getMinecraft().getItemColors());
    }
}
