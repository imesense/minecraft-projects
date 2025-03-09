package org.imesense.dynamicspawncontrol.core.register.parser;

import org.imesense.dynamicspawncontrol.core.baseregister.BaseParserRegister;
import org.imesense.dynamicspawncontrol.core.script.parser.*;

public final class ParserRegister extends BaseParserRegister
{
    private static volatile ParserRegister _INSTANCE;

    public static ParserRegister getInstance()
    {
        if (_INSTANCE == null)
        {
            synchronized (ParserRegister.class)
            {
                if (_INSTANCE == null)
                {
                    _INSTANCE = new ParserRegister();
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
