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
        private final String value;

        /**
         *
         * @param value
         */
        Coordinates(String value)
        {
            this.value = value;
        }

        /**
         *
         * @return
         */
        public String getValue()
        {
            return value;
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
        private final String value;

        /**
         *
         * @param value
         */
         BlockProperties(String value)
         {
            this.value = value;
         }

         /**
         *
         * @return
         */
        public String getValue()
        {
            return this.value;
        }
    }
}
