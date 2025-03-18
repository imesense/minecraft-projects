package org.imesense.dynamicspawncontrol.core.plugin.mod.webslinger_1_12_2_2_2_4.config;

import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public final class DataWebSlinger
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
        public static ConfigDataSpiderAttackWeb Instance;

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
        private Double slingCoolDown = 45.00;

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
        private String[] entityIds = { "minecraft:spider" };

        private Map<String, Integer> entityIdPriorityMap;

        public Map<String, Integer> getEntityIdPriorityMap() {
            return entityIdPriorityMap;
        }
        public Integer getEntityPriority(String entityId) {
            return entityIdPriorityMap.getOrDefault(entityId, -1);
        }
        public void setEntityIdPriorityMap(Map<String, Integer> entityIdPriorityMap) {
            this.entityIdPriorityMap = entityIdPriorityMap;
        }

        /**
         *
         * @param CATEGORY
         */
        public ConfigDataSpiderAttackWeb(@Nonnull final String CATEGORY)
        {
            CodeGeneric.printInitClassToLog(this.getClass());
            this.CATEGORY = CATEGORY;

            entityIdPriorityMap = new HashMap<>();
            entityIdPriorityMap.put("minecraft:spider", 3);
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
         */
        public void setEntityIds(String[] value)
        {
            this.entityIds = value;
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
        public String[] getEntityIds()
        {
            return this.entityIds;
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
