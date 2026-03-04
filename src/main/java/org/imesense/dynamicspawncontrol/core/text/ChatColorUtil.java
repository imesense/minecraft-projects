package org.imesense.dynamicspawncontrol.core.text;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public final class ChatColorUtil
{
    private ChatColorUtil()
    {

    }

    public static String color(String message, TextFormatting color)
    {
        return color + message + TextFormatting.RESET;
    }

    public static void sendColoredMessage(EntityPlayer player, String message, TextFormatting color)
    {
        if (player != null)
        {
            player.sendMessage(new TextComponentString(color(message, color)));
        }
    }
}
