package org.imesense.dynamicspawncontrol.core.plugin;

import fermiumbooter.FermiumRegistryAPI;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public final class LoadingPluginCore implements IFMLLoadingPlugin
{
    public LoadingPluginCore()
    {
        MixinBootstrap.init();

        FermiumRegistryAPI.enqueueMixin(false, "mixin.unlimited.enchantment.json");
        FermiumRegistryAPI.enqueueMixin(false, "mixin.darkness.renderer.json");

        FermiumRegistryAPI.enqueueMixin(true, "mixin.fix.spawn.divinerpg.json",
                () -> Loader.isModLoaded("divinerpg"));
    }

    @Override
    public String[] getASMTransformerClass()
    {
        return new String[0];
    }

    @Override
    public String getModContainerClass()
    {
        return null;
    }

    @Override
    public String getSetupClass()
    {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> objectMap)
    {

    }

    @Override
    public String getAccessTransformerClass()
    {
        return null;
    }
}
