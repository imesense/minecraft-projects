package org.imesense.dynamicspawncontrol.core.config.dataLegacy;

import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import javax.annotation.Nonnull;

public final class LogFileData
{
    public static final class ConfigDataLogFile
    {
        private final String CATEGORY;

        public static ConfigDataLogFile Instance;

        private Short logMaxLines = Short.MAX_VALUE;

        public ConfigDataLogFile(@Nonnull final String CATEGORY)
        {
			CodeGeneric.printInitClassToLog(this.getClass());
			
            this.CATEGORY = CATEGORY;
        }

        public Short getLogMaxLines()
        {
            return this.logMaxLines;
        }

        public void setLogMaxLines(Short value)
        {
            this.logMaxLines = value;
        }

        public String getCategoryObject()
        {
            return this.CATEGORY;
        }
    }
}
