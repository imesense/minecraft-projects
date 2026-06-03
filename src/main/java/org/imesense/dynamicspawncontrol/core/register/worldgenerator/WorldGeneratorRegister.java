package org.imesense.dynamicspawncontrol.core.register.worldgenerator;

import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.base.BaseWorldGeneratorRegister;
import org.imesense.dynamicspawncontrol.generator.*;
import org.imesense.dynamicspawncontrol.generator.block.*;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

@InitLog
public final class WorldGeneratorRegister extends BaseWorldGeneratorRegister
{
    private static volatile WorldGeneratorRegister _INSTANCE;

    public static WorldGeneratorRegister getInstance()
    {
        return CodeGeneric.getInstance(WorldGeneratorRegister.class);
    }

    public WorldGeneratorRegister()
    {

    }

    private static final Class<?>[] ORE_GENERATOR_CLASSES =
    {
        NetherRackBlockGenerator.class,
        MossyCobblestoneBlockGenerator.class,
        MonsterEggBlockGenerator.class,
        WaterMelonBlockGenerator.class,
        EmeraldOreBlockGenerator.class,
        LootBoxInWorldGenerator.class,
        CaveDecorBlockGenerator.class
    };

    @Override
    protected Class<?>[] getWorldGeneratorClasses()
    {
        return ORE_GENERATOR_CLASSES;
    }
}
