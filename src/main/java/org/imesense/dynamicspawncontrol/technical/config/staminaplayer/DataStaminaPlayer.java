package org.imesense.dynamicspawncontrol.technical.config.staminaplayer;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;

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
        public void setShowBar(Boolean value)
        {
            this.showBar = value;
        }

        /**
         *
         * @param value
         */
        public void setXOffset(Integer value)
        {
            this.xOffset = value;
        }

        /**
         *
         * @param value
         */
        public void setYOffset(Integer value)
        {
            this.yOffset = value;
        }

        /**
         *
         * @param value
         */
        public void setIncreaseMultiplier(Double value)
        {
            this.increaseMultiplier = value;
        }

        /**
         *
         * @param value
         */
        public void setMaxStamina(Float value)
        {
            this.maxStamina = value;
        }

        /**
         *
         * @param value
         */
        public void setWalking(Float value)
        {
            this.walking = value;
        }

        /**
         *
         * @param value
         */
        public void setStanding(Float value)
        {
            this.standing = value;
        }

        /**
         *
         * @param value
         */
        public void setSprinting(Float value)
        {
            this.sprinting = value;
        }

        /**
         *
         * @param value
         */
        public void setSneaking(Float value)
        {
            this.sneaking = value;
        }

        /**
         *
         * @param value
         */
        public void setJumping(Float value)
        {
            this.jumping = value;
        }

        /**
         *
         * @return
         */
        public Boolean getShowBar()
        {
            return this.showBar;
        }

        /**
         *
         * @return
         */
        public Integer getXOffset()
        {
            return this.xOffset;
        }

        /**
         *
         * @return
         */
        public Integer getYOffset()
        {
            return this.yOffset;
        }

        /**
         *
         * @return
         */
        public Double getIncreaseMultiplier()
        {
            return this.increaseMultiplier;
        }

        /**
         *
         * @return
         */
        public Float getMaxStamina()
        {
            return this.maxStamina;
        }

        /**
         *
         * @return
         */
        public Float getWalking()
        {
            return this.walking;
        }

        /**
         *
         * @return
         */
        public Float getStanding()
        {
            return this.standing;
        }

        /**
         *
         * @return
         */
        public Float getSprinting()
        {
            return this.sprinting;
        }

        /**
         *
         * @return
         */
        public Float getSneaking()
        {
            return this.sneaking;
        }

        /**
         *
         * @return
         */
        public Float getJumping()
        {
            return this.jumping;
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
