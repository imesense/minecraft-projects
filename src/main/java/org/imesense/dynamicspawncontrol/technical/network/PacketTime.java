package org.imesense.dynamicspawncontrol.technical.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.imesense.dynamicspawncontrol.UniqueField;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.primitive.OnUpdateTimeWorld;

/**
 *
 */
public final class PacketTime implements IMessage
{
    /**
     *
     */
    private long customTime;

    /**
     *
     */
    private double multiplier;

    /**
     *
     */
    public PacketTime()
    {

    }

    /**
     *
     * @param customTime
     * @param multiplier
     */
    public PacketTime(long customTime, double multiplier)
    {
        this.customTime = customTime;
        this.multiplier = multiplier;
    }

    /**
     *
     * @param buf
     */
    public void toBytes(ByteBuf buf)
    {
        buf.writeLong(this.customTime);
        buf.writeDouble(this.multiplier);
    }

    /**
     *
     * @param buf
     */
    public void fromBytes(ByteBuf buf)
    {
        this.customTime = buf.readLong();
        this.multiplier = buf.readDouble();
    }

    /**
     *
     */
    public static class Handler implements IMessageHandler<PacketTime, IMessage>
    {
        /**
         *
         * @param message
         * @param ctx
         * @return
         */
        public IMessage onMessage(PacketTime message, MessageContext ctx)
        {
            UniqueField.CLIENT.addScheduledTask(() ->
            {
                OnUpdateTimeWorld.INSTANCE.clientUpdate(message.customTime, message.multiplier);
            });

            return null;
        }
    }
}
