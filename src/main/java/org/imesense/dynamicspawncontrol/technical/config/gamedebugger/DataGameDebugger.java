package org.imesense.dynamicspawncontrol.technical.config.gamedebugger;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public final class DataGameDebugger
{
    /**
     *
     */
    public static final class DebugMonitor
    {
        /**
         *
         */
        final String setCategory;

        /**
         *
         */
        public static DebugMonitor instance;

        /**
         *
         */
        private Boolean debugMonitorCache = false;

        /**
         *
         * @param category
         */
        public DebugMonitor(@Nonnull final String category)
        {
            CodeGenericUtils.printInitClassToLog(this.getClass());
            this.setCategory = category;
        }

        /**
         *
         * @return
         */
        public Boolean getDebugMonitorCache()
        {
            return this.debugMonitorCache;
        }

        /**
         *
         * @param value
         */
        public void setDebugMonitorCache(Boolean value)
        {
            this.debugMonitorCache = value;
        }
    }

    /**
     *
     */
    public static final class DebugEvent
    {
        /**
         *
         */
        final String setCategory;

        /**
         *
         */
        public static DebugEvent instance;

        /**
         *
         */
        private final Map<String, Boolean> debugSettings = new HashMap<>();

        /**
         *
         * @param category
         */
        public DebugEvent(@Nonnull final String category)
        {
            CodeGenericUtils.printInitClassToLog(this.getClass());

            this.setCategory = category;

            debugSettings.put("debug_on_block_break", false);
            debugSettings.put("debug_on_block_place", false);
            debugSettings.put("debug_on_entity_spawn", false);
            debugSettings.put("debug_on_left_click", false);
            debugSettings.put("debug_on_living_drops", false);
            debugSettings.put("debug_on_living_experience_drop", false);
            debugSettings.put("debug_on_task_manager", false);
            debugSettings.put("debug_on_player_tick", false);
            debugSettings.put("debug_on_potential_spawn", false);
            debugSettings.put("debug_on_right_click", false);
        }

        /**
         *
         * @param key
         * @return
         */
        public Boolean getDebugSetting(String key)
        {
            return debugSettings.getOrDefault(key, false);
        }

        /**
         *
         * @return
         */
        public Map<String, Boolean> getDebugSettings()
        {
            return debugSettings;
        }

        /**
         * 
         * @param key
         * @param value
         */
        public void setDebugSetting(String key, Boolean value)
        {
            if (debugSettings.containsKey(key))
            {
                debugSettings.put(key, value);
            }
            else
            {
                throw new IllegalArgumentException("Unknown debug setting: " + key);
            }
        }
    }
}
