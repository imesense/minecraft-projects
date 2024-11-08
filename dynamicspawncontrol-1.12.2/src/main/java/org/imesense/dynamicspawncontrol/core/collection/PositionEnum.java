package org.imesense.dynamicspawncontrol.core.collection;

/**
 *
 */
public class PositionEnum
{
    /**
     *
     */
    public enum Coordinates
    {
        /**
         *
         */
        X("x"),

        /**
         *
         */
        Y("y"),

        /**
         *
         */
        Z("z");

        /**
         *
         */
        private final String VALUE;

        /**
         *
         * @param VALUE
         */
        Coordinates(final String VALUE)
        {
            this.VALUE = VALUE;
        }

        /**
         *
         * @return
         */
        public String getValue()
        {
            return this.VALUE;
        }
    }

    /**
     *
     */
    public enum BlockProperties
    {
        /**
         *
         */
        OFFSET("offset"),

        /**
         *
         */
        STEP("step"),

        /**
         *
         */
        LOOK("look");

        /**
         *
         */
        private final String VALUE;

        /**
         *
         * @param VALUE
         */
         BlockProperties(final String VALUE)
         {
            this.VALUE = VALUE;
         }

         /**
         *
         * @return
         */
        public String getValue()
        {
            return this.VALUE;
        }
    }
}
