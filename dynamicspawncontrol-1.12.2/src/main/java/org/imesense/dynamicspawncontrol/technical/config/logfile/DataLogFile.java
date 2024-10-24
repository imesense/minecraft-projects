package org.imesense.dynamicspawncontrol.technical.config.logfile;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;

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
        private final String CATEGORY;

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
         * @param CATEGORY
         */
        public ConfigDataLogFile(@Nonnull final String CATEGORY)
        {
			CodeGenericUtil.printInitClassToLog(this.getClass());
			
            this.CATEGORY = CATEGORY;
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
            return this.CATEGORY;
        }
    }
}
