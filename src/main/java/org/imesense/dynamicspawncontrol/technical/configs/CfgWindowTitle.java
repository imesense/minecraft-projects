package org.imesense.dynamicspawncontrol.technical.configs;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;

import java.io.IOException;

public final class CfgWindowTitle extends CustomConceptConfig
{
    public static CfgWindowTitle instance;

    public CfgWindowTitle(String nameConfigFile) throws IOException
    {
        super(nameConfigFile);

        CodeGenericUtils.printInitClassToLog(CfgWindowTitle.class);
    }
}
