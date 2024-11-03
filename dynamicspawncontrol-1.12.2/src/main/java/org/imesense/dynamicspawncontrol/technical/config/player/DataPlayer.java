package org.imesense.dynamicspawncontrol.technical.config.player;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.technical.customlibrary.inlineannotation.Getter;
import org.imesense.dynamicspawncontrol.technical.customlibrary.inlineannotation.Setter;

import javax.annotation.Nonnull;

/**
 *
 */
public final class DataPlayer
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
			CodeGenericUtil.printInitClassToLog(this.getClass());
			
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
