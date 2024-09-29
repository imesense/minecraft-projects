package org.imesense.dynamicspawncontrol.technical.configs;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.customlibrary.inlineannotations.DCSSingleConfig;

import java.io.IOException;

@DCSSingleConfig(fileName = "cfg_window_title.json")
public final class CfgWindowTitle extends CustomConceptConfig
{
    public CfgWindowTitle(String nameConfigFile)
    {
        super(nameConfigFile);

        CodeGenericUtils.printInitClassToLog(CfgWindowTitle.class);
    }
}
