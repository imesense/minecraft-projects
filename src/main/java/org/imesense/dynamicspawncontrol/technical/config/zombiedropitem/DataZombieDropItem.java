package org.imesense.dynamicspawncontrol.technical.config.zombiedropitem;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;

import javax.annotation.Nonnull;

/**
 *
 */
public final class DataZombieDropItem
{
    /**
     *
     */
    public static final class zombieDrop
    {
        /**
         *
         */
        private final String category;

        /**
         *
         */
        public static zombieDrop instance;

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
         * @param category
         */
        public zombieDrop(@Nonnull final String category)
        {
			CodeGenericUtils.printInitClassToLog(this.getClass());
			
            this.category = category;
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
         * @param breakItem
         */
        public void setBreakItem(Float breakItem)
        {
            this.breakItem = breakItem;
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
         * @param handItemDamageFactor
         */
        public void setHandItemDamageFactor(Float handItemDamageFactor)
        {
            this.handItemDamageFactor = handItemDamageFactor;
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
         * @param headDamageFactor
         */
        public void setHeadDamageFactor(Float headDamageFactor)
        {
            this.headDamageFactor = headDamageFactor;
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
         * @param chestDamageFactor
         */
        public void setChestDamageFactor(Float chestDamageFactor)
        {
            this.chestDamageFactor = chestDamageFactor;
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
         * @param legsDamageFactor
         */
        public void setLegsDamageFactor(Float legsDamageFactor)
        {
            this.legsDamageFactor = legsDamageFactor;
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
         * @param feetDamageFactor
         */
        public void setFeetDamageFactor(Float feetDamageFactor)
        {
            this.feetDamageFactor = feetDamageFactor;
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
        public String getCategoryObject()
        {
            return this.category;
        }
    }
}
