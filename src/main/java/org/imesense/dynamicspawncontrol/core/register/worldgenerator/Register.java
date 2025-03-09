package org.imesense.dynamicspawncontrol.core.register.worldgenerator;

import org.imesense.dynamicspawncontrol.core.baseregister.BaseWorldGeneratorRegister;
import org.imesense.dynamicspawncontrol.worldgenerator.*;

public class Register extends BaseWorldGeneratorRegister
{
    private static volatile Register _INSTANCE;

    public static Register getInstance()
    {
        if (_INSTANCE == null)
        {
            synchronized (Register.class)
            {
                if (_INSTANCE == null)
                {
                    _INSTANCE = new Register();
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
