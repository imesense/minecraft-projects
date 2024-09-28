package org.imesense.dynamicspawncontrol.technical.configs;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;

import java.io.IOException;

public final class CfgRenderNightConfig extends CustomConceptConfig
{
    public static CfgRenderNightConfig instance;

    public CfgRenderNightConfig(String nameConfigFile) throws IOException
    {
        super(nameConfigFile);

        CodeGenericUtils.printInitClassToLog(CfgRenderNightConfig.class);
    }
}
