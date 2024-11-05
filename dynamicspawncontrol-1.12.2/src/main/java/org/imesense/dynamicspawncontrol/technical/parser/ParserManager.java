package org.imesense.dynamicspawncontrol.technical.parser;

import org.imesense.dynamicspawncontrol.core.api.IParser;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.technical.parser.beta.IParserSingleZombieSummonAID;
import org.imesense.dynamicspawncontrol.technical.parser.beta.IParserSingleScriptCheckSpawn;
import org.imesense.dynamicspawncontrol.technical.parser.beta.IParserSingleScriptSettingsCache;

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
    private static final List<IParser> BETA_I_PARSER_LIST = new ArrayList<>();

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
        BETA_I_PARSER_LIST.add(new IParserSingleScriptSettingsCache());
        BETA_I_PARSER_LIST.add(new IParserSingleZombieSummonAID());
        BETA_I_PARSER_LIST.add(new IParserSingleScriptCheckSpawn());

        for (IParser iBetaIParser : BETA_I_PARSER_LIST)
        {
            iBetaIParser.loadConfig(true);
        }
    }

    /**
     *
     */
    public static void reloadAllConfigs()
    {
        for (IParser iBetaIParser : BETA_I_PARSER_LIST)
        {
            iBetaIParser.reloadConfig();
        }
    }
}
