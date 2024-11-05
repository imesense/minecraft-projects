package org.imesense.dynamicspawncontrol.config.data;

import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.annotation.Getter;
import org.imesense.dynamicspawncontrol.core.annotation.Setter;

import javax.annotation.Nonnull;

/**
 *
 */
public final class LogFileData
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
        public static ConfigDataLogFile Instance;

        /**
         *
         */
        private Short logMaxLines = Short.MAX_VALUE;

        /**
         *
         * @param CATEGORY
         */
        public ConfigDataLogFile(@Nonnull final String CATEGORY)
        {
			CodeGeneric.printInitClassToLog(this.getClass());
			
            this.CATEGORY = CATEGORY;
        }

        /**
         *
         * @return
         */
        @Getter
        public Short getLogMaxLines()
        {
            return this.logMaxLines;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setLogMaxLines(Short value)
        {
            this.logMaxLines = value;
        }

        /**
         *
         * @return
         */
        @Getter
        public String getCategoryObject()
        {
            return this.CATEGORY;
        }
    }
}
