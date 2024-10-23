package org.imesense.dynamicspawncontrol.technical.register;

import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.gameplay.worldgenerator.*;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

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
        BlockMonsterEgg.class
    };

    /**
     *
     */
    public RegisterOreGenerator()
    {
        CodeGenericUtil.printInitClassToLog(this.getClass());
    }

    /**
     *
     * @param event
     */
    public static void init(FMLPreInitializationEvent event)
    {
        for (Class<?> oreGeneratorClass : ORE_GENERATOR_CLASSES)
        {
            try
            {
                if (!CodeGenericUtil.hasDefaultConstructor(oreGeneratorClass))
                {
                    Log.writeDataToLogFile(2, "Class " + oreGeneratorClass.getName() + " does not have a default constructor.");
                    throw new RuntimeException("Default constructor not found in class: " + oreGeneratorClass.getName());
                }

                Object oreGeneratorInstance =
                        oreGeneratorClass.getConstructor().newInstance();

                GameRegistry.registerWorldGenerator((IWorldGenerator) oreGeneratorInstance, 3);
            }
            catch (Exception exception)
            {
                Log.writeDataToLogFile(2, "Exception in class: " + oreGeneratorClass.getName() + " - " + exception.getMessage());
                throw new RuntimeException(exception);
            }
        }
    }
}
