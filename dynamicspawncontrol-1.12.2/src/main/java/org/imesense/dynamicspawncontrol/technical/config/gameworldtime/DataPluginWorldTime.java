package org.imesense.dynamicspawncontrol.technical.config.gameworldtime;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;

import javax.annotation.Nonnull;

/**
 *
 */
public final class DataPluginWorldTime
{
    /**
     *
     */
    public static final class worldTime
    {
        /**
         *
         */
        private final String category;

        /**
         *
         */
        public static worldTime instance;

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
         * @param category
         */
        public worldTime(@Nonnull final String category)
        {
			CodeGenericUtils.printInitClassToLog(this.getClass());
			
            this.category = category;
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
         * @param dayLengthMinutes
         */
        public void setDayLengthMinutes(Integer dayLengthMinutes)
        {
            this.dayLengthMinutes = dayLengthMinutes;
        }

        /**
         *
         * @param nightLengthMinutes
         */
        public void setNightLengthMinutes(Integer nightLengthMinutes)
        {
            this.nightLengthMinutes = nightLengthMinutes;
        }

        /**
         *
         * @param syncToSystemTimeRate
         */
        public void setSyncToSystemTimeRate(Integer syncToSystemTimeRate)
        {
            this.syncToSystemTimeRate = syncToSystemTimeRate;
        }

        /**
         *
         * @param timeControlDebug
         */
        public void setTimeControlDebug(Boolean timeControlDebug)
        {
            this.timeControlDebug = timeControlDebug;
        }

        /**
         *
         * @param syncToSystemTime
         */
        public void setSyncToSystemTime(Boolean syncToSystemTime)
        {
            this.syncToSystemTime = syncToSystemTime;
        }

        /**
         *
         * @return
         */
        public String getCategoryObject()
        {
            return this.category;
        }
    }
}
