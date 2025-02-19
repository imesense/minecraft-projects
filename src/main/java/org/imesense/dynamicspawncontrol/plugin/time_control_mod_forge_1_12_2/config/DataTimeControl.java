package org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.config;

import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import javax.annotation.Nonnull;

/**
 *
 */
public final class DataTimeControl
{
    /**
     *
     */
    public static final class ConfigDataWorldTime
    {
        /**
         *
         */
        private final String CATEGORY;

        /**
         *
         */
        public static ConfigDataWorldTime Instance;

        /**
         *
         */
        private Integer dayLengthMinutes = 10;

        /**
         *
         */
        private Integer nightLengthMinutes = 10;

        /**
         *
         */
        private Integer syncToSystemTimeRate = 20;

        /**
         *
         */
        private Boolean timeControlDebug = false;

        /**
         *
         */
        public Boolean SyncToSystemTime = false;

        /**
         *
         * @param CATEGORY
         */
        public ConfigDataWorldTime(@Nonnull final String CATEGORY)
        {
			CodeGeneric.printInitClassToLog(this.getClass());
			
            this.CATEGORY = CATEGORY;
        }

        /**
         *
         * @return
         */
        public Integer getDayLengthMinutes()
        {
            return this.dayLengthMinutes;
        }

        /**
         *
         * @return
         */
        public Integer getNightLengthMinutes()
        {
            return this.nightLengthMinutes;
        }

        /**
         *
         * @return
         */
        public Integer getSyncToSystemTimeRate()
        {
            return this.syncToSystemTimeRate;
        }

        /**
         *
         * @return
         */
        public Boolean getTimeControlDebug()
        {
            return this.timeControlDebug;
        }

        /**
         *
         * @return
         */
        public Boolean getSyncToSystemTime()
        {
            return this.SyncToSystemTime;
        }

        /**
         *
         * @param value
         */
        public void setDayLengthMinutes(Integer value)
        {
            this.dayLengthMinutes = value;
        }

        /**
         *
         * @param value
         */
        public void setNightLengthMinutes(Integer value)
        {
            this.nightLengthMinutes = value;
        }

        /**
         *
         * @param value
         */
        public void setSyncToSystemTimeRate(Integer value)
        {
            this.syncToSystemTimeRate = value;
        }

        /**
         *
         * @param value
         */
        public void setTimeControlDebug(Boolean value)
        {
            this.timeControlDebug = value;
        }

        /**
         *
         * @param value
         */
        public void setSyncToSystemTime(Boolean value)
        {
            this.SyncToSystemTime = value;
        }

        /**
         *
         * @return
         */
        public String getCategoryObject()
        {
            return this.CATEGORY;
        }
    }
}
