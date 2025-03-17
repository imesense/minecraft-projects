package org.imesense.dynamicspawncontrol.core.config.data;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class BlockWorldGeneratorData
{
    public static final BlockWorldGeneratorDataAbstract NETHER_RACK =
            new BlockWorldGeneratorDataAbstract("settings_block_nether_rack", 20, 5, 20)
            {

            };

    public static final BlockWorldGeneratorDataAbstract MOSSY_COBBLESTONE =
            new BlockWorldGeneratorDataAbstract("settings_block_mossy_cobblestone", 35, 10, 45)
            {

            };

    public static final BlockWorldGeneratorDataAbstract MONSTER_EGG =
            new BlockWorldGeneratorDataAbstract("settings_block_monster_egg", 10, 7, 40)
            {

            };
}
