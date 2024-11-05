package org.imesense.dynamicspawncontrol.plugin.bloodmoon_mc1_12_2_1_5_3.asm;

import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;

import java.util.Map;

/**
 *
 */
@IFMLLoadingPlugin.MCVersion(ForgeVersion.mcVersion)
@IFMLLoadingPlugin.Name(DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
@IFMLLoadingPlugin.SortingIndex(LoadingPlugin.AFTER_DEOBFUSCATION)
public final class LoadingPlugin implements IFMLLoadingPlugin
{
    /**
     *
     */
    public static boolean IN_MCP = false;

    /**
     *
     */
    public static final int AFTER_DEOBFUSCATION = 1002;

    /**
     *
     * @return
     */
    public String[] getASMTransformerClass()
    {
        return new String[] { ClassTransformer.class.getName() };
    }

    /**
     *
     * @return
     */
    public String getModContainerClass()
    {
        return null;
    }

    /**
     *
     * @return
     */
    public String getSetupClass()
    {
        return null;
    }

    /**
     *
     * @param objectMap
     */
    public void injectData(Map<String, Object> objectMap)
    {
        IN_MCP = !(Boolean)objectMap.get("runtimeDeobfuscationEnabled");
    }

    /**
     *
     * @return
     */
    public String getAccessTransformerClass()
    {
        return null;
    }
}

