package org.imesense.dynamicspawncontrol.technical.configs;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;

import java.io.IOException;

public final class CfgGameDebugger extends CustomConceptConfig
{
    public static CfgGameDebugger instance;

    public CfgGameDebugger(String nameConfigFile) throws IOException
    {
        super(nameConfigFile);

        CodeGenericUtils.printInitClassToLog(CfgGameDebugger.class);
    }
}
