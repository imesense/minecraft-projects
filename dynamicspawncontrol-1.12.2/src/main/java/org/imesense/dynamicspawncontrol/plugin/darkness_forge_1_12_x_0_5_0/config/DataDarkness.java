package org.imesense.dynamicspawncontrol.plugin.darkness_forge_1_12_x_0_5_0.config;

import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.annotation.Getter;
import org.imesense.dynamicspawncontrol.core.annotation.Setter;

import javax.annotation.Nonnull;

/**
 *
 */
public final class DataDarkness
{
    /**
     *
     */
    public static final class ConfigDataRenderNight
    {
        /**
         *
         */
        private final String CATEGORY;

        /**
         *
         */
        public static ConfigDataRenderNight Instance;

        /**
         *
         */
        private Boolean darknessOverWorld = true;

        /**
         *
         */
        private Boolean darknessNether = true;

        /**
         *
         */
        private Boolean darknessEnd = true;

        /**
         *
         */
        private Boolean darknessDefault = true;

        /**
         *
         */
        private Boolean darknessSkyLess = true;

        /**
         *
         */
        private Boolean darknessNetherFog = true;

        /**
         *
         */
        private Boolean darknessEndFog = true;

        /**
         *
         */
        private Boolean ignoreMoonLight = false;

        /**
         *
         */
        private Boolean invertBlacklist = false;

        /**
         *
         */
        private Integer[] blacklistByID = { };

        /**
         *
         */
        private Double[] moonPhaseFactors = { 0.6, 0.4, 0.3, 0.2, 0.0, 0.1, 0.2, 0.4 };

        /**
         *
         */
        private String[] blacklistByName = { };

        /**
         *
         * @param CATEGORY
         */
        public ConfigDataRenderNight(@Nonnull final String CATEGORY)
        {
            CodeGeneric.printInitClassToLog(this.getClass());

            this.CATEGORY = CATEGORY;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setDarknessOverWorld(Boolean value)
        {
            this.darknessOverWorld = value;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setDarknessNether(Boolean value)
        {
            this.darknessNether = value;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setDarknessEnd(Boolean value)
        {
            this.darknessEnd = value;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setDarknessDefault(Boolean value)
        {
            this.darknessDefault = value;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setDarknessSkyLess(Boolean value)
        {
            this.darknessSkyLess = value;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setDarknessNetherFog(Boolean value)
        {
            this.darknessNetherFog = value;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setDarknessEndFog(Boolean value)
        {
            this.darknessEndFog = value;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setIgnoreMoonLight(Boolean value)
        {
            this.ignoreMoonLight = value;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setInvertBlacklist(Boolean value)
        {
            this.invertBlacklist = value;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setBlacklistByID(Integer[] value)
        {
            this.blacklistByID = value;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setMoonPhaseFactors(Double[] value)
        {
            this.moonPhaseFactors = value;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setBlacklistByName(String[] value)
        {
            this.blacklistByName = value;
        }

        /**
         *
         * @return
         */
        @Getter
        public Boolean getDarknessOverWorld()
        {
            return this.darknessOverWorld;
        }

        /**
         *
         * @return
         */
        @Getter
        public Boolean getDarknessNether()
        {
            return this.darknessNether;
        }

        /**
         *
         * @return
         */
        @Getter
        public Boolean getDarknessEnd()
        {
            return this.darknessEnd;
        }

        /**
         *
         * @return
         */
        @Getter
        public Boolean getDarknessDefault()
        {
            return this.darknessDefault;
        }

        /**
         *
         * @return
         */
        @Getter
        public Boolean getDarknessSkyLess()
        {
            return this.darknessSkyLess;
        }

        /**
         *
         * @return
         */
        @Getter
        public Boolean getDarknessNetherFog()
        {
            return this.darknessNetherFog;
        }

        /**
         *
         * @return
         */
        @Getter
        public Boolean getDarknessEndFog()
        {
            return this.darknessEndFog;
        }

        /**
         *
         * @return
         */
        @Getter
        public Boolean getIgnoreMoonLight()
        {
            return this.ignoreMoonLight;
        }

        /**
         *
         * @return
         */
        @Getter
        public Boolean getInvertBlacklist()
        {
            return this.invertBlacklist;
        }

        /**
         *
         * @return
         */
        @Getter
        public Integer[] getBlacklistByID()
        {
            return this.blacklistByID;
        }

        /**
         *
         * @return
         */
        @Getter
        public Double[] getMoonPhaseFactors()
        {
            return this.moonPhaseFactors;
        }

        /**
         *
         * @return
         */
        @Getter
        public String[] getBlacklistByName()
        {
            return this.blacklistByName;
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
