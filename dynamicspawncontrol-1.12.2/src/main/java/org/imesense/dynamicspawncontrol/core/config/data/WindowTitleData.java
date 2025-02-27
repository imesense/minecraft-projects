package org.imesense.dynamicspawncontrol.core.config.data;

import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import javax.annotation.Nonnull;

/**
 *
 */
public final class WindowTitleData
{
    /**
     *
     */
    public static final class ConfigDataWindowTitle
    {
        /**
         *
         */
        private final String CATEGORY;

        /**
         *
         */
        public static ConfigDataWindowTitle Instance;

        /**
         *
         */
        private String windowTitle =
                String.format("Minecraft: %s + %s",
                        DynamicSpawnControlStructure.STRUCT_INFO_MOD.VERSION, DynamicSpawnControlStructure.STRUCT_INFO_MOD.NAME);

        /**
         *
         * @param CATEGORY
         */
        public ConfigDataWindowTitle(@Nonnull final String CATEGORY)
        {
			CodeGeneric.printInitClassToLog(this.getClass());
			
            this.CATEGORY = CATEGORY;
        }

        /**
         *
         * @return
         */
        public String getWindowTitle()
        {
            return this.windowTitle;
        }

        /**
         *
         * @param value
         */
        public void setWindowTitle(String value)
        {
            this.windowTitle = value;
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
