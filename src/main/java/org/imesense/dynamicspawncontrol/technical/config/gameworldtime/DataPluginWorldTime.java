package org.imesense.dynamicspawncontrol.technical.config.gameworldtime;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;

import javax.annotation.Nonnull;

/**
 *
 */
public final class DataPluginWorldTime
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
        public static ConfigDataWorldTime instance;

        /**
         *
         */
        private Integer dayLengthMinutes = 12;

        /**
         *
         */
        private Integer nightLengthMinutes = 12;

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
        public Boolean syncToSystemTime = false;

        /**
         *
         * @param CATEGORY
         */
        public ConfigDataWorldTime(@Nonnull final String CATEGORY)
        {
			CodeGenericUtil.printInitClassToLog(this.getClass());
			
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
            return this.syncToSystemTime;
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
            this.syncToSystemTime = value;
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
