package org.imesense.dynamicspawncontrol;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public final class DynamicSpawnControlLoadMixins implements IFMLLoadingPlugin
{
    public DynamicSpawnControlLoadMixins()
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
