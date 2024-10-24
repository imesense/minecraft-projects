package org.imesense.dynamicspawncontrol.technical.parser;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.technical.parser.beta.ParserSingleScriptCheckSpawn;
import org.imesense.dynamicspawncontrol.technical.parser.beta.ParserSingleScriptSettingsCache;
import org.imesense.dynamicspawncontrol.technical.parser.beta.ParserSingleZombieSummonAID;

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
    private static final List<IBetaParser> BETA_PARSER_LIST = new ArrayList<>();

    /**
     *
     */
    public ParserManager()
    {
        CodeGenericUtil.printInitClassToLog(this.getClass());
    }

    /**
     *
     */
    public static void init()
    {
        BETA_PARSER_LIST.add(new ParserSingleScriptSettingsCache());
        BETA_PARSER_LIST.add(new ParserSingleZombieSummonAID());
        BETA_PARSER_LIST.add(new ParserSingleScriptCheckSpawn());

        for (IBetaParser parser : BETA_PARSER_LIST)
        {
            parser.loadConfig(true);
        }
    }

    /**
     *
     */
    public static void reloadAllConfigs()
    {
        for (IBetaParser parser : BETA_PARSER_LIST)
        {
            parser.reloadConfig();
        }
    }
}
