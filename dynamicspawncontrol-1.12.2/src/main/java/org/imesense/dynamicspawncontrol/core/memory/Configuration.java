package org.imesense.dynamicspawncontrol.core.memory;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Configuration
{
    @Getter
    private static boolean showMessage = true;

    @Getter
    private static boolean cleanOnJoin = true;

    @Getter
    private static boolean cleanOnInit = true;

    @Getter
    private static int forceCleanPercentage = 80;

    private static final List<String> COMMAND_ALIASES = new ArrayList<>();
    private static final AutoCleanup AUTOMATIC_CLEANUP = new AutoCleanup();

    public static List<String> getCommandAliases()
    {
        return Collections.unmodifiableList(COMMAND_ALIASES);
    }

    public static AutoCleanup getAutomaticCleanup()
    {
        return AUTOMATIC_CLEANUP;
    }

    public Configuration()
    {

    }

    @Setter
    @Getter
    public static final class AutoCleanup
    {
        private boolean autoCleanup = true;
        private int minInterval = 300;  //-' 5 минут
        private int maxInterval = 1200; //-' 20 минут
        private int minIdleTime = 30;   //-' 30 секунд
    }
}
