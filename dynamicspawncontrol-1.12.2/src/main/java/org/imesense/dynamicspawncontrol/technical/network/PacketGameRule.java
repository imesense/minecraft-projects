package org.imesense.dynamicspawncontrol.technical.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.logging.log4j.LogManager;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.config.gameworldtime.DataPluginWorldTime;

/**
 *
 */
public final class PacketGameRule implements IMessage
{
    /**
     *
     */
    private boolean doDaylightCycle_tc;

    /**
     *
     */
    public PacketGameRule()
    {
        CodeGenericUtils.printInitClassToLog(this.getClass());
    }

    /**
     *
     * @param doDaylightCycle_tc
     */
    public PacketGameRule(boolean doDaylightCycle_tc)
    {
        this.doDaylightCycle_tc = doDaylightCycle_tc;
    }

    /**
     *
     * @param buf
     */
    public void toBytes(ByteBuf buf)
    {
        buf.writeBoolean(this.doDaylightCycle_tc);
    }

    /**
     *
     * @param buf
     */
    public void fromBytes(ByteBuf buf)
    {
        this.doDaylightCycle_tc = buf.readBoolean();
    }

    /**
     *
     */
    public static class Handler implements IMessageHandler<PacketGameRule, IMessage>
    {
        /**
         *
         * @param message
         * @param ctx
         * @return
         */
        public IMessage onMessage(PacketGameRule message, MessageContext ctx)
        {
            Minecraft.getMinecraft().addScheduledTask(() ->
            {
                Minecraft.getMinecraft().world.getGameRules().setOrCreateGameRule("doDaylightCycle_tc", Boolean.toString(message.doDaylightCycle_tc));

                if (DataPluginWorldTime.ConfigDataWorldTime.instance.getTimeControlDebug())
                {
                    LogManager.getLogger().info("Network packet for game_rule doDaylightCycle_tc received, value: " + message.doDaylightCycle_tc);
                }
            });

            return null;
        }
    }
}
