package org.imesense.dynamicspawncontrol.core.register.parser;

import org.imesense.dynamicspawncontrol.core.api.BaseParserRegister;
import org.imesense.dynamicspawncontrol.core.register.config.RegisterConfig;
import org.imesense.dynamicspawncontrol.core.script.parser.*;

public final class RegisterParser extends BaseParserRegister
{
    private static volatile RegisterParser _INSTANCE;

    public static RegisterParser getInstance()
    {
        if (_INSTANCE == null)
        {
            synchronized (RegisterParser.class)
            {
                if (_INSTANCE == null)
                {
                    _INSTANCE = new RegisterParser();
                }
            }
        }

        return _INSTANCE;
    }

    private static final Class<?>[] PARSER_CLASSES =
    {
        ParserEventCacheSettings.class,
        ParserEventCheckSpawn.class,
        ParserEventDropExperience.class,
        ParserEventDropItem.class,
        ParserEventPotentialSpawn.class,
        ParserEventPopulationChunk.class,
        ParserEventMobTaskManager.class
    };

    @Override
    protected Class<?>[] getParserClasses()
    {
        return PARSER_CLASSES;
    }
}
