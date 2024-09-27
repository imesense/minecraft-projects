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

    @Override
    public void init() throws IOException
    {

    }

    @Override
    public void loadConfigFromFile() throws IOException {

    }

    @Override
    public boolean getConfigValue(String key) {
        return false;
    }
}
