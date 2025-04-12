package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.proxy;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public final class ServerProxy extends CommonProxy
{
    @Override
    @OverridingMethodsMustInvokeSuper
    public void postInit(FMLPostInitializationEvent event)
    {
        super.postInit(event);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void init(FMLInitializationEvent event)
    {
        super.init(event);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
    }
}
