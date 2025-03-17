package org.imesense.dynamicspawncontrol.core.config.data;

import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import javax.annotation.Nonnull;

public final class ZombieDropItemData
{
    public static final class ConfigDataZombieDrop
    {
        private final String CATEGORY;

        public static ConfigDataZombieDrop Instance;

        private Float breakItem = 0.15f;

        private Float handItemDamageFactor = 0.85f;

        private Float headDamageFactor = 0.9f;

        private Float chestDamageFactor = 0.9f;

        private Float legsDamageFactor = 0.9f;

        private Float feetDamageFactor = 0.9f;

        private Float damageSpreadFactor = 0.2f;

        public ConfigDataZombieDrop(@Nonnull final String CATEGORY)
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

        public String getCategoryObject()
        {
            return this.CATEGORY;
        }
    }
}

