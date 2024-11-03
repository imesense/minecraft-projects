package org.imesense.dynamicspawncontrol.technical.config.zombiedropitem;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.technical.customlibrary.inlineannotation.Getter;
import org.imesense.dynamicspawncontrol.technical.customlibrary.inlineannotation.Setter;

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
            CodeGenericUtil.printInitClassToLog(this.getClass());
            this.CATEGORY = CATEGORY;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setBreakItem(Float value)
        {
            this.breakItem = value;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setHandItemDamageFactor(Float value)
        {
            this.handItemDamageFactor = value;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setHeadDamageFactor(Float value)
        {
            this.headDamageFactor = value;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setChestDamageFactor(Float value)
        {
            this.chestDamageFactor = value;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setLegsDamageFactor(Float value)
        {
            this.legsDamageFactor = value;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setFeetDamageFactor(Float value)
        {
            this.feetDamageFactor = value;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setDamageSpreadFactor(Float value)
        {
            this.damageSpreadFactor = value;
        }

        /**
         *
         * @return
         */
        @Getter
        public Float getBreakItem()
        {
            return this.breakItem;
        }

        /**
         *
         * @return
         */
        @Getter
        public Float getHandItemDamageFactor()
        {
            return this.handItemDamageFactor;
        }

        /**
         *
         * @return
         */
        @Getter
        public Float getHeadDamageFactor()
        {
            return this.headDamageFactor;
        }

        /**
         *
         * @return
         */
        @Getter
        public Float getChestDamageFactor()
        {
            return this.chestDamageFactor;
        }

        /**
         *
         * @return
         */
        @Getter
        public Float getLegsDamageFactor()
        {
            return this.legsDamageFactor;
        }

        /**
         *
         * @return
         */
        @Getter
        public Float getFeetDamageFactor()
        {
            return this.feetDamageFactor;
        }

        /**
         *
         * @return
         */
        @Getter
        public Float getDamageSpreadFactor()
        {
            return this.damageSpreadFactor;
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

