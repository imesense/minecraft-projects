package org.imesense.dynamicspawncontrol.technical.config.windowtitle;

import org.imesense.dynamicspawncontrol.DynamicSpawnControl;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;

import javax.annotation.Nonnull;

/**
 *
 */
public final class DataWindowTitle
{
    /**
     *
     */
    public static final class ConfigDataWindowTitle
    {
        /**
         *
         */
        private final String category;

        /**
         *
         */
        public static ConfigDataWindowTitle instance;

        /**
         *
         */
        private String windowTitle =
                String.format("Minecraft: %s + %s", DynamicSpawnControl.STRUCT_INFO_MOD.VERSION, DynamicSpawnControl.STRUCT_INFO_MOD.NAME);

        /**
         *
         * @param category
         */
        public ConfigDataWindowTitle(@Nonnull final String category)
        {
			CodeGenericUtils.printInitClassToLog(this.getClass());
			
            this.category = category;
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
         * @param windowTitle
         */
        public void setWindowTitle(String windowTitle)
        {
            this.windowTitle = windowTitle;
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
