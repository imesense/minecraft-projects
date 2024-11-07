package org.imesense.dynamicspawncontrol.core.register;

import org.imesense.dynamicspawncontrol.core.api.AParser;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.parser.single.ParserWorldCacheMobs;
import org.imesense.dynamicspawncontrol.parser.multiple.ParserCheckSpawnEntity;
import org.imesense.dynamicspawncontrol.parser.multiple.ParserSpecialSpawnEntity;

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
        ParserWorldCacheMobs.class,
        ParserSpecialSpawnEntity.class,
        ParserCheckSpawnEntity.class
    };

    /**
     *
     */
    private static final List<AParser> PARSER_LIST = new ArrayList<>();

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
                AParser parser = (AParser) parserClass.getConstructor(String.class)
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
        for (AParser parser : PARSER_LIST)
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
                .toLowerCase();
    }
}
