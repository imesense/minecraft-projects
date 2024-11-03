package org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.config;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.technical.customlibrary.inlineannotation.Getter;
import org.imesense.dynamicspawncontrol.technical.customlibrary.inlineannotation.Setter;

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
			CodeGenericUtil.printInitClassToLog(this.getClass());
			
            this.CATEGORY = CATEGORY;
        }

        /**
         *
         * @return
         */
        @Getter
        public Integer getDayLengthMinutes()
        {
            return this.dayLengthMinutes;
        }

        /**
         *
         * @return
         */
        @Getter
        public Integer getNightLengthMinutes()
        {
            return this.nightLengthMinutes;
        }

        /**
         *
         * @return
         */
        @Getter
        public Integer getSyncToSystemTimeRate()
        {
            return this.syncToSystemTimeRate;
        }

        /**
         *
         * @return
         */
        @Getter
        public Boolean getTimeControlDebug()
        {
            return this.timeControlDebug;
        }

        /**
         *
         * @return
         */
        @Getter
        public Boolean getSyncToSystemTime()
        {
            return this.SyncToSystemTime;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setDayLengthMinutes(Integer value)
        {
            this.dayLengthMinutes = value;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setNightLengthMinutes(Integer value)
        {
            this.nightLengthMinutes = value;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setSyncToSystemTimeRate(Integer value)
        {
            this.syncToSystemTimeRate = value;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setTimeControlDebug(Boolean value)
        {
            this.timeControlDebug = value;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setSyncToSystemTime(Boolean value)
        {
            this.SyncToSystemTime = value;
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
