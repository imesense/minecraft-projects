package org.imesense.dynamicspawncontrol.core.config.blockworldgenerator;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

@Getter
@Accessors(fluent = true)
public abstract class BlockWorldGeneratorData
{
    public static final BlockWorldGeneratorDataAbstract NETHER_RACK =
            new BlockWorldGeneratorDataAbstract("settings_block_nether_rack", 20, 5, 20) {};

    public static final BlockWorldGeneratorDataAbstract MOSSY_COBBLESTONE =
            new BlockWorldGeneratorDataAbstract("settings_block_mossy_cobblestone", 35, 10, 45) {};

    public static final BlockWorldGeneratorDataAbstract MONSTER_EGG =
            new BlockWorldGeneratorDataAbstract("settings_block_monster_egg", 10, 7, 40) {};

    public static final BlockWorldGeneratorDataAbstract EMERALD_ORE =
            new BlockWorldGeneratorDataAbstract("settings_block_emerald_ore", 20, 5, 30) {};

    {
        CodeGeneric.printInitClassToLog(this.getClass());
    }
}
