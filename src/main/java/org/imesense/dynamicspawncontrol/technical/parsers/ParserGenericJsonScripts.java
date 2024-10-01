package org.imesense.dynamicspawncontrol.technical.parsers;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.imesense.dynamicspawncontrol.DynamicSpawnControl;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
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
public final class ParserGenericJsonScripts
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
    public final static List<GenericPotentialSpawn> GENERIC_POTENTIAL_SPAWN_LIST = new ArrayList<>();

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
    public final static List<GenericMobTaskManager> GENERIC_MOBS_TASK_MANAGER_LIST = new ArrayList<>();

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
        GENERIC_POTENTIAL_SPAWN_LIST.clear();
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
        //
        CodeGenericUtils.readAndLogRules(path, "DropAllItems" + DynamicSpawnControl.STRUCT_FILES_EXTENSION.SCRIPT_FILE_EXTENSION,
                GenericDropLoot::parse, GENERIC_DROP_LOOT_LIST, ARRAY_TYPE_SCRIPT[0]);

        //
        CodeGenericUtils.readAndLogRules(path, "DropAllExperience" + DynamicSpawnControl.STRUCT_FILES_EXTENSION.SCRIPT_FILE_EXTENSION,
                GenericExperience::parse, GENERIC_EXPERIENCE_LIST, ARRAY_TYPE_SCRIPT[0]);

        //
        CodeGenericUtils.readAndLogRules(path, "MainPotentialSpawn" + DynamicSpawnControl.STRUCT_FILES_EXTENSION.SCRIPT_FILE_EXTENSION,
                GenericPotentialSpawn::parse, GENERIC_POTENTIAL_SPAWN_LIST, ARRAY_TYPE_SCRIPT[4]);

        //
        CodeGenericUtils.readAndLogRules(path, "SpawnConditions" + DynamicSpawnControl.STRUCT_FILES_EXTENSION.SCRIPT_FILE_EXTENSION,
                GenericSpawnConditions::parse, GENERIC_SPAWN_CONDITIONS_LIST, ARRAY_TYPE_SCRIPT[4]);

        //
        CodeGenericUtils.readAndLogRules(path, "MobTaskManager" + DynamicSpawnControl.STRUCT_FILES_EXTENSION.SCRIPT_FILE_EXTENSION,
                GenericMobTaskManager::parse, GENERIC_MOBS_TASK_MANAGER_LIST, ARRAY_TYPE_SCRIPT[4]);

        //
        CodeGenericUtils.readAndLogRules(path, "EventEffects" + DynamicSpawnControl.STRUCT_FILES_EXTENSION.SCRIPT_FILE_EXTENSION,
                GenericMapEffectsActions::parse, GENERIC_MAP_EFFECTS_ACTIONS_LIST, ARRAY_TYPE_SCRIPT[2]);

        //
        CodeGenericUtils.readAndLogRules(path, "EventBlockPlace" + DynamicSpawnControl.STRUCT_FILES_EXTENSION.SCRIPT_FILE_EXTENSION,
                GenericBlockPlaceActions::parse, GENERIC_BLOCK_PLACE_ACTIONS_LIST, ARRAY_TYPE_SCRIPT[1]);

        //
        CodeGenericUtils.readAndLogRules(path, "EventBlockBreak" + DynamicSpawnControl.STRUCT_FILES_EXTENSION.SCRIPT_FILE_EXTENSION,
                GenericBlockBreakActions::parse, GENERIC_BLOCK_BREAK_ACTIONS_LIST, ARRAY_TYPE_SCRIPT[1]);

        //
        CodeGenericUtils.readAndLogRules(path, "EventLeftMouseClick" + DynamicSpawnControl.STRUCT_FILES_EXTENSION.SCRIPT_FILE_EXTENSION,
                GenericLeftClickActions::parse, GENERIC_LEFT_CLICK_ACTIONS_LIST, ARRAY_TYPE_SCRIPT[3]);

        //
        CodeGenericUtils.readAndLogRules(path, "EventRightMouseClick" + DynamicSpawnControl.STRUCT_FILES_EXTENSION.SCRIPT_FILE_EXTENSION,
                GenericRightClickActions::parse, GENERIC_RIGHT_CLICK_ACTIONS_LIST, ARRAY_TYPE_SCRIPT[3]);
    }

    /**
     *
     * @param path
     * @param filename
     * @param parser
     * @param rules
     * @param getTypeScript
     * @param <T>
     */
    public static <T> void readRules(final String path, final String filename, Function<JsonElement, T> parser, List<T> rules, final String getTypeScript)
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
                rules.add(rule);
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
    private static JsonElement getRootElement(final String path, final String filename, final String getTypeScript)
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
        catch (FileNotFoundException exception)
        {
            Log.writeDataToLogFile(2, "Error reading " + filename + "!");
            return null;
        }

        BufferedReader br;

        try
        {
            br = new BufferedReader(new InputStreamReader(inputstream, "UTF-8"));
        }
        catch (UnsupportedEncodingException exception)
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
        catch (FileNotFoundException exception)
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
