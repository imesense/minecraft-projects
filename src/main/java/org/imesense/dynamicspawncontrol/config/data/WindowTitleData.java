package org.imesense.dynamicspawncontrol.config.data;

import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.annotation.Getter;
import org.imesense.dynamicspawncontrol.core.annotation.Setter;

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
        @Getter
        public String getWindowTitle()
        {
            return this.windowTitle;
        }

        /**
         *
         * @param value
         */
        @Setter
        public void setWindowTitle(String value)
        {
            this.windowTitle = value;
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
