package org.imesense.dynamicspawncontrol.core.plugin;

import fermiumbooter.FermiumRegistryAPI;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlInitMixins;
import org.spongepowered.asm.launch.MixinBootstrap;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public final class LoadingPluginCore implements IFMLLoadingPlugin
{
    public LoadingPluginCore()
    {
        MixinBootstrap.init();

        for (DynamicSpawnControlInitMixins config : DynamicSpawnControlInitMixins.values())
        {
            config.register();
        }
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
