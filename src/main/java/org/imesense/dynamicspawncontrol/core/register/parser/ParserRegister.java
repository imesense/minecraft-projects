package org.imesense.dynamicspawncontrol.core.register.parser;

import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseregister.BaseParserRegister;
import org.imesense.dynamicspawncontrol.core.script.parser.*;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

@InitLog
public final class ParserRegister extends BaseParserRegister
{
    private static volatile ParserRegister _INSTANCE;

    public static ParserRegister getInstance()
    {
        return CodeGeneric.getInstance(ParserRegister.class);
    }

    private static final Class<?>[] PARSER_CLASSES =
    {
        ParserEventCacheSettings.class,
        ParserEventCheckSpawn.class,
        ParserEventDropExperience.class,
        ParserEventDropItem.class,
        ParserEventPotentialSpawn.class,
        ParserEventPopulationChunk.class,
        ParserEventMobTaskManager.class,
        ParserEventLootBoxInWorld.class,
        ParserEventDamageByEntity.class
    };

    @Override
    protected Class<?>[] getParserClasses()
    {
        return PARSER_CLASSES;
    }
}
