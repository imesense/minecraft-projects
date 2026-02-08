package org.imesense.dynamicspawncontrol.core.mixinconfig;

import java.io.File;

public final class Mixin
{
    public static void createFile(final String PATH)
    {
        try
        {
            File pathDir = new File(PATH);
            MixinConfig.init(pathDir);
        }
        catch (Exception exception)
        {
            System.err.println("Failed to create mixin config: " + exception.getMessage());
        }
    }
}