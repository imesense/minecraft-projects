package org.imesense.dynamicspawncontrol.technical.configs;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;

import java.io.IOException;

public final class CfgPlayer extends CustomConceptConfig
{
    public static CfgPlayer instance;

    public CfgPlayer(String nameConfigFile) throws IOException
    {
        super(nameConfigFile);

        CodeGenericUtils.printInitClassToLog(CfgPlayer.class);
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
