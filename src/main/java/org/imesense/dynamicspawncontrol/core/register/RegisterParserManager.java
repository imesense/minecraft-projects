package org.imesense.dynamicspawncontrol.core.register;

import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.api.AbstractConceptParser;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.script.parser.ParserEventMobTaskManager;
import org.imesense.dynamicspawncontrol.core.script.parser.ParserEventPotentialSpawn;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.script.parser.ParserEventCacheSettings;
import org.imesense.dynamicspawncontrol.core.script.parser.ParserEventCheckSpawn;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public final class RegisterParserManager
{
    /**
     *
     */
    private static final Class<?>[] PARSER_CLASSES =
    {
        ParserEventCacheSettings.class, //-' TODO - переместить это в отдельный класс
        ParserEventCheckSpawn.class,
        ParserEventPotentialSpawn.class,
        ParserEventMobTaskManager.class
    };

    /**
     *
     */
    private static final List<AbstractConceptParser> PARSER_LIST = new ArrayList<>();

    /**
     *
     */
    public RegisterParserManager()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
    }

    /**
     *
     */
    public static void init()
    {
        for (Class<?> parserClass : PARSER_CLASSES)
        {
            try
            {
                AbstractConceptParser parser = (AbstractConceptParser) parserClass.getConstructor(String.class)
                        .newInstance(getParserName(parserClass));

                PARSER_LIST.add(parser);

                parser.loadConfig(true);
            }
            catch (Exception exception)
            {
                Log.writeDataToLogFile(2, "Exception initializing parser: " + parserClass.getName() + " - " + exception.getMessage());
                throw new RuntimeException(exception);
            }
        }
    }

    /**
     *
     */
    public static void reloadAllConfigs()
    {
        for (AbstractConceptParser parser : PARSER_LIST)
        {
            parser.reloadConfig();
        }
    }

    /**
     *
     * @param parserClass
     * @return
     */
    private static String getParserName(Class<?> parserClass)
    {
        return parserClass.getSimpleName()
                .replaceAll("([a-z])([A-Z]+)", "$1_$2")
                    .toLowerCase() + DynamicSpawnControlStructure.STRUCT_FILES_EXTENSION.SCRIPT_FILE_EXTENSION;
    }
}
