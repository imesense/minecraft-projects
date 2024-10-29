package org.imesense.dynamicspawncontrol.technical.config.staminaplayer;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.technical.customlibrary.inlineannotation.Getter;
import org.imesense.dynamicspawncontrol.technical.customlibrary.inlineannotation.Setter;

import javax.annotation.Nonnull;

/**
 *
 */
public final class DataStaminaPlayer
{
    /**
     *
     */
    public static final class ConfigDataStaminaPlayer
    {
        /**
         *
         */
        private final String CATEGORY;

        /**
         *
         */
        public static ConfigDataStaminaPlayer instance;

        /**
         *
         */
        private Boolean showBar = true;

        /**
         *
         */
        private Integer xOffset = 0;

        /**
         *
         */
        private Integer yOffset = 0;

        /**
         *
         */
        private Double increaseMultiplier = 1.50;

        /**
         *
         */
        private Float maxStamina = 200.f;

        /**
         *
         */
        private Float walking = 2.5f;

        /**
         *
         */
        private Float standing = 1.5f;

        /**
         *
         */
        private Float sprinting = 3.f;

        /**
         *
         */
        private Float sneaking = 2.0f;

        /**
         *
         */
        private Float jumping = 10.f;

        /**
         *
         * @param CATEGORY
         */
        public ConfigDataStaminaPlayer(@Nonnull final String CATEGORY)
        {
            CodeGenericUtil.printInitClassToLog(this.getClass());
            this.CATEGORY = CATEGORY;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setShowBar(Boolean value)
        {
            this.showBar = value;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setXOffset(Integer value)
        {
            this.xOffset = value;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setYOffset(Integer value)
        {
            this.yOffset = value;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setIncreaseMultiplier(Double value)
        {
            this.increaseMultiplier = value;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setMaxStamina(Float value)
        {
            this.maxStamina = value;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setWalking(Float value)
        {
            this.walking = value;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setStanding(Float value)
        {
            this.standing = value;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setSprinting(Float value)
        {
            this.sprinting = value;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setSneaking(Float value)
        {
            this.sneaking = value;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setJumping(Float value)
        {
            this.jumping = value;
        }

        /**
         *
         * @return
         */
        @Getter
        public Boolean getShowBar()
        {
            return this.showBar;
        }

        /**
         *
         * @return
         */
        @Getter
        public Integer getXOffset()
        {
            return this.xOffset;
        }

        /**
         *
         * @return
         */
        @Getter
        public Integer getYOffset()
        {
            return this.yOffset;
        }

        /**
         *
         * @return
         */
        @Getter
        public Double getIncreaseMultiplier()
        {
            return this.increaseMultiplier;
        }

        /**
         *
         * @return
         */
        @Getter
        public Float getMaxStamina()
        {
            return this.maxStamina;
        }

        /**
         *
         * @return
         */
        @Getter
        public Float getWalking()
        {
            return this.walking;
        }

        /**
         *
         * @return
         */
        @Getter
        public Float getStanding()
        {
            return this.standing;
        }

        /**
         *
         * @return
         */
        @Getter
        public Float getSprinting()
        {
            return this.sprinting;
        }

        /**
         *
         * @return
         */
        @Getter
        public Float getSneaking()
        {
            return this.sneaking;
        }

        /**
         *
         * @return
         */
        @Getter
        public Float getJumping()
        {
            return this.jumping;
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
