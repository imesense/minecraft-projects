package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.items;

import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;

public final class ItemMobEssence extends ItemMobSoul
{
    public ItemMobEssence()
    {
        setUnlocalizedName("mob_essence");
        setRegistryName(DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID, "mob_essence");
    }
}
