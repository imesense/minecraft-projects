package org.imesense.dynamicspawncontrol.technical.parsers;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.parsers.beta.ParserSingleScriptCheckSpawn;
import org.imesense.dynamicspawncontrol.technical.parsers.beta.ParserSingleScriptSettingsCache;
import org.imesense.dynamicspawncontrol.technical.parsers.beta.ParserSingleZombieSummonAID;

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
    private static final List<IBetaParsers> betaParsersList = new ArrayList<>();

    /**
     *
     */
    public ParserManager()
    {
        CodeGenericUtils.printInitClassToLog(this.getClass());
    }

    /**
     *
     */
    public static void init()
    {
        betaParsersList.add(new ParserSingleScriptSettingsCache());
        betaParsersList.add(new ParserSingleZombieSummonAID());
        betaParsersList.add(new ParserSingleScriptCheckSpawn());

        for (IBetaParsers parser : betaParsersList)
        {
            parser.loadConfig(true);
        }
    }

    /**
     *
     */
    public static void reloadAllConfigs()
    {
        for (IBetaParsers parser : betaParsersList)
        {
            parser.reloadConfig();
        }
    }
}
