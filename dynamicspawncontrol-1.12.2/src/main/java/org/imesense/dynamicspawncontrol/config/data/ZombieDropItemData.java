package org.imesense.dynamicspawncontrol.config.data;

import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import javax.annotation.Nonnull;

/**
 *
 */
public final class ZombieDropItemData
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
        public static ConfigDataZombieDrop Instance;

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
            CodeGeneric.printInitClassToLog(this.getClass());
            this.CATEGORY = CATEGORY;
        }

        /**
         *
         * @param value
         */
        public void setBreakItem(Float value)
        {
            this.breakItem = value;
        }

        /**
         *
         * @param value
         */
        public void setHandItemDamageFactor(Float value)
        {
            this.handItemDamageFactor = value;
        }

        /**
         *
         * @param value
         */
        public void setHeadDamageFactor(Float value)
        {
            this.headDamageFactor = value;
        }

        /**
         *
         * @param value
         */
        public void setChestDamageFactor(Float value)
        {
            this.chestDamageFactor = value;
        }

        /**
         *
         * @param value
         */
        public void setLegsDamageFactor(Float value)
        {
            this.legsDamageFactor = value;
        }

        /**
         *
         * @param value
         */
        public void setFeetDamageFactor(Float value)
        {
            this.feetDamageFactor = value;
        }

        /**
         *
         * @param value
         */
        public void setDamageSpreadFactor(Float value)
        {
            this.damageSpreadFactor = value;
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

