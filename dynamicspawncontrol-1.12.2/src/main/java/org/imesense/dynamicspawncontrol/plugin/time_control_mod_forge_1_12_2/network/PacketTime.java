package org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.imesense.dynamicspawncontrol.UniqueField;
import org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.TimeEvents;

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
     * @param byteBuf
     */
    public void toBytes(ByteBuf byteBuf)
    {
        byteBuf.writeLong(this.customTime);
        byteBuf.writeDouble(this.multiplier);
    }

    /**
     *
     * @param byteBuf
     */
    public void fromBytes(ByteBuf byteBuf)
    {
        this.customTime = byteBuf.readLong();
        this.multiplier = byteBuf.readDouble();
    }

    /**
     *
     */
    public static class Handler implements IMessageHandler<PacketTime, IMessage>
    {
        /**
         *
         * @param packetTime
         * @param messageContext
         * @return
         */
        public IMessage onMessage(PacketTime packetTime, MessageContext messageContext)
        {
            UniqueField.CLIENT.addScheduledTask(() ->
            {
                TimeEvents.INSTANCE.clientUpdate(packetTime.customTime, packetTime.multiplier);
            });

            return null;
        }
    }
}
