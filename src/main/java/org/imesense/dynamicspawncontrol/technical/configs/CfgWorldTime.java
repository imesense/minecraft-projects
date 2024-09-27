package org.imesense.dynamicspawncontrol.technical.configs;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;

import java.io.IOException;

public final class CfgWorldTime extends CustomConceptConfig
{
    public static CfgWorldTime instance;

    public CfgWorldTime(String nameConfigFile) throws IOException
    {
        super(nameConfigFile);

        CodeGenericUtils.printInitClassToLog(CfgWorldTime.class);
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