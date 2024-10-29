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
        private final String CATEGORY;

        public static ConfigDataStaminaPlayer instance;

        private Boolean showBar = true;

        private Integer xOffset = 0;
        private Integer yOffset = 0;

        private Double increaseMultiplier = 1.50;

        private Float maxStamina = 200.f;
        private Float walking = 2.5f;
        private Float standing = 1.5f;
        private Float sprinting = 3.f;
        private Float sneaking = 2.0f;
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
         * @param showBar
         */
        public void setShowBar(Boolean showBar)
        {
            this.showBar = showBar;
        }

        /**
         *
         * @param xOffset
         */
        public void setXOffset(Integer xOffset)
        {
            this.xOffset = xOffset;
        }

        /**
         *
         * @param yOffset
         */
        public void setYOffset(Integer yOffset)
        {
            this.yOffset = yOffset;
        }

        /**
         *
         * @param increaseMultiplier
         */
        public void setIncreaseMultiplier(Double increaseMultiplier)
        {
            this.increaseMultiplier = increaseMultiplier;
        }

        /**
         *
         * @param maxStamina
         */
        public void setMaxStamina(Float maxStamina)
        {
            this.maxStamina = maxStamina;
        }

        /**
         *
         * @param walking
         */
        public void setWalking(Float walking)
        {
            this.walking = walking;
        }

        /**
         *
         * @param standing
         */
        public void setStanding(Float standing)
        {
            this.standing = standing;
        }

        /**
         *
         * @param sprinting
         */
        public void setSprinting(Float sprinting)
        {
            this.sprinting = sprinting;
        }

        /**
         *
         * @param sneaking
         */
        public void setSneaking(Float sneaking)
        {
            this.sneaking = sneaking;
        }

        /**
         *
         * @param jumping
         */
        public void setJumping(Float jumping)
        {
            this.jumping = jumping;
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
