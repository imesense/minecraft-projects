package org.imesense.dynamicspawncontrol.core.baseregister;

import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.baseparser.BaseParser;
import org.imesense.dynamicspawncontrol.core.logfile.Logger;

import java.util.ArrayList;
import java.util.List;

@TODO(value = "Merge 'base' files into 'core/base/...' and fix the class diagram in version 0.2", showOnce = false, priority = TODO.TodoPriority.HIGH)
public abstract class BaseParserRegister
{
    protected abstract Class<?>[] getParserClasses();

    protected final List<BaseParser> PARSER_LIST = new ArrayList<>();

    public BaseParserRegister()
    {

    }

    public void init()
    {
        for (Class<?> _class : getParserClasses())
        {
            try
            {
                BaseParser baseParser = (BaseParser) _class.getConstructor(String.class)
                        .newInstance(getParserName(_class));

                PARSER_LIST.add(baseParser);

                baseParser.loadConfig(true);
            }
            catch (Exception exception)
            {
                Logger.error("Exception initializing parser: " +
                        _class.getName() + " - " + exception.getMessage());

                throw new RuntimeException(exception);
            }
        }
    }

    public void reloadAllConfigs()
    {
        for (BaseParser baseParser : PARSER_LIST)
        {
            baseParser.reloadConfig();
        }
    }

    protected String getParserName(Class<?> _class)
    {
        return _class.getSimpleName()
                .replaceAll("([a-z])([A-Z]+)", "$1_$2")
                .toLowerCase() + DynamicSpawnControlStructure.STRUCT_FILES_EXTENSION.SCRIPT_FILE_EXTENSION;
    }
}
