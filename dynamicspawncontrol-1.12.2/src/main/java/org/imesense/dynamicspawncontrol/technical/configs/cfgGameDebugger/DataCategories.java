package org.imesense.dynamicspawncontrol.technical.configs.cfgGameDebugger;

import javax.annotation.Nonnull;

public class DataCategories
{
    public static final class DebugMonitor
    {
        final String setCategory;

        public static DebugMonitor instance;

        private boolean monitorDebug = false;

        public DebugMonitor(@Nonnull final String category)
        {
            this.setCategory = category;
        }

        public Boolean getDebugMonitorOpt()
        {
            return monitorDebug;
        }

        public void setDebugMonitorOpt(Boolean monitorDebug)
        {
            this.monitorDebug = monitorDebug;
        }
    }

    public static final class DebugEvent
    {
        final String setCategory;

        public static DebugEvent instance;

        private boolean drop_all_experience = false;

        public DebugEvent(@Nonnull final String category)
        {
            this.setCategory = category;
        }

        public Boolean getDebugDropAllExpOpt()
        {
            return this.drop_all_experience;
        }

        public void setDebugDropAllExpOpt(Boolean drop_all_experience)
        {
            this.drop_all_experience = drop_all_experience;
        }
    }
}
