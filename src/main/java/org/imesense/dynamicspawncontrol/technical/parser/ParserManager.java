package org.imesense.dynamicspawncontrol.technical.parser;

import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.api.AParser;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.parser.single.ParserWorldCacheMobs;
import org.imesense.dynamicspawncontrol.parser.multiple.ParserCheckSpawnEntity;
import org.imesense.dynamicspawncontrol.parser.multiple.ParserSpecialSpawnEntity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public final class ParserManager
{
    /**
     *
     */
    private static final List<AParser> PARSER_LIST = new ArrayList<>();

    /**
     *
     */
    public ParserManager()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
    }

    /**
     *
     */
    public static void init()
    {
        PARSER_LIST.add(new ParserWorldCacheMobs("world_cache_mobs" +
                DynamicSpawnControlStructure.STRUCT_FILES_EXTENSION.SCRIPT_FILE_EXTENSION));

        PARSER_LIST.add(new ParserSpecialSpawnEntity());
        PARSER_LIST.add(new ParserCheckSpawnEntity());

        for (AParser parser : PARSER_LIST)
        {
            parser.loadConfig(true);
        }
    }

    /**
     *
     */
    public static void reloadAllConfigs()
    {
        for (AParser parser : PARSER_LIST)
        {
            parser.reloadConfig();
        }
    }
}
