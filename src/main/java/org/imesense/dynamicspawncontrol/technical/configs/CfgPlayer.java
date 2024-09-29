package org.imesense.dynamicspawncontrol.technical.configs;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.customlibrary.inlineannotations.DCSSingleConfig;

import java.io.IOException;

@DCSSingleConfig(fileName = "cfg_player.json")
public final class CfgPlayer extends CustomConceptConfig
{
    public CfgPlayer(String nameConfigFile)
    {
        super(nameConfigFile);

        CodeGenericUtils.printInitClassToLog(CfgPlayer.class);
    }
}
