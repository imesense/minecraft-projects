package org.imesense.dynamicspawncontrol.technical.config.gamedebugger;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;

import javax.annotation.Nonnull;

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
        private Boolean debugOnBlockBreak = false;

        /**
         *
         */
        private Boolean debugOnBlockPlace = false;

        /**
         *
         */
        private Boolean debugOnEntitySpawn = false;

        /**
         *
         */
        private Boolean debugOnLeftClick = false;

        /**
         *
         */
        private Boolean debugOnLivingDrops = false;

        /**
         *
         */
        private Boolean debugOnLivingExperienceDrop = false;

        /**
         *
         */
        private Boolean debugOnTaskManager = false;

        /**
         *
         */
        private Boolean debugOnPlayerTick = false;

        /**
         *
         */
        private Boolean debugOnPotentialSpawn = false;

        /**
         *
         */
        private Boolean debugOnRightClick = false;

        /**
         *
         * @param category
         */
        public DebugEvent(@Nonnull final String category)
        {
			CodeGenericUtils.printInitClassToLog(this.getClass());
			
            this.setCategory = category;
        }

        /**
         *
         * @return
         */
        public Boolean getDebugOnBlockBreak()
        {
            return this.debugOnBlockBreak;
        }

        /**
         *
         * @param value
         */
        public void setDebugOnBlockBreak(Boolean value)
        {
            this.debugOnBlockBreak = value;
        }

        /**
         *
         * @return
         */
        public Boolean getDebugOnBlockPlace()
        {
            return this.debugOnBlockPlace;
        }

        /**
         *
         * @param value
         */
        public void setDebugOnBlockPlace(Boolean value)
        {
            this.debugOnBlockPlace = value;
        }

        /**
         *
         * @return
         */
        public Boolean getDebugOnEntitySpawn()
        {
            return this.debugOnEntitySpawn;
        }

        /**
         *
         * @param value
         */
        public void setDebugOnEntitySpawn(Boolean value)
        {
            this.debugOnEntitySpawn = value;
        }

        /**
         *
         * @return
         */
        public Boolean getDebugOnLeftClick()
        {
            return this.debugOnLeftClick;
        }

        /**
         *
         * @param value
         */
        public void setDebugOnLeftClick(Boolean value)
        {
            this.debugOnLeftClick = value;
        }

        /**
         *
         * @return
         */
        public Boolean getDebugOnLivingDrops()
        {
            return this.debugOnLivingDrops;
        }

        /**
         *
         * @param value
         */
        public void setDebugOnLivingDrops(Boolean value)
        {
            this.debugOnLivingDrops = value;
        }

        /**
         *
         * @return
         */
        public Boolean getDebugOnLivingExperienceDrop()
        {
            return this.debugOnLivingExperienceDrop;
        }

        /**
         *
         * @param value
         */
        public void setDebugOnLivingExperienceDrop(Boolean value)
        {
            this.debugOnLivingExperienceDrop = value;
        }

        /**
         *
         * @return
         */
        public Boolean getDebugOnTaskManager()
        {
            return this.debugOnTaskManager;
        }

        /**
         *
         * @param value
         */
        public void setDebugOnTaskManager(Boolean value)
        {
            this.debugOnTaskManager = value;
        }

        /**
         *
         * @return
         */
        public Boolean getDebugOnPlayerTick()
        {
            return this.debugOnPlayerTick;
        }

        /**
         *
         * @param value
         */
        public void setDebugOnPlayerTick(Boolean value)
        {
            this.debugOnPlayerTick = value;
        }

        /**
         *
         * @return
         */
        public Boolean getDebugOnPotentialSpawn()
        {
            return this.debugOnPotentialSpawn;
        }

        /**
         *
         * @param value
         */
        public void setDebugOnPotentialSpawn(Boolean value)
        {
            this.debugOnPotentialSpawn = value;
        }

        /**
         *
         * @return
         */
        public Boolean getDebugOnRightClick()
        {
            return this.debugOnRightClick;
        }

        /**
         *
         * @param value
         */
        public void setDebugOnRightClick(Boolean value)
        {
            this.debugOnRightClick = value;
        }
    }
}
