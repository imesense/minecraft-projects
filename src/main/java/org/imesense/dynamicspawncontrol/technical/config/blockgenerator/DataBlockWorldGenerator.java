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
    public static final class InfoDataBlockNetherRack extends ConfigDataBlock
    {
        /**
         *
         */
        public static InfoDataBlockNetherRack Instance;

        /**
         *
         * @param CATEGORY
         */
        public InfoDataBlockNetherRack(@Nonnull final String CATEGORY)
        {
            super(CATEGORY, 20, 5, 20);
        }
    }

    /**
     *
     */
    public static final class InfoDataBlockMossyCobblestone extends ConfigDataBlock
    {
        /**
         *
         */
        public static InfoDataBlockMossyCobblestone Instance;

        /**
         *
         * @param CATEGORY
         */
        public InfoDataBlockMossyCobblestone(@Nonnull final String CATEGORY)
        {
            super(CATEGORY, 35, 10, 45);
        }
    }

    /**
     *
     */
    public static final class InfoDataBlockBlockMonsterEgg extends ConfigDataBlock
    {
        /**
         *
         */
        public static InfoDataBlockBlockMonsterEgg Instance;

        /**
         *
         * @param CATEGORY
         */
        public InfoDataBlockBlockMonsterEgg(@Nonnull final String CATEGORY)
        {
            super(CATEGORY, 10, 7, 40);
        }
    }
}
