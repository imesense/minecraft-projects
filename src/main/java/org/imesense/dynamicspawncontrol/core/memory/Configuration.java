package org.imesense.dynamicspawncontrol.core.memory;

public class Configuration {
    public static String commandAliases = "";
    public static boolean showMessage = true;
    public static boolean cleanOnJoin = true;
    public static boolean cleanOnInit = true;
    public static int forceCleanPercentage = 80;
    public static Configuration.AutoCleanup AutomaticCleanup = new Configuration.AutoCleanup();

    public static class AutoCleanup
    {
        public boolean autoCleanup = true;
        public int minInterval = 300;
        public int maxInterval = 1200;
        public int minIdleTime = 30;
    }
}
