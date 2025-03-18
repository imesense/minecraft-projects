package org.imesense.dynamicspawncontrol.core.plugin.mod.fogworld_1_12_1_1_0_b15_universal.config;

import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import javax.annotation.Nonnull;

public class DataFogWorld
{
    public static final class ConfigDataFogWorld
    {
        private final String CATEGORY;

        public static ConfigDataFogWorld Instance;

        private float fogDensity = 0.1f;
        private int fogColor = 16777215;

        public ConfigDataFogWorld(@Nonnull final String CATEGORY)
        {
            CodeGeneric.printInitClassToLog(this.getClass());
            this.CATEGORY = CATEGORY;
        }

        public float getFogDensity()
        {
            return fogDensity;
        }

        public int getFogColor()
        {
            return fogColor;
        }

        public void setFogDensity(float fogDensity)
        {
            this.fogDensity = fogDensity;
        }

        public void setFogColor(int fogColor)
        {
            this.fogColor = fogColor;
        }

        public String getCategoryObject()
        {
            return this.CATEGORY;
        }
    }
}

