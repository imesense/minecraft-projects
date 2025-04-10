package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.init;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
/* loaded from: input.jar:cad97/spawnercraft/init/SpawnerCraftItems.class */
public class SpawnerCraftItems {
    public static final ItemMobEssence MOB_ESSENCE = new ItemMobEssence();
    public static final ItemMobAgglomeration MOB_AGGLOMERATION = new ItemMobAgglomeration();
    public static final ItemMobSpirit MOB_SPIRIT = new ItemMobSpirit();
    public static final ItemMobRod MOB_ROD = new ItemMobRod();

    public static void registerItems() {
        ForgeRegistries.ITEMS.register(MOB_ESSENCE);
        ForgeRegistries.ITEMS.register(MOB_AGGLOMERATION);
        ForgeRegistries.ITEMS.register(MOB_SPIRIT);
        ForgeRegistries.ITEMS.register(MOB_ROD);
        ForgeRegistries.ITEMS.register(new ItemBlock(SpawnerCraftBlocks.MOB_CAGE).setRegistryName(SpawnerCraftBlocks.MOB_CAGE.getRegistryName()));
        LogHelper.logInfo("Items initialized.");
    }

    @SideOnly(Side.CLIENT)
    public static void registerModels() {
        ModelLoader.setCustomModelResourceLocation(MOB_ESSENCE, 0, new ModelResourceLocation(MOB_ESSENCE.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(MOB_AGGLOMERATION, 0, new ModelResourceLocation(MOB_AGGLOMERATION.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(MOB_SPIRIT, 0, new ModelResourceLocation(MOB_SPIRIT.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(MOB_ROD, 0, new ModelResourceLocation(MOB_ROD.getRegistryName(), "inventory"));
        LogHelper.logInfo("Item models initialized.");
    }

    @SideOnly(Side.CLIENT)
    public static void registerColors(ItemColors itemColors) {
        itemColors.func_186730_a(stack, tintIndex -> {
            EntityList.EntityEggInfo eggInfo = (EntityList.EntityEggInfo) EntityList.field_75627_a.get(ItemMonsterPlacer.func_190908_h(stack));
            if (eggInfo == null) {
                return -1;
            }
            return tintIndex == 0 ? eggInfo.field_75611_b : eggInfo.field_75612_c;
        }, new Item[]{MOB_ESSENCE, MOB_AGGLOMERATION, MOB_SPIRIT});
        LogHelper.logInfo("Item colors initialized.");
    }
}
