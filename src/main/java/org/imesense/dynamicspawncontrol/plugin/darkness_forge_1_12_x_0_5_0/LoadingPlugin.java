package org.imesense.dynamicspawncontrol.plugin.darkness_forge_1_12_x_0_5_0;

import java.io.File;
import java.util.Map;

import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.plugin.darkness_forge_1_12_x_0_5_0.asm.EntityRendererTransformer;
import org.imesense.dynamicspawncontrol.plugin.darkness_forge_1_12_x_0_5_0.asm.WorldProviderTransformer;

/**
 *
 * OldSerpskiStalker:
 * -Dfml.coreMods.load=org.imesense.dynamicspawncontrol.technical.plugins.LoadingPluginNight
 */
@IFMLLoadingPlugin.MCVersion(ForgeVersion.mcVersion)
@IFMLLoadingPlugin.Name(DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
@IFMLLoadingPlugin.SortingIndex(LoadingPlugin.AFTER_DEOBFUSCATION)
public final class LoadingPlugin implements IFMLLoadingPlugin
{
    /**
     *
     */
    public static File File_location;

    /**
     *
     */
    public static Boolean Runtime_deobfuscation;

    /**
     *
     */
    public static final int AFTER_DEOBFUSCATION = 1001;

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
     * @param objectMap
     */
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
