package org.imesense.dynamicspawncontrol.technical.customlibrary;

/**
 *
 */
public class EnumGameProperties
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
         * @param value
         */
        Coordinates(final String value)
        {
            this.VALUE = value;
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
         * @param value
         */
         BlockProperties(final String value)
         {
            this.VALUE = value;
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
