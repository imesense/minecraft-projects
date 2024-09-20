package org.imesense.dynamicspawncontrol.gameplay.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 *
 */
public final class cmdAdminDumpItem extends CommandBase
{
    /**
     *
     * @param nameClass
     */
    public cmdAdminDumpItem(final String nameClass)
    {
        Log.writeDataToLogFile(0, nameClass);
    }

    /**
     *
     * @return
     */
    @Nonnull
    @Override
    public String getName()
    {
        return "dsc_dump_item";
    }

    /**
     *
     * @param sender
     * @return
     */
    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender)
    {
        return "/dsc_dump_item";
    }

    /**
     *
     * @param server
     * @param sender
     * @param args
     */
    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String... args)
    {
        if (sender instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) sender;
            ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
            Item item = heldItem.getItem();
            sender.sendMessage(new TextComponentString(TextFormatting.GOLD + Objects.requireNonNull(item.getRegistryName()).toString()));
            Log.writeDataToLogFile(0, Objects.requireNonNull(item.getRegistryName()).toString());
            NBTTagCompound nbt = heldItem.getTagCompound();
            if (nbt != null)
            {
                dumpNBT(sender, 2, nbt);
            }
        }
        else
        {
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "Эту команду могут использовать только игроки!"));
        }
    }

    /**
     *
     * @param sender
     * @param indent
     * @param nbt
     */
    private static void dumpNBT(ICommandSender sender, int indent, NBTTagCompound nbt)
    {
        for (String key : nbt.getKeySet())
        {
            NBTBase base = nbt.getTag(key);
            byte id = base.getId();
            switch (id)
            {
                case Constants.NBT.TAG_INT:
                    sender.sendMessage(new TextComponentString(StringUtils.repeat(' ', indent) + "(Int) " + key + " = " + nbt.getInteger(key)));
                    Log.writeDataToLogFile(0, StringUtils.repeat(' ', indent) + "(Int) " + key + " = " + nbt.getInteger(key));
                    break;
                case Constants.NBT.TAG_LONG:
                    sender.sendMessage(new TextComponentString(StringUtils.repeat(' ', indent) + "(Long) " + key + " = " + nbt.getLong(key)));
                    Log.writeDataToLogFile(0, StringUtils.repeat(' ', indent) + "(Long) " + key + " = " + nbt.getLong(key));
                    break;
                case Constants.NBT.TAG_DOUBLE:
                    sender.sendMessage(new TextComponentString(StringUtils.repeat(' ', indent) + "(Double) " + key + " = " + nbt.getDouble(key)));
                    Log.writeDataToLogFile(0, StringUtils.repeat(' ', indent) + "(Double) " + key + " = " + nbt.getDouble(key));
                    break;
                case Constants.NBT.TAG_FLOAT:
                    sender.sendMessage(new TextComponentString(StringUtils.repeat(' ', indent) + "(Float) " + key + " = " + nbt.getFloat(key)));
                    Log.writeDataToLogFile(0, StringUtils.repeat(' ', indent) + "(Float) " + key + " = " + nbt.getFloat(key));
                    break;
                case Constants.NBT.TAG_STRING:
                    sender.sendMessage(new TextComponentString(StringUtils.repeat(' ', indent) + "(String) " + key + " = " + nbt.getString(key)));
                    Log.writeDataToLogFile(0, StringUtils.repeat(' ', indent) + "(String) " + key + " = " + nbt.getString(key));
                    break;
                case Constants.NBT.TAG_BYTE:
                    sender.sendMessage(new TextComponentString(StringUtils.repeat(' ', indent) + "(Byte) " + key + " = " + nbt.getByte(key)));
                    Log.writeDataToLogFile(0, StringUtils.repeat(' ', indent) + "(Byte) " + key + " = " + nbt.getByte(key));
                    break;
                case Constants.NBT.TAG_SHORT:
                    sender.sendMessage(new TextComponentString(StringUtils.repeat(' ', indent) + "(Short) " + key + " = " + nbt.getShort(key)));
                    Log.writeDataToLogFile(0, StringUtils.repeat(' ', indent) + "(Short) " + key + " = " + nbt.getShort(key));
                    break;
                case Constants.NBT.TAG_LIST:
                    sender.sendMessage(new TextComponentString(StringUtils.repeat(' ', indent) + "(List) " + key));
                    Log.writeDataToLogFile(0, StringUtils.repeat(' ', indent) + "(List) " + key);
                    NBTBase b = nbt.getTag(key);

                    if (((NBTTagList)b).getTagType() == Constants.NBT.TAG_COMPOUND)
                    {
                        NBTTagList list = nbt.getTagList(key, Constants.NBT.TAG_COMPOUND);
                        int idx = 0;
                        for (NBTBase bs : list)
                        {
                            sender.sendMessage(new TextComponentString(TextFormatting.YELLOW + StringUtils.repeat(' ', indent+2) + "Index " + idx));
                            Log.writeDataToLogFile(0, StringUtils.repeat(' ', indent+2) + "Index " + idx);
                            idx++;
                            dumpNBT(sender, indent + 4, (NBTTagCompound) bs);
                        }
                    }
                    break;
                case Constants.NBT.TAG_COMPOUND:
                    sender.sendMessage(new TextComponentString(StringUtils.repeat(' ', indent) + "(NBT) " + key));
                    Log.writeDataToLogFile(0, StringUtils.repeat(' ', indent) + "(NBT) " + key);
                    dumpNBT(sender, indent + 2, nbt.getCompoundTag(key));
                    break;
                default:
                    sender.sendMessage(new TextComponentString(StringUtils.repeat(' ', indent) + "(?) " + key));
                    Log.writeDataToLogFile(0, StringUtils.repeat(' ', indent) + "(?) " + key);
                    break;
            }
        }
    }
}
