package org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.imesense.dynamicspawncontrol.UniqueField;
import org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.TimeEvents;

public class PacketTime implements IMessage {
    private long customtime;
    private double multiplier;

    public PacketTime() {
    }

    public PacketTime(long customtime, double multiplier) {
        this.customtime = customtime;
        this.multiplier = multiplier;
    }

    public void toBytes(ByteBuf buf) {
        buf.writeLong(this.customtime);
        buf.writeDouble(this.multiplier);
    }

    public void fromBytes(ByteBuf buf) {
        this.customtime = buf.readLong();
        this.multiplier = buf.readDouble();
    }

    public static class Handler implements IMessageHandler<PacketTime, IMessage> {
        public IMessage onMessage(PacketTime message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                TimeEvents.INSTANCE.clientUpdate(message.customtime, message.multiplier);
            });
            return null;
        }
    }
}

