package org.imesense.dynamicspawncontrol.technical.config.logfile;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;

import javax.annotation.Nonnull;

/**
 *
 */
public class DataLogFile
{
    /**
     *
     */
    public static final class logFile
    {
        /**
         *
         */
        final String setCategory;

        /**
         *
         */
        public static logFile instance;

        /**
         *
         */
        private Short logMaxLines = 32767;

        /**
         *
         * @param category
         */
        public logFile(@Nonnull final String category)
        {
			CodeGenericUtils.printInitClassToLog(this.getClass());
			
            this.setCategory = category;
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
    }
}
