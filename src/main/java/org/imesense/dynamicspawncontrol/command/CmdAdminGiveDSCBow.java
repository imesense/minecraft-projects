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
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.text.ChatColorUtil;

import javax.annotation.Nonnull;

/**
 *
 */
@InitLog
public final class CmdAdminGiveDSCBow extends CommandBase
{
    /**
     *
     */
    public CmdAdminGiveDSCBow()
    {

    }

    /**
     *
     * @return
     */
    @Nonnull
    @Override
    public String getName()
    {
        return "dsc_give_bow";
    }

    /**
     *
     * @param iCommandSender
     * @return
     */
    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender iCommandSender)
    {
        return "/dsc_give_bow";
    }

    /**
     *
     * @param minecraftServer
     * @param iCommandSender
     * @param args
     * @throws CommandException
     */
    @Override
    public void execute(@Nonnull MinecraftServer minecraftServer, @Nonnull ICommandSender iCommandSender, @Nonnull String... args) throws CommandException
    {
        EntityPlayerMP entityPlayerMP = getCommandSenderAsPlayer(iCommandSender);

        ItemStack itemStack = new ItemStack(Items.BOW, 1);
        NBTTagCompound nbtTagCompound = new NBTTagCompound();

        NBTTagList nbtTagList = new NBTTagList();

        nbtTagList.appendTag(createEnchantment(48, 1000)); // Power
        nbtTagList.appendTag(createEnchantment(49, 1000)); // Punch
        nbtTagList.appendTag(createEnchantment(50, 1000)); // Flame
        nbtTagList.appendTag(createEnchantment(51, 1000)); // Infinity

        nbtTagCompound.setTag("ench", nbtTagList);

        NBTTagCompound nbtTagCompound1 = new NBTTagCompound();

        nbtTagCompound1.setString("Name", "DSC Bow");
        nbtTagCompound.setTag("display", nbtTagCompound1);

        nbtTagCompound.setBoolean("Unbreakable", true);

        itemStack.setTagCompound(nbtTagCompound);

        boolean added = entityPlayerMP.inventory.addItemStackToInventory(itemStack);

        if (added)
        {
            Log.write(0, "Gave 'DynamicSpawnControl' Bow to " + entityPlayerMP.getName());
            ChatColorUtil.sendColoredMessage(
                    entityPlayerMP,
                    "You received §3§l'DynamicSpawnControl' Bow§r§a!",
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

    /**
     *
     * @param id
     * @param level
     * @return
     */
    private NBTTagCompound createEnchantment(int id, int level)
    {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();

        nbtTagCompound.setShort("id", (short) id);
        nbtTagCompound.setShort("lvl", (short) level);

        return nbtTagCompound;
    }
}