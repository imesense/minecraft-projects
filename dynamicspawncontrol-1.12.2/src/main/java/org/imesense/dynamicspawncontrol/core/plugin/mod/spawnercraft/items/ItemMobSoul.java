package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.items;

import javax.annotation.Nonnull;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
/* loaded from: input.jar:cad97/spawnercraft/items/ItemMobSoul.class */
public abstract class ItemMobSoul extends SpawnerCraftItem {
    static final /* synthetic */ boolean $assertionsDisabled;

    @Override // cad97.spawnercraft.items.SpawnerCraftItem
    @Nonnull
    public /* bridge */ /* synthetic */ Item func_77655_b(@Nonnull String str) {
        return super.func_77655_b(str);
    }

    static {
        $assertionsDisabled = !ItemMobSoul.class.desiredAssertionStatus();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ItemMobSoul() {
        func_77625_d(64);
        func_77627_a(true);
    }

    @Nonnull
    public String func_77653_i(@Nonnull ItemStack stack) {
        String itemName = I18n.func_74838_a(func_77658_a() + ".name").trim();
        String mobName = EntityList.func_191302_a(ItemMonsterPlacer.func_190908_h(stack));
        if (mobName != null) {
            mobName = I18n.func_74838_a("entity." + mobName + ".name");
        }
        return String.format(itemName, mobName);
    }

    @SideOnly(Side.CLIENT)
    public void func_150895_a(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (func_194125_a(tab)) {
            for (EntityList.EntityEggInfo entitylist$entityegginfo : EntityList.field_75627_a.values()) {
                ItemStack itemstack = new ItemStack(this, 1);
                applyEntityIdToItemStack(itemstack, entitylist$entityegginfo.field_75613_a);
                items.add(itemstack);
            }
        }
    }

    public static void applyEntityIdToItemStack(ItemStack stack, ResourceLocation entityId) {
        NBTTagCompound nbttagcompound = stack.func_77942_o() ? stack.func_77978_p() : new NBTTagCompound();
        if (!$assertionsDisabled && nbttagcompound == null) {
            throw new AssertionError();
        }
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        nbttagcompound1.func_74778_a("id", entityId.toString());
        nbttagcompound.func_74782_a("EntityTag", nbttagcompound1);
        stack.func_77982_d(nbttagcompound);
    }
}

