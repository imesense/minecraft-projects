package org.imesense.dynamicspawncontrol.technical.configs;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.customlibrary.inlineannotations.DCSSingleConfig;

import java.io.IOException;

@DCSSingleConfig(fileName = "cfg_cache_world_game.json")
public final class CfgCacheWorldGame extends CustomConceptConfig
{
    public CfgCacheWorldGame(String nameConfigFile)
    {
        super(nameConfigFile);

        CodeGenericUtils.printInitClassToLog(CfgCacheWorldGame.class);
    }
}
