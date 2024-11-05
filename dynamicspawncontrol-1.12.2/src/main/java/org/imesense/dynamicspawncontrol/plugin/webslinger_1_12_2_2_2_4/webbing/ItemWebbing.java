package org.imesense.dynamicspawncontrol.plugin.webslinger_1_12_2_2_2_4.webbing;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.plugin.webslinger_1_12_2_2_2_4.ObjectHandlerClient;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 *
 */
public final class ItemWebbing extends Item
{
    /**
     *
     */
    public ItemWebbing()
    {
        CodeGeneric.printInitClassToLog(this.getClass());

        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.MISC);

        ObjectHandlerClient.RegistrationHandler.nameHelper(this, "dynamicspawncontrol:webbing");
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
        ItemStack itemStack = playerIn.getHeldItem(handIn);

        if (!playerIn.capabilities.isCreativeMode)
        {
            itemStack.shrink(1);
        }

        EntityWebbing.sling(worldIn, playerIn);
        playerIn.addStat(Objects.requireNonNull(StatList.getObjectUseStats(this)));

        return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
    }
}
