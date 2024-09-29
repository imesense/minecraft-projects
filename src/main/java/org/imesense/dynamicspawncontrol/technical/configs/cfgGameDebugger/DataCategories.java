package org.imesense.dynamicspawncontrol.technical.configs.cfgGameDebugger;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;

import javax.annotation.Nonnull;

public class DataCategories
{
    /**
     *
     */
    public static final class DebugMonitor
    {
        final String setCategory;

        public static DebugMonitor instance;

        private Boolean debug_monitor_cache = false;

        public DebugMonitor(@Nonnull final String category)
        {
            this.setCategory = category;

            CodeGenericUtils.printInitClassToLog(this.getClass());
        }

        public Boolean getDebugMonitorCache()
        {
            return this.debug_monitor_cache;
        }

        public void setDebugMonitorCache(Boolean value)
        {
            this.debug_monitor_cache = value;
        }
    }

    /**
     *
     */
    public static final class DebugEvent
    {
        final String setCategory;

        public static DebugEvent instance;

        private Boolean debug_on_block_break = false;
        private Boolean debug_on_block_place = false;
        private Boolean debug_on_entity_spawn = false;
        private Boolean debug_on_left_click = false;
        private Boolean debug_on_living_drops = false;
        private Boolean debug_on_living_experience_drop = false;
        private Boolean debug_on_task_manager = false;
        private Boolean debug_on_player_tick = false;
        private Boolean debug_on_potential_spawn = false;
        private Boolean debug_on_right_click = false;

        public DebugEvent(@Nonnull final String category)
        {
            this.setCategory = category;

            CodeGenericUtils.printInitClassToLog(this.getClass());
        }

        public Boolean getDebugOnBlockBreak()
        {
            return this.debug_on_block_break;
        }

        public void setDebugOnBlockBreak(Boolean value)
        {
            this.debug_on_block_break = value;
        }

        public Boolean getDebugOnBlockPlace()
        {
            return this.debug_on_block_place;
        }

        public void setDebugOnBlockPlace(Boolean value)
        {
            this.debug_on_block_place = value;
        }

        public Boolean getDebugOnEntitySpawn()
        {
            return this.debug_on_entity_spawn;
        }

        public void setDebugOnEntitySpawn(Boolean value)
        {
            this.debug_on_entity_spawn = value;
        }

        public Boolean getDebugOnLeftClick()
        {
            return this.debug_on_left_click;
        }

        public void setDebugOnLeftClick(Boolean value)
        {
            this.debug_on_left_click = value;
        }

        public Boolean getDebugOnLivingDrops()
        {
            return this.debug_on_living_drops;
        }

        public void setDebugOnLivingDrops(Boolean value)
        {
            this.debug_on_living_drops = value;
        }

        public Boolean getDebugOnLivingExperienceDrop()
        {
            return this.debug_on_living_experience_drop;
        }

        public void setDebugOnLivingExperienceDrop(Boolean value)
        {
            this.debug_on_living_experience_drop = value;
        }

        public Boolean getDebugOnTaskManager()
        {
            return this.debug_on_task_manager;
        }

        public void setDebugOnTaskManager(Boolean value)
        {
            this.debug_on_task_manager = value;
        }

        public Boolean getDebugOnPlayerTick()
        {
            return this.debug_on_player_tick;
        }

        public void setDebugOnPlayerTick(Boolean value)
        {
            this.debug_on_player_tick = value;
        }

        public Boolean getDebugOnPotentialSpawn()
        {
            return this.debug_on_potential_spawn;
        }

        public void setDebugOnPotentialSpawn(Boolean value)
        {
            this.debug_on_potential_spawn = value;
        }

        public Boolean getDebugOnRightClick()
        {
            return this.debug_on_right_click;
        }

        public void setDebugOnRightClick(Boolean value)
        {
            this.debug_on_right_click = value;
        }
    }
}
