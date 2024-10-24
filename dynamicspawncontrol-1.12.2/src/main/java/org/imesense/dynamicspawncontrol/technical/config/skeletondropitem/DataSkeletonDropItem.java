package org.imesense.dynamicspawncontrol.technical.config.skeletondropitem;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 *
 */
public final class DataSkeletonDropItem
{
    /**
     *
     */
    public static final class ConfigDataSkeletonDrop
    {
        /**
         *
         */
        private final String CATEGORY;

        /**
         *
         */
        public static DataSkeletonDropItem.ConfigDataSkeletonDrop instance;

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
         */
        private Byte arrowsToDrops = (byte)(1 + new Random().nextInt(3));

        /**
         *
         * @param CATEGORY
         */
        public ConfigDataSkeletonDrop(@Nonnull final String CATEGORY)
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
         * @param arrowsToDrops
         */
        public void setArrowsToDrops(Byte arrowsToDrops)
        {
            this.arrowsToDrops = arrowsToDrops;
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
        public Byte getArrowsToDrops()
        {
            return this.arrowsToDrops;
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
