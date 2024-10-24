package org.imesense.dynamicspawncontrol.technical.config.windowtitle;

import org.imesense.dynamicspawncontrol.ProjectStructure;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;

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
        private final String CATEGORY;

        /**
         *
         */
        public static ConfigDataWindowTitle instance;

        /**
         *
         */
        private String windowTitle =
                String.format("Minecraft: %s + %s", ProjectStructure.STRUCT_INFO_MOD.VERSION, ProjectStructure.STRUCT_INFO_MOD.NAME);

        /**
         *
         * @param CATEGORY
         */
        public ConfigDataWindowTitle(@Nonnull final String CATEGORY)
        {
			CodeGenericUtil.printInitClassToLog(this.getClass());
			
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
            return this.CATEGORY;
        }
    }
}
