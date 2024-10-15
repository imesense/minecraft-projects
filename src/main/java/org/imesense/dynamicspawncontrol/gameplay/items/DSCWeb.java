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
import org.imesense.dynamicspawncontrol.ai.spider.utils.attackweb.EntityThrowableWeb;
import org.imesense.dynamicspawncontrol.technical.customlibrary.ObjectHandler;

public final class DSCWeb extends Item
{
    public DSCWeb()
    {
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.MISC);
        ObjectHandler.RegistrationHandler.nameHelper(this, "dynamicspawncontrol:webbing");
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (!playerIn.capabilities.isCreativeMode)
        {
            itemstack.shrink(1);
        }

        EntityThrowableWeb.sling(worldIn, playerIn);
        playerIn.addStat(StatList.getObjectUseStats(this));

        return new ActionResult(EnumActionResult.SUCCESS, itemstack);
    }
}
