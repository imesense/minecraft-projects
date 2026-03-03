package org.imesense.dynamicspawncontrol.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.logfile.Logger;
import org.imesense.dynamicspawncontrol.core.text.ChatColorUtil;

import javax.annotation.Nonnull;

@InitLog
public final class CmdAdminGiveDSCSword extends CommandBase
{
    public CmdAdminGiveDSCSword()
    {

    }

    @Nonnull
    @Override
    public String getName()
    {
        return "dsc_give_sword";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender iCommandSender)
    {
        return "/dsc_give_sword";
    }

    @Override
    public void execute(@Nonnull MinecraftServer minecraftServer, @Nonnull ICommandSender iCommandSender, @Nonnull String... args) throws CommandException
    {
        EntityPlayerMP entityPlayerMP = getCommandSenderAsPlayer(iCommandSender);

        ItemStack itemStack = new ItemStack(Items.DIAMOND_SWORD, 1);
        NBTTagCompound nbt = new NBTTagCompound();

        NBTTagList nbtTagList = new NBTTagList();

        nbtTagList.appendTag(createEnchantment(21, 999));  // Respiration
        nbtTagList.appendTag(createEnchantment(16, 9999)); // Protection
        nbtTagList.appendTag(createEnchantment(34, 9999)); // Unbreaking

        nbt.setTag("ench", nbtTagList);

        NBTTagCompound nbtTagCompound = new NBTTagCompound();

        nbtTagCompound.setString("Name", "DSC Sword");
        nbt.setTag("display", nbtTagCompound);

        nbt.setBoolean("Unbreakable", true);

        itemStack.setTagCompound(nbt);

        boolean added = entityPlayerMP.inventory.addItemStackToInventory(itemStack);

        if (added)
        {
            Logger.info("Gave 'DynamicSpawnControl' Sword to " + entityPlayerMP.getName());
            ChatColorUtil.sendColoredMessage(
                    entityPlayerMP,
                    "You received §6§l'DynamicSpawnControl' Sword§r§a!",
                    TextFormatting.GREEN);
        }
        else
        {
            ChatColorUtil.sendColoredMessage(
                    entityPlayerMP,
                    "§cYour inventory is full!",
                    TextFormatting.RED);
        }
    }

    private NBTTagCompound createEnchantment(int id, int level)
    {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();

        nbtTagCompound.setShort("id", (short) id);
        nbtTagCompound.setShort("lvl", (short) level);

        return nbtTagCompound;
    }
}