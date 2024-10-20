package org.imesense.dynamicspawncontrol.gameplay.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.gameplay.throwingobjects.DSCThrowItemWeb;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.customlibrary.ObjectHandler;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 *
 */
public final class DSCWeb extends Item
{
    /**
     *
     */
    public DSCWeb()
    {
        CodeGenericUtils.printInitClassToLog(this.getClass());

        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.MISC);

        ObjectHandler.RegistrationHandler.nameHelper(this, "dynamicspawncontrol:webbing");
    }

    /**
     *
     * @param worldIn
     * @param playerIn
     * @param handIn
     * @return
     */
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (!playerIn.capabilities.isCreativeMode)
        {
            itemstack.shrink(1);
        }

        DSCThrowItemWeb.sling(worldIn, playerIn);
        playerIn.addStat(Objects.requireNonNull(StatList.getObjectUseStats(this)));

        return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
    }
}
