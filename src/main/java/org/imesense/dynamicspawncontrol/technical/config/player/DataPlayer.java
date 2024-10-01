package org.imesense.dynamicspawncontrol.technical.config.player;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;

import javax.annotation.Nonnull;

/**
 *
 */
public final class DataPlayer
{
    /**
     *
     */
    public static final class player
    {
        /**
         *
         */
        final String setCategory;

        /**
         *
         */
        public static player instance;

        /**
         *
         */
        private Short protectRespawnPlayerRadius = 15;

        /**
         *
         * @param category
         */
        public player(@Nonnull final String category)
        {
			CodeGenericUtils.printInitClassToLog(this.getClass());
			
            this.setCategory = category;
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
         * @param value
         */
        public void setProtectRespawnPlayerRadius(Short value)
        {
            this.protectRespawnPlayerRadius = value;
        }
    }
}
