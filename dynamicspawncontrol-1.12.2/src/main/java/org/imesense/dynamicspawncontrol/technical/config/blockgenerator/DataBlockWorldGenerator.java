package org.imesense.dynamicspawncontrol.technical.config.blockgenerator;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;

import javax.annotation.Nonnull;

/**
 *
 */
public class DataBlockWorldGenerator
{
    /**
     *
     */
    public static final class InfoDataBlockNetherRack
    {
        /**
         *
         */
        final String setCategory;

        /**
         *
         */
        public static InfoDataBlockNetherRack instance;

        /**
         *
         */
        private Integer chanceSpawn = 20;

        /**
         *
         */
        private Integer minHeight = 5;

        /**
         *
         */
        private Integer maxHeight = 20;

        /**
         *
         * @param category
         */
        public InfoDataBlockNetherRack(@Nonnull final String category)
        {
			CodeGenericUtils.printInitClassToLog(this.getClass());
			
            this.setCategory = category;
        }

        /**
         *
         * @return
         */
        public Integer getChanceSpawn()
        {
            return this.chanceSpawn;
        }

        /**
         *
         * @param chanceSpawn
         */
        public void setChanceSpawn(Integer chanceSpawn)
        {
            this.chanceSpawn = chanceSpawn;
        }

        /**
         *
         * @return
         */
        public Integer getMinHeight()
        {
            return this.minHeight;
        }

        /**
         *
         * @param minHeight
         */
        public void setMinHeight(Integer minHeight)
        {
            this.minHeight = minHeight;
        }

        /**
         *
         * @return
         */
        public Integer getMaxHeight()
        {
            return this.maxHeight;
        }

        /**
         *
         * @param maxHeight
         */
        public void setMaxHeight(Integer maxHeight)
        {
            this.maxHeight = maxHeight;
        }
    }

    /**
     *
     */
    public static final class InfoDataBlockMossyCobblestone
    {
        /**
         *
         */
        final String setCategory;

        /**
         *
         */
        public static InfoDataBlockMossyCobblestone instance;

        /**
         *
         */
        private Integer chanceSpawn = 35;

        /**
         *
         */
        private Integer minHeight = 10;

        /**
         *
         */
        private Integer maxHeight = 45;

        /**
         *
         * @param category
         */
        public InfoDataBlockMossyCobblestone(@Nonnull final String category)
        {
			CodeGenericUtils.printInitClassToLog(this.getClass());
			
            this.setCategory = category;
        }

        /**
         *
         * @return
         */
        public Integer getChanceSpawn()
        {
            return this.chanceSpawn;
        }

        /**
         *
         * @param chanceSpawn
         */
        public void setChanceSpawn(Integer chanceSpawn)
        {
            this.chanceSpawn = chanceSpawn;
        }

        /**
         *
         * @return
         */
        public Integer getMinHeight()
        {
            return this.minHeight;
        }

        /**
         *
         * @param minHeight
         */
        public void setMinHeight(Integer minHeight)
        {
            this.minHeight = minHeight;
        }

        /**
         *
         * @return
         */
        public Integer getMaxHeight()
        {
            return this.maxHeight;
        }

        /**
         *
         * @param maxHeight
         */
        public void setMaxHeight(Integer maxHeight)
        {
            this.maxHeight = maxHeight;
        }
    }

    /**
     *
     */
    public static final class InfoDataBlockBlockMonsterEgg
    {
        /**
         *
         */
        final String setCategory;

        /**
         *
         */
        public static InfoDataBlockBlockMonsterEgg instance;

        /**
         *
         */
        private Integer chanceSpawn = 10;

        /**
         *
         */
        private Integer minHeight = 7;

        /**
         *
         */
        private Integer maxHeight = 40;

        /**
         *
         * @param category
         */
        public InfoDataBlockBlockMonsterEgg(@Nonnull final String category)
        {
			CodeGenericUtils.printInitClassToLog(this.getClass());
			
            this.setCategory = category;
        }

        /**
         *
         * @return
         */
        public Integer getChanceSpawn()
        {
            return this.chanceSpawn;
        }

        /**
         *
         * @param chanceSpawn
         */
        public void setChanceSpawn(Integer chanceSpawn)
        {
            this.chanceSpawn = chanceSpawn;
        }

        /**
         *
         * @return
         */
        public Integer getMinHeight()
        {
            return this.minHeight;
        }

        /**
         *
         * @param minHeight
         */
        public void setMinHeight(Integer minHeight)
        {
            this.minHeight = minHeight;
        }

        /**
         *
         * @return
         */
        public Integer getMaxHeight()
        {
            return this.maxHeight;
        }

        /**
         *
         * @param maxHeight
         */
        public void setMaxHeight(Integer maxHeight)
        {
            this.maxHeight = maxHeight;
        }
    }
}
