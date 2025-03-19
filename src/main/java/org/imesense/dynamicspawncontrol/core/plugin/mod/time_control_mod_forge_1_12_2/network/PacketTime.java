package org.imesense.dynamicspawncontrol.core.plugin.mod.time_control_mod_forge_1_12_2.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.imesense.dynamicspawncontrol.core.plugin.mod.time_control_mod_forge_1_12_2.TimeEvents;

public final class PacketTime implements IMessage
{
    private long customTime;
    private double multiplier;

    public PacketTime()
    {
    }

    public PacketTime(long customTime, double multiplier)
    {
        this.customTime = customTime;
        this.multiplier = multiplier;
    }

    public void toBytes(ByteBuf byteBuf)
    {
        byteBuf.writeLong(this.customTime);
        byteBuf.writeDouble(this.multiplier);
    }

    public void fromBytes(ByteBuf byteBuf)
    {
        this.customTime = byteBuf.readLong();
        this.multiplier = byteBuf.readDouble();
    }

    public static class Handler implements IMessageHandler<PacketTime, IMessage>
    {
        public IMessage onMessage(PacketTime message, MessageContext ctx)
        {
            Minecraft.getMinecraft().addScheduledTask(() ->
            {
                TimeEvents.INSTANCE.clientUpdate(message.customTime, message.multiplier);
            });

            return null;
        }
    }
}

