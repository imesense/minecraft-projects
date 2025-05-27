package org.imesense.dynamicspawncontrol.core.plugin;

import fermiumbooter.FermiumRegistryAPI;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.plugin.mod.bloodmoon_mc1_12_2_1_5_3.ClassTransformer;
import org.imesense.dynamicspawncontrol.core.plugin.mod.darkness_forge_1_12_x_0_5_0.EntityRendererTransformer;
import org.imesense.dynamicspawncontrol.core.plugin.mod.darkness_forge_1_12_x_0_5_0.WorldProviderTransformer;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public final class LoadingPluginCore implements IFMLLoadingPlugin
{
    public static File File_location;
    public static Boolean Runtime_deobfuscation;

    public LoadingPluginCore()
    {
        MixinBootstrap.init();

        FermiumRegistryAPI.enqueueMixin(false, "mixin.unlimited.enchantment.json");

        FermiumRegistryAPI.enqueueMixin(true, "mixin.fix.spawn.divinerpg.json",
                () -> Loader.isModLoaded("divinerpg"));
    }

    @Override
    public String[] getASMTransformerClass()
    {
        return new String[]
        {
            WorldProviderTransformer.class.getName(),
            EntityRendererTransformer.class.getName(),
            ClassTransformer.class.getName()
        };
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
        Runtime_deobfuscation = (Boolean) objectMap.get("runtimeDeobfuscationEnabled");
        File_location = (File) objectMap.get("coremodLocation");

        if (File_location == null)
        {
            File_location = new File(
                    getClass()
                            .getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .getPath());
        }
    }

    @Override
    public String getAccessTransformerClass()
    {
        return null;
    }
}
