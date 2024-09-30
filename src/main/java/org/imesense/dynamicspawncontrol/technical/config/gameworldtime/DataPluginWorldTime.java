package org.imesense.dynamicspawncontrol.technical.config.gameworldtime;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;

import javax.annotation.Nonnull;

/**
 *
 */
public class DataPluginWorldTime
{
    /**
     *
     */
    public static final class worldTime
    {
        /**
         *
         */
        final String setCategory;

        /**
         *
         */
        public static worldTime instance;

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
        public Boolean syncToSystemTime = false;

        /**
         *
         * @param category
         */
        public worldTime(@Nonnull final String category)
        {
			CodeGenericUtils.printInitClassToLog(this.getClass());
			
            this.setCategory = category;
        }

        /**
         *
         * @return
         */
        public Integer getDayLengthMinutes()
        {
            return dayLengthMinutes;
        }

        /**
         *
         * @return
         */
        public Integer getNightLengthMinutes()
        {
            return nightLengthMinutes;
        }

        /**
         *
         * @return
         */
        public Integer getSyncToSystemTimeRate()
        {
            return syncToSystemTimeRate;
        }

        /**
         *
         * @return
         */
        public Boolean getTimeControlDebug()
        {
            return timeControlDebug;
        }

        /**
         *
         * @return
         */
        public Boolean getSyncToSystemTime()
        {
            return syncToSystemTime;
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
    }
}
