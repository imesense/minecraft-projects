package org.imesense.dynamicspawncontrol.technical.config.blockgenerator;

import javax.annotation.Nonnull;

/**
 *
 */
public final class DataBlockWorldGenerator
{
    /**
     *
     */
    public static final class InfoDataBlockNetherRack extends InfoDataBlock
    {
        /**
         *
         */
        public static InfoDataBlockNetherRack instance;

        /**
         *
         * @param category
         */
        public InfoDataBlockNetherRack(@Nonnull final String category)
        {
            super(category, 20, 5, 20);
        }
    }

    /**
     *
     */
    public static final class InfoDataBlockMossyCobblestone extends InfoDataBlock
    {
        /**
         *
         */
        public static InfoDataBlockMossyCobblestone instance;

        /**
         *
         * @param category
         */
        public InfoDataBlockMossyCobblestone(@Nonnull final String category)
        {
            super(category, 35, 10, 45);
        }
    }

    /**
     *
     */
    public static final class InfoDataBlockBlockMonsterEgg extends InfoDataBlock
    {
        /**
         *
         */
        public static InfoDataBlockBlockMonsterEgg instance;

        /**
         *
         * @param category
         */
        public InfoDataBlockBlockMonsterEgg(@Nonnull final String category)
        {
            super(category, 10, 7, 40);
        }
    }
}
