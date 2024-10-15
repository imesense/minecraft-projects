package org.imesense.dynamicspawncontrol.technical.config.spiderattackweb;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;

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
        private final String category;

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
        private Integer slingCooldown = 45;

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
         * @param category
         */
        public ConfigDataSpiderAttackWeb(@Nonnull final String category)
        {
            CodeGenericUtils.printInitClassToLog(this.getClass());
            this.category = category;
        }

        /**
         *
         * @param blockWebReplacement
         */
        public void setBlockWebReplacement(Boolean blockWebReplacement)
        {
            this.blockWebReplacement = blockWebReplacement;
        }

        /**
         *
         * @param webMeleeChance
         */
        public void setWebMeleeChance(Float webMeleeChance)
        {
            this.webMeleeChance = webMeleeChance;
        }

        /**
         *
         * @param slingCooldown
         */
        public void setSlingCooldown(Integer slingCooldown)
        {
            this.slingCooldown = slingCooldown;
        }

        /**
         *
         * @param slingInaccuracy
         */
        public void setSlingInaccuracy(Float slingInaccuracy)
        {
            this.slingInaccuracy = slingInaccuracy;
        }

        /**
         *
         * @param slingVariance
         */
        public void setSlingVariance(Float slingVariance)
        {
            this.slingVariance = slingVariance;
        }

        /**
         *
         * @param slingWebbing
         */
        public void setSlingWebbing(Boolean slingWebbing)
        {
            this.slingWebbing = slingWebbing;
        }

        /**
         *
         * @param slingWebbingOnWeb
         */
        public void setSlingWebbingOnWeb(Boolean slingWebbingOnWeb)
        {
            this.slingWebbingOnWeb = slingWebbingOnWeb;
        }

        /**
         *
         * @param AIPrioritySlingWebs
         */
        public void setAIPrioritySlingWebs(Integer AIPrioritySlingWebs)
        {
            this.AIPrioritySlingWebs = AIPrioritySlingWebs;
        }

        /**
         *
         * @param debugInfo
         */
        public void setDebugInfo(Boolean debugInfo)
        {
            this.debugInfo = debugInfo;
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
        public Integer getSlingCooldown()
        {
            return this.slingCooldown;
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
            return category;
        }
    }
}