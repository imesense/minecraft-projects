package org.imesense.dynamicspawncontrol.technical.configs;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.customlibrary.inlineannotations.DCSSingleConfig;

import java.io.IOException;

@DCSSingleConfig(fileName = "cfg_zombie_drop_items.json")
public final class CfgZombieDropItem extends CustomConceptConfig
{
    public CfgZombieDropItem(String nameConfigFile)
    {
        super(nameConfigFile);

        CodeGenericUtils.printInitClassToLog(CfgZombieDropItem.class);
    }
}