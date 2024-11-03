package org.imesense.dynamicspawncontrol.technical.config.gamedebugger;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.technical.customlibrary.inlineannotation.Getter;
import org.imesense.dynamicspawncontrol.technical.customlibrary.inlineannotation.Setter;

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
    public static final class ConfigDataMonitor
    {
        /**
         *
         */
        private final String CATEGORY;

        /**
         *
         */
        public static ConfigDataMonitor Instance;

        /**
         *
         */
        private Boolean debugMonitorCache = false;

        /**
         *
         * @param CATEGORY
         */
        public ConfigDataMonitor(@Nonnull final String CATEGORY)
        {
            CodeGenericUtil.printInitClassToLog(this.getClass());

            this.CATEGORY = CATEGORY;
        }

        /**
         *
         * @return
         */
        @Getter
        public Boolean getDebugMonitorCache()
        {
            return this.debugMonitorCache;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setDebugMonitorCache(Boolean value)
        {
            this.debugMonitorCache = value;
        }

        /**
         *
         * @return
         */
        @Getter
        public String getCategoryObject()
        {
            return this.CATEGORY;
        }
    }

    /**
     *
     */
    public static final class ConfigDataEvent
    {
        /**
         *
         */
        private final String CATEGORY;

        /**
         *
         */
        public static ConfigDataEvent Instance;

        /**
         *
         */
        private final Map<String, Boolean> DEBUG_SETTINGS = new HashMap<>();

        /**
         *
         * @param CATEGORY
         */
        public ConfigDataEvent(@Nonnull final String CATEGORY)
        {
            CodeGenericUtil.printInitClassToLog(this.getClass());

            this.CATEGORY = CATEGORY;

            DEBUG_SETTINGS.put("debug_on_block_break", false);
            DEBUG_SETTINGS.put("debug_on_block_place", false);
            DEBUG_SETTINGS.put("debug_on_entity_spawn", false);
            DEBUG_SETTINGS.put("debug_on_left_click", false);
            DEBUG_SETTINGS.put("debug_on_living_drops", false);
            DEBUG_SETTINGS.put("debug_on_living_experience_drop", false);
            DEBUG_SETTINGS.put("debug_on_task_manager", false);
            DEBUG_SETTINGS.put("debug_on_player_tick", false);
            DEBUG_SETTINGS.put("debug_on_potential_spawn", false);
            DEBUG_SETTINGS.put("debug_on_right_click", false);
        }

        /**
         *
         * @param key
         * @return
         */
        @Getter
        public Boolean getDebugSetting(String key)
        {
            return DEBUG_SETTINGS.getOrDefault(key, false);
        }

        /**
         *
         * @return
         */
        @Getter
        public Map<String, Boolean> getDebugSettings()
        {
            return this.DEBUG_SETTINGS;
        }

        /**
         * 
         * @param key
         * @param value
         */
        @Setter
        public void setDebugSetting(String key, Boolean value)
        {
            if (DEBUG_SETTINGS.containsKey(key))
            {
                DEBUG_SETTINGS.put(key, value);
            }
            else
            {
                throw new IllegalArgumentException("Unknown debug setting: " + key);
            }
        }

        /**
         *
         * @return
         */
        @Getter
        public String getCategoryObject()
        {
            return this.CATEGORY;
        }
    }
}
