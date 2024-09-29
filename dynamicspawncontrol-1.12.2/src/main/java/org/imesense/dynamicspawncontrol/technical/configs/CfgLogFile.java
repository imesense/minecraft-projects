package org.imesense.dynamicspawncontrol.technical.configs;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;

import java.io.IOException;

public final class CfgLogFile extends CustomConceptConfig
{
    public CfgLogFile(String nameConfigFile) throws IOException
    {
        super(nameConfigFile);

        CodeGenericUtils.printInitClassToLog(CfgLogFile.class);
    }
}
