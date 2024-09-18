package org.imesense.dynamicspawncontrol.technical.plugins;

import java.io.File;
import java.util.Map;

import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.imesense.dynamicspawncontrol.DynamicSpawnControl;
import org.imesense.dynamicspawncontrol.technical.asm.EntityRendererTransformer;
import org.imesense.dynamicspawncontrol.technical.asm.WorldProviderTransformer;

/**
 *
 * -Dfml.coreMods.load=org.imesense.dynamicspawncontrol.technical.plugins.LoadingPluginNight
 */
@IFMLLoadingPlugin.MCVersion(ForgeVersion.mcVersion)
@IFMLLoadingPlugin.Name(DynamicSpawnControl.STRUCT_INFO_MOD.MOD_ID)
@IFMLLoadingPlugin.SortingIndex(LoadingPluginNight.AFTER_DEOBF)
public final class LoadingPluginNight implements IFMLLoadingPlugin
{
    /**
     *
     */
    public static File FILE_LOCATION;

    /**
     *
     */
    public static Boolean RUNTIME_DEOBF;

    /**
     * FMLDeobfTweaker:
     * https://github.com/MinecraftForge/MinecraftForge/blob/a8b9abcb17e28007ed5f5e110997be8e499575e5/src/main/java/net/minecraftforge/fml/relauncher/CoreModManager.java#L633
     */
    public static final int AFTER_DEOBF = 1001;

    /**
     *
     * @return
     */
    @Override
    public String[] getASMTransformerClass()
    {
        return new String[]
        {
            WorldProviderTransformer.class.getName(),
            EntityRendererTransformer.class.getName(),
        };
    }

    /**
     *
     * @return
     */
    @Override
    public String getModContainerClass()
    {
        return null;
    }

    /**
     *
     * @return
     */
    @Override
    public String getSetupClass()
    {
        return null;
    }

    /**
     *
     * @param data
     */
    @Override
    public void injectData(Map<String, Object> data)
    {
        RUNTIME_DEOBF = (Boolean) data.get("runtimeDeobfuscationEnabled");
        FILE_LOCATION = (File) data.get("coremodLocation");
        
        if (FILE_LOCATION == null)
        {
            FILE_LOCATION = new File(
                    getClass()
                            .getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .getPath());
        }
    }

    /**
     *
     * @return
     */
    @Override
    public String getAccessTransformerClass()
    {
        return null;
    }
}
