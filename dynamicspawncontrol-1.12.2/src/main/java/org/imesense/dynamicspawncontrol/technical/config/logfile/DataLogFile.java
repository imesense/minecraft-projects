package org.imesense.dynamicspawncontrol.technical.config.logfile;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.technical.customlibrary.inlineannotation.Getter;
import org.imesense.dynamicspawncontrol.technical.customlibrary.inlineannotation.Setter;

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
        public static ConfigDataLogFile Instance;

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
