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
public abstract class ItemMobSoul extends SpawnerCraftItem
{
    static final /* synthetic */ boolean $assertionsDisabled;

    @Override // cad97.spawnercraft.items.SpawnerCraftItem
    @Nonnull
    public /* bridge */ /* synthetic */ Item setUnlocalizedName(@Nonnull String str)
    {
        return super.setUnlocalizedName(str);
    }

    static
    {
        $assertionsDisabled = !ItemMobSoul.class.desiredAssertionStatus();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ItemMobSoul()
    {
        setMaxStackSize(64);
        setHasSubtypes(true);
    }

    @Nonnull
    public String getItemStackDisplayName(@Nonnull ItemStack stack)
    {
        String itemName = I18n.translateToLocal(getUnlocalizedName() + ".name").trim();
        String mobName = EntityList.getTranslationName(ItemMonsterPlacer.getNamedIdFrom(stack));

        if (mobName != null)
        {
            mobName = I18n.translateToLocal("entity." + mobName + ".name");
        }

        return String.format(itemName, mobName);
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items)
    {
        if (isInCreativeTab(tab))
        {
            for (EntityList.EntityEggInfo entitylist$entityegginfo : EntityList.ENTITY_EGGS.values())
            {
                ItemStack itemstack = new ItemStack(this, 1);
                applyEntityIdToItemStack(itemstack, entitylist$entityegginfo.spawnedID);
                items.add(itemstack);
            }
        }
    }

    public static void applyEntityIdToItemStack(ItemStack stack, ResourceLocation entityId)
    {
        NBTTagCompound nbttagcompound = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();

        if (!$assertionsDisabled && nbttagcompound == null)
        {
            throw new AssertionError();
        }

        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        nbttagcompound1.setString("id", entityId.toString());
        nbttagcompound.setTag("EntityTag", nbttagcompound1);
        stack.setTagCompound(nbttagcompound);
    }
}

