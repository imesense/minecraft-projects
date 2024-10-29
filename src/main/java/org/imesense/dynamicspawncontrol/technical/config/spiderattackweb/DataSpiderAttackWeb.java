package org.imesense.dynamicspawncontrol.technical.config.spiderattackweb;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;

import javax.annotation.Nonnull;

/**
 *
 */
public final class DataSpiderAttackWeb
{
    /**
     *
     */
    public static final class ConfigDataSpiderAttackWeb
    {
        /**
         *
         */
        private final String CATEGORY;

        /**
         *
         */
        public static ConfigDataSpiderAttackWeb instance;

        /**
         *
         */
        private Boolean blockWebReplacement = true;

        /**
         *
         */
        private Float webMeleeChance = 0.15f;

        /**
         *
         */
        private Double slingCoolDown = 45.0;

        /**
         *
         */
        private Float slingInaccuracy = 6.f;

        /**
         *
         */
        private Float slingVariance = 2.f;

        /**
         *
         */
        private Boolean slingWebbing = true;

        /**
         *
         */
        private Boolean slingWebbingOnWeb = false;

        /**
         *
         */
        private Integer AIPrioritySlingWebs = 3;

        /**
         *
         */
        private Boolean debugInfo = false;

        /**
         *
         * @param CATEGORY
         */
        public ConfigDataSpiderAttackWeb(@Nonnull final String CATEGORY)
        {
            CodeGenericUtil.printInitClassToLog(this.getClass());
            this.CATEGORY = CATEGORY;
        }

        /**
         *
         * @param value
         */
        public void setBlockWebReplacement(Boolean value)
        {
            this.blockWebReplacement = value;
        }

        /**
         *
         * @param value
         */
        public void setWebMeleeChance(Float value)
        {
            this.webMeleeChance = value;
        }

        /**
         *
         * @param value
         */
        public void setSlingCoolDown(Double value)
        {
            this.slingCoolDown = value;
        }

        /**
         *
         * @param value
         */
        public void setSlingInaccuracy(Float value)
        {
            this.slingInaccuracy = value;
        }

        /**
         *
         * @param value
         */
        public void setSlingVariance(Float value)
        {
            this.slingVariance = value;
        }

        /**
         *
         * @param value
         */
        public void setSlingWebbing(Boolean value)
        {
            this.slingWebbing = value;
        }

        /**
         *
         * @param value
         */
        public void setSlingWebbingOnWeb(Boolean value)
        {
            this.slingWebbingOnWeb = value;
        }

        /**
         *
         * @param value
         */
        public void setAIPrioritySlingWebs(Integer value)
        {
            this.AIPrioritySlingWebs = value;
        }

        /**
         *
         * @param value
         */
        public void setDebugInfo(Boolean value)
        {
            this.debugInfo = value;
        }

        /**
         *
         * @return
         */
        public Boolean getBlockWebReplacement()
        {
            return this.blockWebReplacement;
        }

        /**
         *
         * @return
         */
        public Float getWebMeleeChance()
        {
            return this.webMeleeChance;
        }

        /**
         *
         * @return
         */
        public Double getSlingCoolDown()
        {
            return this.slingCoolDown;
        }

        /**
         *
         * @return
         */
        public Float getSlingInaccuracy()
        {
            return this.slingInaccuracy;
        }

        /**
         *
         * @return
         */
        public Float getSlingVariance()
        {
            return this.slingVariance;
        }

        /**
         *
         * @return
         */
        public Boolean getSlingWebbing()
        {
            return this.slingWebbing;
        }

        /**
         *
         * @return
         */
        public Boolean getSlingWebbingOnWeb()
        {
            return this.slingWebbingOnWeb;
        }

        /**
         *
         * @return
         */
        public Integer getAIPrioritySlingWebs()
        {
            return this.AIPrioritySlingWebs;
        }

        /**
         *
         * @return
         */
        public Boolean getDebugInfo()
        {
            return this.debugInfo;
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
