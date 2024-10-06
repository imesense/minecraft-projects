package org.imesense.dynamicspawncontrol.technical.eventprocessor.primitive;

import net.minecraftforge.fml.common.Mod;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

@Mod.EventBusSubscriber
public final class OnComplexityBiomes
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public OnComplexityBiomes()
    {
        CodeGenericUtils.printInitClassToLog(this.getClass());

        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;
    }
}
