package org.imesense.dynamicspawncontrol.plugin.fogworld_1_12_1_1_0_b15_universal.config;

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
        private boolean poisonousFog = false;
        private int posionTicks = 1200;
        private int poisonDamage = 1;
        private String[] fogBiomeBlacklist = new String[0];
        private String[] fogDimensionBlacklist = new String[0];

        public ConfigDataFogWorld(@Nonnull final String CATEGORY)
        {
            CodeGeneric.printInitClassToLog(this.getClass());
            this.CATEGORY = CATEGORY;
        }

        // Getters
        public float getFogDensity()
        {
            return fogDensity;
        }

        public int getFogColor()
        {
            return fogColor;
        }

        public boolean isPoisonousFog()
        {
            return poisonousFog;
        }

        public int getPosionTicks()
        {
            return posionTicks;
        }

        public int getPoisonDamage()
        {
            return poisonDamage;
        }

        public String[] getFogBiomeBlacklist()
        {
            return fogBiomeBlacklist;
        }

        public String[] getFogDimensionBlacklist()
        {
            return fogDimensionBlacklist;
        }

        // Setters
        public void setFogDensity(float fogDensity)
        {
            this.fogDensity = fogDensity;
        }

        public void setFogColor(int fogColor)
        {
            this.fogColor = fogColor;
        }

        public void setPoisonousFog(boolean poisonousFog)
        {
            this.poisonousFog = poisonousFog;
        }

        public void setPosionTicks(int posionTicks)
        {
            this.posionTicks = posionTicks;
        }

        public void setPoisonDamage(int poisonDamage)
        {
            this.poisonDamage = poisonDamage;
        }

        public void setFogBiomeBlacklist(String[] fogBiomeBlacklist)
        {
            this.fogBiomeBlacklist = fogBiomeBlacklist;
        }

        public void setFogDimensionBlacklist(String[] fogDimensionBlacklist)
        {
            this.fogDimensionBlacklist = fogDimensionBlacklist;
        }

        public String getCategoryObject()
        {
            return this.CATEGORY;
        }
    }
}

