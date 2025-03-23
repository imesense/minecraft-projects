package org.imesense.dynamicspawncontrol.core.baseregister;

import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.baseparser.BaseParser;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseParserRegister
{
    protected abstract Class<?>[] getParserClasses();

    private final List<BaseParser> PARSER_LIST = new ArrayList<>();

    public void init()
    {
        for (Class<?> parserClass : getParserClasses())
        {
            try
            {
                BaseParser parser = (BaseParser) parserClass.getConstructor(String.class)
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

    public void reloadAllConfigs()
    {
        for (BaseParser parser : PARSER_LIST)
        {
            parser.reloadConfig();
        }
    }

    private String getParserName(Class<?> parserClass)
    {
        return parserClass.getSimpleName()
                .replaceAll("([a-z])([A-Z]+)", "$1_$2")
                .toLowerCase() + DynamicSpawnControlStructure.STRUCT_FILES_EXTENSION.SCRIPT_FILE_EXTENSION;
    }
}
