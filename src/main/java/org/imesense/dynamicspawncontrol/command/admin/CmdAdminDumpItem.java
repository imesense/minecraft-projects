package org.imesense.dynamicspawncontrol.command.admin;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
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
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.logfile.LogManager;

import javax.annotation.Nonnull;
import java.util.Objects;

@InitLog
public final class CmdAdminDumpItem extends CommandBase
{
    public CmdAdminDumpItem()
    {

    }

    @Nonnull
    @Override
    public String getName()
    {
        return "dsc_dump_item";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender iCommandSender)
    {
        return "/dsc_dump_item";
    }

    @Override
    public void execute(@Nonnull MinecraftServer minecraftServer, @Nonnull ICommandSender iCommandSender, @Nonnull String... args)
    {
        if (iCommandSender instanceof EntityPlayerMP)
        {
            EntityPlayerMP entityPlayerMP = (EntityPlayerMP) iCommandSender;
            ItemStack itemStack = entityPlayerMP.getHeldItem(EnumHand.MAIN_HAND);

            Item item = itemStack.getItem();
            iCommandSender.sendMessage(new TextComponentString(TextFormatting.GOLD +
                    Objects.requireNonNull(item.getRegistryName()).toString()));

            LogManager.info(Objects.requireNonNull(item.getRegistryName()).toString());

            NBTTagCompound nbtTagCompound = itemStack.getTagCompound();
            if (nbtTagCompound != null)
            {
                dumpNBT(iCommandSender, 2, nbtTagCompound);
            }
        }
        else
        {
            iCommandSender.sendMessage(new TextComponentString(TextFormatting.RED + "Эту команду могут использовать только игроки!"));
        }
    }

    private static void dumpNBT(ICommandSender iCommandSender, int indent, NBTTagCompound nbtTagCompound)
    {
        for (String key : nbtTagCompound.getKeySet())
        {
            NBTBase nbtBase = nbtTagCompound.getTag(key);
            byte id = nbtBase.getId();

            switch (id)
            {
                case Constants.NBT.TAG_INT:
                    iCommandSender.sendMessage(new TextComponentString(StringUtils.repeat(' ', indent) + "(Int) " +
                            key + " = " + nbtTagCompound.getInteger(key)));

                    LogManager.info(StringUtils.repeat(' ', indent) + "(Int) " + key + " = " +
                            nbtTagCompound.getInteger(key));

                    break;
                case Constants.NBT.TAG_LONG:
                    iCommandSender.sendMessage(new TextComponentString(StringUtils.repeat(' ', indent) + "(Long) " +
                            key + " = " + nbtTagCompound.getLong(key)));

                    LogManager.info(StringUtils.repeat(' ', indent) + "(Long) " + key + " = " +
                            nbtTagCompound.getLong(key));

                    break;
                case Constants.NBT.TAG_DOUBLE:
                    iCommandSender.sendMessage(new TextComponentString(StringUtils.repeat(' ', indent) + "(Double) " +
                            key + " = " + nbtTagCompound.getDouble(key)));

                    LogManager.info(StringUtils.repeat(' ', indent) + "(Double) " + key + " = " +
                            nbtTagCompound.getDouble(key));

                    break;
                case Constants.NBT.TAG_FLOAT:
                    iCommandSender.sendMessage(new TextComponentString(StringUtils.repeat(' ', indent) + "(Float) " +
                            key + " = " + nbtTagCompound.getFloat(key)));

                    LogManager.info(StringUtils.repeat(' ', indent) + "(Float) " + key + " = " +
                            nbtTagCompound.getFloat(key));

                    break;
                case Constants.NBT.TAG_STRING:
                    iCommandSender.sendMessage(new TextComponentString(StringUtils.repeat(' ', indent) + "(String) " +
                            key + " = " + nbtTagCompound.getString(key)));

                    LogManager.info(StringUtils.repeat(' ', indent) + "(String) " + key + " = " +
                            nbtTagCompound.getString(key));

                    break;
                case Constants.NBT.TAG_BYTE:
                    iCommandSender.sendMessage(new TextComponentString(StringUtils.repeat(' ', indent) + "(Byte) " +
                            key + " = " + nbtTagCompound.getByte(key)));

                    LogManager.info(StringUtils.repeat(' ', indent) + "(Byte) " + key + " = " +
                            nbtTagCompound.getByte(key));

                    break;
                case Constants.NBT.TAG_SHORT:
                    iCommandSender.sendMessage(new TextComponentString(StringUtils.repeat(' ', indent) + "(Short) " +
                            key + " = " + nbtTagCompound.getShort(key)));

                    LogManager.info(StringUtils.repeat(' ', indent) + "(Short) " + key + " = " +
                            nbtTagCompound.getShort(key));

                    break;
                case Constants.NBT.TAG_LIST:
                    iCommandSender.sendMessage(new TextComponentString(StringUtils.repeat(' ', indent) + "(List) " + key));
                    LogManager.info(StringUtils.repeat(' ', indent) + "(List) " + key);
                    NBTBase nbtBase1 = nbtTagCompound.getTag(key);

                    if (((NBTTagList)nbtBase1).getTagType() == Constants.NBT.TAG_COMPOUND)
                    {
                        int idx = 0;

                        NBTTagList nbtTagList = nbtTagCompound.getTagList(key, Constants.NBT.TAG_COMPOUND);

                        for (NBTBase nbtBase2 : nbtTagList)
                        {
                            iCommandSender.sendMessage(new TextComponentString(TextFormatting.YELLOW +
                                    StringUtils.repeat(' ', indent+2) + "Index " + idx));

                            LogManager.info(StringUtils.repeat(' ', indent+2) + "Index " + idx);
                            idx++;
                            dumpNBT(iCommandSender, indent + 4, (NBTTagCompound) nbtBase2);
                        }
                    }
                    break;
                case Constants.NBT.TAG_COMPOUND:
                    iCommandSender.sendMessage(new TextComponentString(StringUtils.repeat(' ', indent) + "(NBT) " + key));
                    LogManager.info(StringUtils.repeat(' ', indent) + "(NBT) " + key);
                    dumpNBT(iCommandSender, indent + 2, nbtTagCompound.getCompoundTag(key));
                    break;
                default:
                    iCommandSender.sendMessage(new TextComponentString(StringUtils.repeat(' ', indent) + "(?) " + key));
                    LogManager.info(StringUtils.repeat(' ', indent) + "(?) " + key);
                    break;
            }
        }
    }
}
