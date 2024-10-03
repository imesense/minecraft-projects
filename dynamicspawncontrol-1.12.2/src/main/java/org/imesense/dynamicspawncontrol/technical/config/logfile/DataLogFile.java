package org.imesense.dynamicspawncontrol.technical.config.logfile;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;

import javax.annotation.Nonnull;

/**
 *
 */
public final class DataLogFile
{
    /**
     *
     */
    public static final class ConfigDataLogFile
    {
        /**
         *
         */
        private final String category;

        /**
         *
         */
        public static ConfigDataLogFile instance;

        /**
         *
         */
        private Short logMaxLines = 32767;

        /**
         *
         * @param category
         */
        public ConfigDataLogFile(@Nonnull final String category)
        {
			CodeGenericUtils.printInitClassToLog(this.getClass());
			
            this.category = category;
        }

        /**
         *
         * @return
         */
        public Short getLogMaxLines()
        {
            return this.logMaxLines;
        }

        /**
         *
         * @param value
         */
        public void setLogMaxLines(Short value)
        {
            this.logMaxLines = value;
        }

        /**
         *
         * @return
         */
        public String getCategoryObject()
        {
            return this.category;
        }
    }
}
