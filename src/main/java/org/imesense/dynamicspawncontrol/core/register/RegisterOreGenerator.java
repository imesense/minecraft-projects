package org.imesense.dynamicspawncontrol.core.register;

import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.worldgenerator.*;

/**
 *
 */
public final class RegisterOreGenerator
{
    /**
     *
     */
    private static final Class<?>[] ORE_GENERATOR_CLASSES =
    {
        BlockNetherRack.class,
        BlockMossyCobblestone.class,
        BlockMonsterEgg.class,
        BlockWaterMelon.class
    };

    /**
     *
     */
    public RegisterOreGenerator()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
    }

    /**
     *
     * @param fmlPreInitializationEvent
     */
    public static void init(FMLPreInitializationEvent fmlPreInitializationEvent)
    {
        for (Class<?> _class : ORE_GENERATOR_CLASSES)
        {
            try
            {
                if (!CodeGeneric.hasDefaultConstructor(_class))
                {
                    Log.writeDataToLogFile(2, "Class " + _class.getName() + " does not have a default constructor.");
                    throw new RuntimeException("Default constructor not found in class: " + _class.getName());
                }

                Object object =
                        _class.getConstructor().newInstance();

                GameRegistry.registerWorldGenerator((IWorldGenerator) object, 3);
            }
            catch (Exception exception)
            {
                Log.writeDataToLogFile(2, "Exception in class: " + _class.getName() + " - " + exception.getMessage());
                throw new RuntimeException(exception);
            }
        }
    }
}
