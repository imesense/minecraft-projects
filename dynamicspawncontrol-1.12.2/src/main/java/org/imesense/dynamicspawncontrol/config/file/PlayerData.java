package org.imesense.dynamicspawncontrol.config.file;

import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.annotation.Getter;
import org.imesense.dynamicspawncontrol.core.annotation.Setter;

import javax.annotation.Nonnull;

/**
 *
 */
public final class PlayerData
{
    /**
     *
     */
    public static final class ConfigDataPlayer
    {
        /**
         *
         */
        private final String CATEGORY;

        /**
         *
         */
        public static ConfigDataPlayer Instance;

        /**
         *
         */
        private Short protectRespawnPlayerRadius = 15;

        /**
         *
         * @param CATEGORY
         */
        public ConfigDataPlayer(@Nonnull final String CATEGORY)
        {
			CodeGeneric.printInitClassToLog(this.getClass());
			
            this.CATEGORY = CATEGORY;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setProtectRespawnPlayerRadius(Short value)
        {
            this.protectRespawnPlayerRadius = value;
        }

        /**
         *
         * @return
         */
        @Getter
        public Short getProtectRespawnPlayerRadius()
        {
            return this.protectRespawnPlayerRadius;
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
