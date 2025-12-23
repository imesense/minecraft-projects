package org.imesense.dynamicspawncontrol.core.script.parser;

import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.baseparser.BaseParser;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

import java.io.File;

@InitLog
@TODO(
        value = "Work in Progress. Concept for 0.2 ver",
        priority = TODO.TodoPriority.HIGH,
        showOnce = false
)
public final class ParserEventDamageByEntity extends BaseParser
{
    public ParserEventDamageByEntity(final String NAME_FILE)
    {
        this.nameFile = NAME_FILE;
    }

    @Override
    public void loadConfig(boolean init)
    {
        Log.write(0, "Reading the config for the first time: " + init + " " + "file: " + this.nameFile);

        File file = getConfigFile(init,
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_GAME_SCRIPTS, this.nameFile);

        if (!file.exists())
        {
            Log.write(0, "Config file not found, creating new: " + file);
            this.createNewConfigFile(file);
            return;
        }
    }

    @Override
    public void eraseData()
    {

    }
}
