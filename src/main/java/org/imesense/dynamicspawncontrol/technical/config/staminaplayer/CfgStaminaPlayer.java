package org.imesense.dynamicspawncontrol.technical.config.staminaplayer;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.technical.config.CfgClassAbstract;
import org.imesense.dynamicspawncontrol.technical.customlibrary.inlineannotation.DCSSingleConfig;

import java.nio.file.Files;
import java.nio.file.Paths;

@DCSSingleConfig(fileName = "cfg_plugin_stamina_player")
public final class CfgStaminaPlayer extends CfgClassAbstract
{
    public CfgStaminaPlayer(String nameConfigFile)
    {
        super(nameConfigFile, Boolean.FALSE);

        CodeGenericUtil.printInitClassToLog(this.getClass());

        if (Files.exists(Paths.get(this.nameConfig)))
        {
            this.loadFromFile();
        }
        else
        {
            this.saveToFile();
        }
    }

    /**
     *
     */
    @Override
    public void saveToFile() {

    }

    /**
     *
     */
    @Override
    public void loadFromFile() {

    }
}
