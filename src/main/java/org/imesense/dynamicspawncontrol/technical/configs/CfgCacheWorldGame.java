package org.imesense.dynamicspawncontrol.technical.configs;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;

import java.io.IOException;

public final class CfgCacheWorldGame extends CustomConceptConfig
{
    public static CfgCacheWorldGame instance;

    public CfgCacheWorldGame(String nameConfigFile) throws IOException
    {
        super(nameConfigFile);

        CodeGenericUtils.printInitClassToLog(CfgCacheWorldGame.class);
    }
}
