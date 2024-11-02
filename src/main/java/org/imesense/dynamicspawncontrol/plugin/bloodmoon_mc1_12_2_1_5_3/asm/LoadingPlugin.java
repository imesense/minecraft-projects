package org.imesense.dynamicspawncontrol.plugin.bloodmoon_mc1_12_2_1_5_3.asm;

import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.imesense.dynamicspawncontrol.ProjectStructure;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion(ForgeVersion.mcVersion)
@IFMLLoadingPlugin.Name(ProjectStructure.STRUCT_INFO_MOD.MOD_ID)
@IFMLLoadingPlugin.SortingIndex(LoadingPlugin.AFTER_DEOBF)
public final class LoadingPlugin implements IFMLLoadingPlugin
{
    public static boolean IN_MCP = false;

    public static final int AFTER_DEOBF = 1002;

    public String[] getASMTransformerClass() {
        return new String[]{ClassTransformer.class.getName()};
    }

    public String getModContainerClass() {
        return null;
    }

    public String getSetupClass() {
        return null;
    }

    public void injectData(Map<String, Object> data) {
        IN_MCP = !(Boolean)data.get("runtimeDeobfuscationEnabled");
    }

    public String getAccessTransformerClass() {
        return null;
    }
}

