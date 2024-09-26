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
}