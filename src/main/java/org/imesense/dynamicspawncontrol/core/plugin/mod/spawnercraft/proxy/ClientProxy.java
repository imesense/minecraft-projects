package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.proxy;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.init.SpawnerCraftBlocks;
import org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.init.SpawnerCraftItems;

/* loaded from: input.jar:cad97/spawnercraft/proxy/ClientProxy.class */
public final class ClientProxy extends CommonProxy {
    @Override // cad97.spawnercraft.proxy.CommonProxy, cad97.spawnercraft.proxy.IProxy
    @OverridingMethodsMustInvokeSuper
    public /* bridge */ /* synthetic */ void postInit(FMLPostInitializationEvent fMLPostInitializationEvent) {
        super.postInit(fMLPostInitializationEvent);
    }

    @Override // cad97.spawnercraft.proxy.CommonProxy, cad97.spawnercraft.proxy.IProxy
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        SpawnerCraftBlocks.registerModels();
        SpawnerCraftItems.registerModels();
    }

    @Override // cad97.spawnercraft.proxy.CommonProxy, cad97.spawnercraft.proxy.IProxy
    public void init(FMLInitializationEvent event) {
        super.init(event);
        SpawnerCraftItems.registerColors(Minecraft.getMinecraft().getItemColors());
    }
}
