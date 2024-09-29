package org.imesense.dynamicspawncontrol.technical.configs;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.customlibrary.inlineannotations.DCSSingleConfig;

import java.io.IOException;

@DCSSingleConfig(fileName = "cfg_log_file.json")
public final class CfgLogFile extends CustomConceptConfig
{
    public CfgLogFile(String nameConfigFile)
    {
        super(nameConfigFile);

        CodeGenericUtils.printInitClassToLog(CfgLogFile.class);
    }
}
