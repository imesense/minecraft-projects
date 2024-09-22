package org.imesense.dynamicspawncontrol.technical.parsers;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.imesense.dynamicspawncontrol.DynamicSpawnControl;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.generic.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 *
 */
public final class ParserJsonScripts
{
    /**
     *
     */
    private static String path = "Unknown";

    /**
     *
     */
    public final static List<GenericDropLoot> GENERIC_DROP_LOOT_LIST = new ArrayList<>();

    /**
     *
     */
    public final static List<GenericExperience> GENERIC_EXPERIENCE_LIST = new ArrayList<>();

    /**
     *
     */
    public final static List<GenericOverrideSpawn> GENERIC_OVERRIDE_SPAWN_LIST = new ArrayList<>();

    /**
     *
     */
    public final static List<GenericSpawnConditions> GENERIC_SPAWN_CONDITIONS_LIST = new ArrayList<>();

    /**
     *
     */
    public final static List<GenericLeftClickActions> GENERIC_LEFT_CLICK_ACTIONS_LIST = new ArrayList<>();

    /**
     *
     */
    public final static List<GenericMobsTaskManager> GENERIC_MOBS_TASK_MANAGER_LIST = new ArrayList<>();

    /**
     *
     */
    public final static List<GenericBlockPlaceActions> GENERIC_BLOCK_PLACE_ACTIONS_LIST = new ArrayList<>();

    /**
     *
     */
    public final static List<GenericBlockBreakActions> GENERIC_BLOCK_BREAK_ACTIONS_LIST = new ArrayList<>();

    /**
     *
     */
    public final static List<GenericMapEffectsActions> GENERIC_MAP_EFFECTS_ACTIONS_LIST = new ArrayList<>();

    /**
     *
     */
    public final static List<GenericRightClickActions> GENERIC_RIGHT_CLICK_ACTIONS_LIST = new ArrayList<>();

    /**
     *
     */
    private static final String[] ARRAY_TYPE_SCRIPT = { "Drop", "Block", "Effect", "Mouse", "Spawn", "Zombie" };

    /**
     *
     */
    public static void reloadRules()
    {
        GENERIC_DROP_LOOT_LIST.clear();
        GENERIC_EXPERIENCE_LIST.clear();
        GENERIC_OVERRIDE_SPAWN_LIST.clear();
        GENERIC_SPAWN_CONDITIONS_LIST.clear();
        GENERIC_MOBS_TASK_MANAGER_LIST.clear();

        GENERIC_LEFT_CLICK_ACTIONS_LIST.clear();
        GENERIC_RIGHT_CLICK_ACTIONS_LIST.clear();
        GENERIC_BLOCK_PLACE_ACTIONS_LIST.clear();
        GENERIC_BLOCK_BREAK_ACTIONS_LIST.clear();
        GENERIC_MAP_EFFECTS_ACTIONS_LIST.clear();

        readAllRules();
    }

    /**
     *
     * @param directory
     */
    public static void setRulePath(File directory)
    {
        path = directory.getPath();
    }

    /**
     *
     */
    public static void readRules()
    {
        readAllRules();
    }

    /**
     *
     */
    private static void readAllRules()
    {
        {
            readRules(path, "DropAllItems.json", GenericDropLoot::parse, GENERIC_DROP_LOOT_LIST, ARRAY_TYPE_SCRIPT[0]);

            if (!GENERIC_DROP_LOOT_LIST.isEmpty())
            {
                Log.writeDataToLogFile(0,
                        String.format("Parsing '%s' list size = %d", GENERIC_DROP_LOOT_LIST, GENERIC_DROP_LOOT_LIST.size()));
            }
        }

        {
            readRules(path, "DropAllExperience.json", GenericExperience::parse, GENERIC_EXPERIENCE_LIST, ARRAY_TYPE_SCRIPT[0]);

            if (!GENERIC_EXPERIENCE_LIST.isEmpty())
            {
                Log.writeDataToLogFile(0,
                        String.format("Parsing '%s' list size = %d", GENERIC_EXPERIENCE_LIST, GENERIC_EXPERIENCE_LIST.size()));
            }
        }

        {
            readRules(path, "MainOverrideSpawn.json", GenericOverrideSpawn::parse, GENERIC_OVERRIDE_SPAWN_LIST, ARRAY_TYPE_SCRIPT[4]);

            if (!GENERIC_OVERRIDE_SPAWN_LIST.isEmpty())
            {
                Log.writeDataToLogFile(0,
                        String.format("Parsing '%s' list size = %d", GENERIC_OVERRIDE_SPAWN_LIST, GENERIC_OVERRIDE_SPAWN_LIST.size()));
            }
        }

        {
            readRules(path, "SpawnConditions.json", GenericSpawnConditions::parse, GENERIC_SPAWN_CONDITIONS_LIST, ARRAY_TYPE_SCRIPT[4]);

            if (!GENERIC_SPAWN_CONDITIONS_LIST.isEmpty())
            {
                Log.writeDataToLogFile(0,
                        String.format("Parsing '%s' list size = %d", GENERIC_SPAWN_CONDITIONS_LIST, GENERIC_SPAWN_CONDITIONS_LIST.size()));
            }
        }

        {
            readRules(path, "MobsTaskManager.json", GenericMobsTaskManager::parse, GENERIC_MOBS_TASK_MANAGER_LIST, ARRAY_TYPE_SCRIPT[4]);

            if (!GENERIC_MOBS_TASK_MANAGER_LIST.isEmpty())
            {
                Log.writeDataToLogFile(0,
                        String.format("Parsing '%s' list size = %d", GENERIC_MOBS_TASK_MANAGER_LIST, GENERIC_MOBS_TASK_MANAGER_LIST.size()));
            }
        }

        {
            readRules(path, "EventEffects.json", GenericMapEffectsActions::parse, GENERIC_MAP_EFFECTS_ACTIONS_LIST, ARRAY_TYPE_SCRIPT[2]);

            if (!GENERIC_MAP_EFFECTS_ACTIONS_LIST.isEmpty())
            {
                Log.writeDataToLogFile(0,
                        String.format("Parsing '%s' list size = %d", GENERIC_MAP_EFFECTS_ACTIONS_LIST, GENERIC_MAP_EFFECTS_ACTIONS_LIST.size()));
            }
        }

        {
            readRules(path, "EventBlockPlace.json", GenericBlockPlaceActions::parse, GENERIC_BLOCK_PLACE_ACTIONS_LIST, ARRAY_TYPE_SCRIPT[1]);

            if (!GENERIC_BLOCK_PLACE_ACTIONS_LIST.isEmpty())
            {
                Log.writeDataToLogFile(0,
                        String.format("Parsing '%s' list size = %d", GENERIC_BLOCK_PLACE_ACTIONS_LIST, GENERIC_BLOCK_PLACE_ACTIONS_LIST.size()));
            }
        }

        {
            readRules(path, "EventBlockBreak.json", GenericBlockBreakActions::parse, GENERIC_BLOCK_BREAK_ACTIONS_LIST, ARRAY_TYPE_SCRIPT[1]);

            if (!GENERIC_BLOCK_BREAK_ACTIONS_LIST.isEmpty())
            {
                Log.writeDataToLogFile(0,
                        String.format("Parsing '%s' list size = %d", GENERIC_BLOCK_BREAK_ACTIONS_LIST, GENERIC_BLOCK_BREAK_ACTIONS_LIST.size()));
            }
        }

        {
            readRules(path, "EventLeftMouseClick.json", GenericLeftClickActions::parse, GENERIC_LEFT_CLICK_ACTIONS_LIST, ARRAY_TYPE_SCRIPT[3]);

            if (!GENERIC_LEFT_CLICK_ACTIONS_LIST.isEmpty())
            {
                Log.writeDataToLogFile(0,
                        String.format("Parsing '%s' list size = %d", GENERIC_LEFT_CLICK_ACTIONS_LIST, GENERIC_LEFT_CLICK_ACTIONS_LIST.size()));
            }
        }

        {
            readRules(path, "EventRightMouseClick.json", GenericRightClickActions::parse, GENERIC_RIGHT_CLICK_ACTIONS_LIST, ARRAY_TYPE_SCRIPT[3]);

            if (!GENERIC_RIGHT_CLICK_ACTIONS_LIST.isEmpty())
            {
                Log.writeDataToLogFile(0,
                        String.format("Parsing '%s' list size = %d", GENERIC_RIGHT_CLICK_ACTIONS_LIST, GENERIC_RIGHT_CLICK_ACTIONS_LIST.size()));
            }
        }
    }

    /**
     *
     * @param path
     * @param filename
     * @param parser
     * @param _rules
     * @param getTypeScript
     * @param <T>
     */
    private static <T> void readRules(String path, String filename, Function<JsonElement, T> parser, List<T> _rules, String getTypeScript)
    {
        JsonElement element = getRootElement(path, filename, getTypeScript);

        if (element == null)
        {
            return;
        }

        AtomicInteger i = new AtomicInteger();

        for (JsonElement entry : element.getAsJsonArray())
        {
            T rule = parser.apply(entry);

            if (rule != null)
            {
                _rules.add(rule);
            }
            else
            {
                Log.writeDataToLogFile(0, "Rule " + i + " in " + filename + " is invalid, skipping!");
            }

            i.getAndIncrement();
        }

        if (i.get() != 0)
        {
            Log.writeDataToLogFile(0, "Loaded " + i + " rules!");
        }
    }

    /**
     *
     * @param path
     * @param filename
     * @param getTypeScript
     * @return
     */
    private static JsonElement getRootElement(String path, String filename, String getTypeScript)
    {
        File file;

        if (path == null)
        {
            file = new File(filename);
        }
        else
        {
            File infinityForceSpawnConfigsDir = new File(path + File.separator + DynamicSpawnControl.STRUCT_FILES_DIRS.NAME_DIRECTORY);
            File scriptsDir = new File(infinityForceSpawnConfigsDir, DynamicSpawnControl.STRUCT_FILES_DIRS.NAME_DIR_SCRIPTS + File.separator + getTypeScript);

            if (!scriptsDir.exists())
            {
                if (!scriptsDir.mkdirs())
                {
                    Log.writeDataToLogFile(0, "Failed to create directory: " + scriptsDir.getAbsolutePath());
                }
            }

            file = new File(scriptsDir, filename);
        }

        if (!file.exists())
        {
            makeEmptyRuleFile(file);
            return null;
        }

        Log.writeDataToLogFile(0, "Reading spawn rules from " + filename);

        InputStream inputstream;

        try
        {
            inputstream = new FileInputStream(file);
        }
        catch (FileNotFoundException e)
        {
            Log.writeDataToLogFile(2, "Error reading " + filename + "!");
            return null;
        }

        BufferedReader br;

        try
        {
            br = new BufferedReader(new InputStreamReader(inputstream, "UTF-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            Log.writeDataToLogFile(2, "Error reading " + filename + "!");
            return null;
        }

        JsonParser parser = new JsonParser();

        return parser.parse(br);
    }

    /**
     *
     * @param file
     */
    private static void makeEmptyRuleFile(File file)
    {
        PrintWriter writer;

        try
        {
            writer = new PrintWriter(file);
        }
        catch (FileNotFoundException e)
        {
            Log.writeDataToLogFile(2, "Error writing " + file.getName() + "!");
            return;
        }

        writer.println("[");
        writer.println("//-' OldSerpskiStalker, acidicMercury8");
        writer.println("//-' Dynamic Spawn Control for Minecraft: " + DynamicSpawnControl.STRUCT_INFO_MOD.VERSION);
        writer.println("//-' Our organization: https://github.com/imesense");
        writer.println("]");

        writer.close();
    }
}
