package org.imesense.dynamicspawncontrol.core.config.data;

import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import javax.annotation.Nonnull;

public final class SkeletonDropItemData
{
    public static final class ConfigDataSkeletonDrop
    {
        private final String CATEGORY;

        public static SkeletonDropItemData.ConfigDataSkeletonDrop Instance;

        private Float breakItem = 0.15f;

        private Float handItemDamageFactor = 0.85f;

        private Float headDamageFactor = 0.9f;

        private Float chestDamageFactor = 0.9f;

        private Float legsDamageFactor = 0.9f;

        private Float feetDamageFactor = 0.9f;

        private Float damageSpreadFactor = 0.2f;

        private Byte arrowsToDrops = (byte)(1 + UniqueField.RANDOM.nextInt(3));

        public ConfigDataSkeletonDrop(@Nonnull final String CATEGORY)
        {
            CodeGeneric.printInitClassToLog(this.getClass());
            this.CATEGORY = CATEGORY;
        }

        public void setBreakItem(Float value)
        {
            this.breakItem = value;
        }

        public void setHandItemDamageFactor(Float value)
        {
            this.handItemDamageFactor = value;
        }

        public void setHeadDamageFactor(Float value)
        {
            this.headDamageFactor = value;
        }

        public void setChestDamageFactor(Float value)
        {
            this.chestDamageFactor = value;
        }

        public void setLegsDamageFactor(Float value)
        {
            this.legsDamageFactor = value;
        }

        public void setFeetDamageFactor(Float value)
        {
            this.feetDamageFactor = value;
        }

        public void setDamageSpreadFactor(Float value)
        {
            this.damageSpreadFactor = value;
        }

        public void setArrowsToDrops(Byte value)
        {
            this.arrowsToDrops = value;
        }

        public Float getBreakItem()
        {
            return this.breakItem;
        }

        public Float getHandItemDamageFactor()
        {
            return this.handItemDamageFactor;
        }

        public Float getHeadDamageFactor()
        {
            return this.headDamageFactor;
        }

        public Float getChestDamageFactor()
        {
            return this.chestDamageFactor;
        }

        public Float getLegsDamageFactor()
        {
            return this.legsDamageFactor;
        }

        public Float getFeetDamageFactor()
        {
            return this.feetDamageFactor;
        }

        public Float getDamageSpreadFactor()
        {
            return this.damageSpreadFactor;
        }

        public Byte getArrowsToDrops()
        {
            return this.arrowsToDrops;
        }

        public String getCategoryObject()
        {
            return this.CATEGORY;
        }
    }
}
