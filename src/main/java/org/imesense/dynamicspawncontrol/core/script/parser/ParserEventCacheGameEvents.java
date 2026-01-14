package org.imesense.dynamicspawncontrol.core.script.parser;

import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseparser.BaseParser;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

import java.io.File;

//[
/*  {
    "event": {
      "day": 7,
      "repeat": true
    },
    "execute": {
      "id_dimension": 0,
      "entity": "minecraft:zombie",
      "max_entity_count": 150,
      "result": "deny"
    },
    "else": {
    // Сюда указываем ноду, которую отправляем в основной кеш для ограничения сущности
      //"id_dimension": 0,
      //"entity": "minecraft:zombie",
     // "max_entity_count": 18,
     // "result": "deny"
     "node_id": 000, -> отправляем в парсер ParserEventCacheSettings. Чтобы активировать его опцию без override события в этом парсере
    }
  }*/
 //]

@InitLog
public class ParserEventCacheGameEvents extends BaseParser
{
    private static final boolean DEBUG_AND_CHECK_SYNTAX = true;

    public ParserEventCacheGameEvents(final String NAME_FILE)
    {
        this.nameFile = NAME_FILE;

        if (DEBUG_AND_CHECK_SYNTAX)
        {
            Log.write(0, "ParserEventCacheSettings constructor called with file: " + NAME_FILE);
        }
    }

    @Override
    public void loadConfig(boolean init)
    {
        Log.write(0, "Reading the config for the first time: " + init + " " + "file: " + this.nameFile);

        File file = getConfigFile(init,
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_CACHE, this.nameFile);

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
