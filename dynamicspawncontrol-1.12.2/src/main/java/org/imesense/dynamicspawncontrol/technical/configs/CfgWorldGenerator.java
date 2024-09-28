package org.imesense.dynamicspawncontrol.technical.configs;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;

import java.io.IOException;

public final class CfgWorldGenerator extends CustomConceptConfig
{
    public static CfgWorldGenerator instance;

    public CfgWorldGenerator(String nameConfigFile) throws IOException
    {
        super(nameConfigFile);

        CodeGenericUtils.printInitClassToLog(CfgWorldGenerator.class);
    }
}