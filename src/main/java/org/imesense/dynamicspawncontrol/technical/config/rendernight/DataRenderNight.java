package org.imesense.dynamicspawncontrol.technical.config.rendernight;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;

import javax.annotation.Nonnull;

/**
 *
 */
public final class DataRenderNight
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
        public static ConfigDataRenderNight instance;

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
        private Integer[] blacklistByID = {};

        /**
         *
         */
        private Double[] moonPhaseFactors = { 0.6, 0.4, 0.3, 0.2, 0.0, 0.1, 0.2, 0.4 };

        /**
         *
         */
        private String[] blacklistByName = {};

        /**
         *
         * @param CATEGORY
         */
        public ConfigDataRenderNight(@Nonnull final String CATEGORY)
        {
            CodeGenericUtil.printInitClassToLog(this.getClass());

            this.CATEGORY = CATEGORY;
        }

        /**
         *
         * @param darknessOverWorld
         */
        public void setDarknessOverWorld(Boolean darknessOverWorld)
        {
            this.darknessOverWorld = darknessOverWorld;
        }

        /**
         *
         * @param darknessNether
         */
        public void setDarknessNether(Boolean darknessNether)
        {
            this.darknessNether = darknessNether;
        }

        /**
         *
         * @param darknessEnd
         */
        public void setDarknessEnd(Boolean darknessEnd)
        {
            this.darknessEnd = darknessEnd;
        }

        /**
         *
         * @param darknessDefault
         */
        public void setDarknessDefault(Boolean darknessDefault)
        {
            this.darknessDefault = darknessDefault;
        }

        /**
         *
         * @param darknessSkyLess
         */
        public void setDarknessSkyLess(Boolean darknessSkyLess)
        {
            this.darknessSkyLess = darknessSkyLess;
        }

        /**
         *
         * @param darknessNetherFog
         */
        public void setDarknessNetherFog(Boolean darknessNetherFog)
        {
            this.darknessNetherFog = darknessNetherFog;
        }

        /**
         *
         * @param darknessEndFog
         */
        public void setDarknessEndFog(Boolean darknessEndFog)
        {
            this.darknessEndFog = darknessEndFog;
        }

        /**
         *
         * @param ignoreMoonLight
         */
        public void setIgnoreMoonLight(Boolean ignoreMoonLight)
        {
            this.ignoreMoonLight = ignoreMoonLight;
        }

        /**
         *
         * @param invertBlacklist
         */
        public void setInvertBlacklist(Boolean invertBlacklist)
        {
            this.invertBlacklist = invertBlacklist;
        }

        /**
         *
         * @param blacklistByID
         */
        public void setBlacklistByID(Integer[] blacklistByID)
        {
            this.blacklistByID = blacklistByID;
        }

        /**
         *
         * @param moonPhaseFactors
         */
        public void setMoonPhaseFactors(Double[] moonPhaseFactors)
        {
            this.moonPhaseFactors = moonPhaseFactors;
        }

        /**
         *
         * @param blacklistByName
         */
        public void setBlacklistByName(String[] blacklistByName)
        {
            this.blacklistByName = blacklistByName;
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
