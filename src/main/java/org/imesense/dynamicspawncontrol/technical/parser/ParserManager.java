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
    private static final List<IBetaParser> betaParsersList = new ArrayList<>();

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
        betaParsersList.add(new ParserSingleScriptSettingsCache());
        betaParsersList.add(new ParserSingleZombieSummonAID());
        betaParsersList.add(new ParserSingleScriptCheckSpawn());

        for (IBetaParser parser : betaParsersList)
        {
            parser.loadConfig(true);
        }
    }

    /**
     *
     */
    public static void reloadAllConfigs()
    {
        for (IBetaParser parser : betaParsersList)
        {
            parser.reloadConfig();
        }
    }
}
