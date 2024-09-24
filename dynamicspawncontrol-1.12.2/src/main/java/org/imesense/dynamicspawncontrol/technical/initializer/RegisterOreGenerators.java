package org.imesense.dynamicspawncontrol.technical.initializer;

import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.gameplay.worldgenerator.*;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

/**
 *
 */
public final class RegisterOreGenerators
{
    /**
     *
     */
    private static final Class<?>[] ORE_GENERATOR_CLASSES =
    {
        NetherRackGenerator.class,
        MossyCobblestone.class
    };

    /**
     *
     */
    public RegisterOreGenerators()
    {
        CodeGenericUtils.printInitClassToLog(RegisterOreGenerators.class);
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
                if (!CodeGenericUtils.hasDefaultConstructor(oreGeneratorClass))
                {
                    Log.writeDataToLogFile(2, "Class " + oreGeneratorClass.getName() + " does not have a default constructor.");
                    throw new RuntimeException("Default constructor not found in class: " + oreGeneratorClass.getName());
                }

                Object oreGeneratorInstance = oreGeneratorClass.getConstructor().newInstance();
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
