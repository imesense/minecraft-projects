package org.imesense.dynamicspawncontrol.config.data;

import javax.annotation.Nonnull;

/**
 *
 */
public final class BlockWorldGeneratorData
{
    /**
     *
     */
    public static final class InfoDataBlockNetherRack extends BlockWorldGeneratorDataAbstract
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
    public static final class InfoDataBlockMossyCobblestone extends BlockWorldGeneratorDataAbstract
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
    public static final class InfoDataBlockBlockMonsterEgg extends BlockWorldGeneratorDataAbstract
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
