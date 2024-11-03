package org.imesense.dynamicspawncontrol.technical.config.windowtitle;

import org.imesense.dynamicspawncontrol.ProjectStructure;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.customlibrary.inlineannotation.Getter;
import org.imesense.dynamicspawncontrol.technical.customlibrary.inlineannotation.Setter;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

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
        public static ConfigDataWindowTitle Instance;

        /**
         *
         */
        private String windowTitle =
                String.format("Minecraft: %s + %s",
                        ProjectStructure.STRUCT_INFO_MOD.VERSION, ProjectStructure.STRUCT_INFO_MOD.NAME);

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
