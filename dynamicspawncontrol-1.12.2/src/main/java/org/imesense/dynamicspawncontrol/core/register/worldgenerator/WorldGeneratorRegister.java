package org.imesense.dynamicspawncontrol.core.register.worldgenerator;

import org.imesense.dynamicspawncontrol.core.baseregister.BaseWorldGeneratorRegister;
import org.imesense.dynamicspawncontrol.worldgenerator.*;

public class WorldGeneratorRegister extends BaseWorldGeneratorRegister
{
    private static volatile WorldGeneratorRegister _INSTANCE;

    public static WorldGeneratorRegister getInstance()
    {
        if (_INSTANCE == null)
        {
            synchronized (WorldGeneratorRegister.class)
            {
                if (_INSTANCE == null)
                {
                    _INSTANCE = new WorldGeneratorRegister();
                }
            }
        }

        return _INSTANCE;
    }

    private static final Class<?>[] ORE_GENERATOR_CLASSES =
    {
        BlockNetherRack.class,
        BlockMossyCobblestone.class,
        BlockMonsterEgg.class,
        BlockWaterMelon.class
    };

    @Override
    protected Class<?>[] getWorldGeneratorClasses()
    {
        return ORE_GENERATOR_CLASSES;
    }
}
