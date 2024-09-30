package org.imesense.dynamicspawncontrol.technical.config.windowtitle;

import org.imesense.dynamicspawncontrol.DynamicSpawnControl;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;

import javax.annotation.Nonnull;

/**
 *
 */
public class DataWindowTitle
{
    /**
     *
     */
    public static final class windowTitle
    {
        /**
         *
         */
        final String setCategory;

        /**
         *
         */
        public static windowTitle instance;

        /**
         *
         */
        private String windowTitle =
                String.format("Minecraft: %s + %s", DynamicSpawnControl.STRUCT_INFO_MOD.VERSION, DynamicSpawnControl.STRUCT_INFO_MOD.NAME);

        /**
         *
         * @param category
         */
        public windowTitle(@Nonnull final String category)
        {
            this.setCategory = category;

            CodeGenericUtils.printInitClassToLog(this.getClass());
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
    }
}
