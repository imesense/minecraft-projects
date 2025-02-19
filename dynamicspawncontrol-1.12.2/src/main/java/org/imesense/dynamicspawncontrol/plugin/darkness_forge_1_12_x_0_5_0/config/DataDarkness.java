package org.imesense.dynamicspawncontrol.plugin.darkness_forge_1_12_x_0_5_0.config;

import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

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
        public void setDarknessOverWorld(Boolean value)
        {
            this.darknessOverWorld = value;
        }

        /**
         *
         * @param value
         */
        public void setDarknessNether(Boolean value)
        {
            this.darknessNether = value;
        }

        /**
         *
         * @param value
         */
        public void setDarknessEnd(Boolean value)
        {
            this.darknessEnd = value;
        }

        /**
         *
         * @param value
         */
        public void setDarknessDefault(Boolean value)
        {
            this.darknessDefault = value;
        }

        /**
         *
         * @param value
         */
        public void setDarknessSkyLess(Boolean value)
        {
            this.darknessSkyLess = value;
        }

        /**
         *
         * @param value
         */
        public void setDarknessNetherFog(Boolean value)
        {
            this.darknessNetherFog = value;
        }

        /**
         *
         * @param value
         */
        public void setDarknessEndFog(Boolean value)
        {
            this.darknessEndFog = value;
        }

        /**
         *
         * @param value
         */
        public void setIgnoreMoonLight(Boolean value)
        {
            this.ignoreMoonLight = value;
        }

        /**
         *
         * @param value
         */
        public void setInvertBlacklist(Boolean value)
        {
            this.invertBlacklist = value;
        }

        /**
         *
         * @param value
         */
        public void setBlacklistByID(Integer[] value)
        {
            this.blacklistByID = value;
        }

        /**
         *
         * @param value
         */
        public void setMoonPhaseFactors(Double[] value)
        {
            this.moonPhaseFactors = value;
        }

        /**
         *
         * @param value
         */
        public void setBlacklistByName(String[] value)
        {
            this.blacklistByName = value;
        }

        /**
         *
         * @return
         */
        public Boolean getDarknessOverWorld()
        {
            return this.darknessOverWorld;
        }

        /**
         *
         * @return
         */
        public Boolean getDarknessNether()
        {
            return this.darknessNether;
        }

        /**
         *
         * @return
         */
        public Boolean getDarknessEnd()
        {
            return this.darknessEnd;
        }

        /**
         *
         * @return
         */
        public Boolean getDarknessDefault()
        {
            return this.darknessDefault;
        }

        /**
         *
         * @return
         */
        public Boolean getDarknessSkyLess()
        {
            return this.darknessSkyLess;
        }

        /**
         *
         * @return
         */
        public Boolean getDarknessNetherFog()
        {
            return this.darknessNetherFog;
        }

        /**
         *
         * @return
         */
        public Boolean getDarknessEndFog()
        {
            return this.darknessEndFog;
        }

        /**
         *
         * @return
         */
        public Boolean getIgnoreMoonLight()
        {
            return this.ignoreMoonLight;
        }

        /**
         *
         * @return
         */
        public Boolean getInvertBlacklist()
        {
            return this.invertBlacklist;
        }

        /**
         *
         * @return
         */
        public Integer[] getBlacklistByID()
        {
            return this.blacklistByID;
        }

        /**
         *
         * @return
         */
        public Double[] getMoonPhaseFactors()
        {
            return this.moonPhaseFactors;
        }

        /**
         *
         * @return
         */
        public String[] getBlacklistByName()
        {
            return this.blacklistByName;
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
