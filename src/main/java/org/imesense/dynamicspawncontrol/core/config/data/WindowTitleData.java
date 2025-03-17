package org.imesense.dynamicspawncontrol.core.config.data;

import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import javax.annotation.Nonnull;

/**
 *
 */
public final class WindowTitleData
{
    public static final class ConfigDataWindowTitle
    {
        private final String CATEGORY;

        public static ConfigDataWindowTitle Instance;

        private String windowTitle =
                String.format("Minecraft: %s + %s",
                        DynamicSpawnControlStructure.STRUCT_INFO_MOD.VERSION, DynamicSpawnControlStructure.STRUCT_INFO_MOD.NAME);

        public ConfigDataWindowTitle(@Nonnull final String CATEGORY)
        {
			CodeGeneric.printInitClassToLog(this.getClass());
			
            this.CATEGORY = CATEGORY;
        }

        public String getWindowTitle()
        {
            return this.windowTitle;
        }

        public void setWindowTitle(String value)
        {
            this.windowTitle = value;
        }

        public String getCategoryObject()
        {
            return this.CATEGORY;
        }
    }
}
