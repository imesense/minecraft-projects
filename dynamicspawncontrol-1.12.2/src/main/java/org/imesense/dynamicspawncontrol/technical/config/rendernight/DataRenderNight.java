package org.imesense.dynamicspawncontrol.technical.config.rendernight;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;

import javax.annotation.Nonnull;

/**
 *
 */
public final class DataRenderNight
{
    /**
     *
     */
    public static final class renderNight
    {
        /**
         *
         */
        final String setCategory;

        /**
         *
         */
        public static renderNight instance;

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
         * @param category
         */
        public renderNight(@Nonnull final String category)
        {
			CodeGenericUtils.printInitClassToLog(this.getClass());
			
            this.setCategory = category;
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
         * @param darknessOverWorld
         */
        public void setDarknessOverWorld(Boolean darknessOverWorld)
        {
            this.darknessOverWorld = darknessOverWorld;
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
         * @param darknessNether
         */
        public void setDarknessNether(Boolean darknessNether)
        {
            this.darknessNether = darknessNether;
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
         * @param darknessEnd
         */
        public void setDarknessEnd(Boolean darknessEnd)
        {
            this.darknessEnd = darknessEnd;
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
         * @param darknessDefault
         */
        public void setDarknessDefault(Boolean darknessDefault)
        {
            this.darknessDefault = darknessDefault;
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
         * @param darknessSkyLess
         */
        public void setDarknessSkyLess(Boolean darknessSkyLess)
        {
            this.darknessSkyLess = darknessSkyLess;
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
         * @param darknessNetherFog
         */
        public void setDarknessNetherFog(Boolean darknessNetherFog)
        {
            this.darknessNetherFog = darknessNetherFog;
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
         * @param darknessEndFog
         */
        public void setDarknessEndFog(Boolean darknessEndFog)
        {
            this.darknessEndFog = darknessEndFog;
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
         * @param ignoreMoonLight
         */
        public void setIgnoreMoonLight(Boolean ignoreMoonLight)
        {
            this.ignoreMoonLight = ignoreMoonLight;
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
         * @param invertBlacklist
         */
        public void setInvertBlacklist(Boolean invertBlacklist)
        {
            this.invertBlacklist = invertBlacklist;
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
         * @param blacklistByID
         */
        public void setBlacklistByID(Integer[] blacklistByID)
        {
            this.blacklistByID = blacklistByID;
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
         * @param moonPhaseFactors
         */
        public void setMoonPhaseFactors(Double[] moonPhaseFactors)
        {
            this.moonPhaseFactors = moonPhaseFactors;
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
         * @param blacklistByName
         */
        public void setBlacklistByName(String[] blacklistByName)
        {
            this.blacklistByName = blacklistByName;
        }
    }
}
