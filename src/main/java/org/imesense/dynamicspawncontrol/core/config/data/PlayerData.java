package org.imesense.dynamicspawncontrol.core.config.data;

import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import javax.annotation.Nonnull;

public final class PlayerData
{
    public static final class ConfigDataPlayer
    {
        private final String CATEGORY;

        public static ConfigDataPlayer Instance;

        private Short protectRespawnPlayerRadius = 15;

        public ConfigDataPlayer(@Nonnull final String CATEGORY)
        {
			CodeGeneric.printInitClassToLog(this.getClass());
			
            this.CATEGORY = CATEGORY;
        }

        public void setProtectRespawnPlayerRadius(Short value)
        {
            this.protectRespawnPlayerRadius = value;
        }

        public Short getProtectRespawnPlayerRadius()
        {
            return this.protectRespawnPlayerRadius;
        }

        public String getCategoryObject()
        {
            return this.CATEGORY;
        }
    }
}
