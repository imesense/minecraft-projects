package org.imesense.dynamicspawncontrol.technical.config.player;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;

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
        public static ConfigDataPlayer instance;

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
        public void setProtectRespawnPlayerRadius(Short value)
        {
            this.protectRespawnPlayerRadius = value;
        }

        /**
         *
         * @return
         */
        public Short getProtectRespawnPlayerRadius()
        {
            return this.protectRespawnPlayerRadius;
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
