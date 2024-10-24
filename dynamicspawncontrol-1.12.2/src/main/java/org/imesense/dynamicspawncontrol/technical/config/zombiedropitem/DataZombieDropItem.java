package org.imesense.dynamicspawncontrol.technical.config.zombiedropitem;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;

import javax.annotation.Nonnull;

/**
 *
 */
public final class DataZombieDropItem
{
    /**
     *
     */
    public static final class ConfigDataZombieDrop
    {
        /**
         *
         */
        private final String CATEGORY;

        /**
         *
         */
        public static ConfigDataZombieDrop instance;

        /**
         *
         */
        private Float breakItem = 0.15f;

        /**
         *
         */
        private Float handItemDamageFactor = 0.85f;

        /**
         *
         */
        private Float headDamageFactor = 0.9f;

        /**
         *
         */
        private Float chestDamageFactor = 0.9f;

        /**
         *
         */
        private Float legsDamageFactor = 0.9f;

        /**
         *
         */
        private Float feetDamageFactor = 0.9f;

        /**
         *
         */
        private Float damageSpreadFactor = 0.2f;

        /**
         *
         * @param CATEGORY
         */
        public ConfigDataZombieDrop(@Nonnull final String CATEGORY)
        {
            CodeGenericUtil.printInitClassToLog(this.getClass());
            this.CATEGORY = CATEGORY;
        }

        /**
         *
         * @param breakItem
         */
        public void setBreakItem(Float breakItem)
        {
            this.breakItem = breakItem;
        }

        /**
         *
         * @param handItemDamageFactor
         */
        public void setHandItemDamageFactor(Float handItemDamageFactor)
        {
            this.handItemDamageFactor = handItemDamageFactor;
        }

        /**
         *
         * @param headDamageFactor
         */
        public void setHeadDamageFactor(Float headDamageFactor)
        {
            this.headDamageFactor = headDamageFactor;
        }

        /**
         *
         * @param chestDamageFactor
         */
        public void setChestDamageFactor(Float chestDamageFactor)
        {
            this.chestDamageFactor = chestDamageFactor;
        }

        /**
         *
         * @param legsDamageFactor
         */
        public void setLegsDamageFactor(Float legsDamageFactor)
        {
            this.legsDamageFactor = legsDamageFactor;
        }

        /**
         *
         * @param feetDamageFactor
         */
        public void setFeetDamageFactor(Float feetDamageFactor)
        {
            this.feetDamageFactor = feetDamageFactor;
        }

        /**
         *
         * @param damageSpreadFactor
         */
        public void setDamageSpreadFactor(Float damageSpreadFactor)
        {
            this.damageSpreadFactor = damageSpreadFactor;
        }

        /**
         *
         * @return
         */
        public Float getBreakItem()
        {
            return this.breakItem;
        }

        /**
         *
         * @return
         */
        public Float getHandItemDamageFactor()
        {
            return this.handItemDamageFactor;
        }

        /**
         *
         * @return
         */
        public Float getHeadDamageFactor()
        {
            return this.headDamageFactor;
        }

        /**
         *
         * @return
         */
        public Float getChestDamageFactor()
        {
            return this.chestDamageFactor;
        }

        /**
         *
         * @return
         */
        public Float getLegsDamageFactor()
        {
            return this.legsDamageFactor;
        }

        /**
         *
         * @return
         */
        public Float getFeetDamageFactor()
        {
            return this.feetDamageFactor;
        }

        /**
         *
         * @return
         */
        public Float getDamageSpreadFactor()
        {
            return this.damageSpreadFactor;
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

