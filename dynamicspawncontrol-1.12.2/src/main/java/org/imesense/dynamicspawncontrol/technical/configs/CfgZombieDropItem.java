package org.imesense.dynamicspawncontrol.technical.configs;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;

import java.io.IOException;

public final class CfgZombieDropItem extends CustomConceptConfig
{
    public static CfgZombieDropItem instance;

    public CfgZombieDropItem(String nameConfigFile) throws IOException
    {
        super(nameConfigFile);

        CodeGenericUtils.printInitClassToLog(CfgZombieDropItem.class);
    }
}