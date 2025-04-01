package org.imesense.dynamicspawncontrol.core.register.worldgenerator;

import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseregister.BaseWorldGeneratorRegister;
import org.imesense.dynamicspawncontrol.decorworldgenerator.CaveDecorGenerator;
import org.imesense.dynamicspawncontrol.lootboxgenerator.LootBoxInWorld;
import org.imesense.dynamicspawncontrol.blockworldgenerator.*;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

@InitLog
public final class WorldGeneratorRegister extends BaseWorldGeneratorRegister
{
    private static volatile WorldGeneratorRegister _INSTANCE;

    public static WorldGeneratorRegister getInstance()
    {
        return CodeGeneric.getInstance(WorldGeneratorRegister.class);
    }

    private static final Class<?>[] ORE_GENERATOR_CLASSES =
    {
        BlockNetherRack.class,
        BlockMossyCobblestone.class,
        BlockMonsterEgg.class,
        BlockWaterMelon.class,
        BlockEmeraldOre.class,
        LootBoxInWorld.class,
        CaveDecorGenerator.class
    };

    @Override
    protected Class<?>[] getWorldGeneratorClasses()
    {
        return ORE_GENERATOR_CLASSES;
    }
}
