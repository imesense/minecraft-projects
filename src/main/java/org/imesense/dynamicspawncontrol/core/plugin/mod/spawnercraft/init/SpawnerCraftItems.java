package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.init;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.items.ItemMobAgglomeration;
import org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.items.ItemMobEssence;
import org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.items.ItemMobRod;
import org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.items.ItemMobSpirit;

public final class SpawnerCraftItems
{
    public static final ItemMobEssence MOB_ESSENCE = new ItemMobEssence();
    public static final ItemMobAgglomeration MOB_AGGLOMERATION = new ItemMobAgglomeration();
    public static final ItemMobSpirit MOB_SPIRIT = new ItemMobSpirit();
    public static final ItemMobRod MOB_ROD = new ItemMobRod();

    public static void registerItems()
    {
        ForgeRegistries.ITEMS.register(MOB_ESSENCE);
        ForgeRegistries.ITEMS.register(MOB_AGGLOMERATION);
        ForgeRegistries.ITEMS.register(MOB_SPIRIT);
        ForgeRegistries.ITEMS.register(MOB_ROD);
        ForgeRegistries.ITEMS.register(new ItemBlock(SpawnerCraftBlocks.MOB_CAGE).setRegistryName(SpawnerCraftBlocks.MOB_CAGE.getRegistryName()));
    }

    @SideOnly(Side.CLIENT)
    public static void registerModels()
    {
        ModelLoader.setCustomModelResourceLocation(MOB_ESSENCE, 0,
                new ModelResourceLocation(MOB_ESSENCE.getRegistryName(), "inventory"));

        ModelLoader.setCustomModelResourceLocation(MOB_AGGLOMERATION, 0,
                new ModelResourceLocation(MOB_AGGLOMERATION.getRegistryName(), "inventory"));

        ModelLoader.setCustomModelResourceLocation(MOB_SPIRIT, 0,
                new ModelResourceLocation(MOB_SPIRIT.getRegistryName(), "inventory"));

        ModelLoader.setCustomModelResourceLocation(MOB_ROD, 0,
                new ModelResourceLocation(MOB_ROD.getRegistryName(), "inventory"));
    }

    @SideOnly(Side.CLIENT)
    public static void registerColors(ItemColors itemColors)
    {
        itemColors.registerItemColorHandler((stack, tintIndex) ->
        {
            EntityList.EntityEggInfo eggInfo = EntityList.ENTITY_EGGS.get(
                    ItemMonsterPlacer.getNamedIdFrom(stack)
            );

            return eggInfo == null ? -1 :
                    (tintIndex == 0 ? eggInfo.primaryColor :
                            eggInfo.secondaryColor);
        },
                MOB_ESSENCE, MOB_AGGLOMERATION, MOB_SPIRIT);
    }
}
