package org.imesense.dynamicspawncontrol.config.data;

import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.annotation.Getter;
import org.imesense.dynamicspawncontrol.core.annotation.Setter;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 *
 */
public final class SkeletonDropItemData
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
        public static SkeletonDropItemData.ConfigDataSkeletonDrop Instance;

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
            CodeGeneric.printInitClassToLog(this.getClass());
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
         * @param value
         */
        @Setter
        public void setArrowsToDrops(Byte value)
        {
            this.arrowsToDrops = value;
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
        public Byte getArrowsToDrops()
        {
            return this.arrowsToDrops;
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
