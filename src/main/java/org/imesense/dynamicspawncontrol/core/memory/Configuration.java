package org.imesense.dynamicspawncontrol.core.memory;

import lombok.Getter;

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

    public static List<String> getCommandAliases()
    {
        return Collections.unmodifiableList(COMMAND_ALIASES);
    }

    public Configuration()
    {

    }
}
