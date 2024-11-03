package org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.logging.log4j.LogManager;
import org.imesense.dynamicspawncontrol.UniqueField;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.config.DataTimeControl;

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
        CodeGenericUtil.printInitClassToLog(this.getClass());
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
     * @param byteBuf
     */
    public void toBytes(ByteBuf byteBuf)
    {
        byteBuf.writeBoolean(this.doDaylightCycle_tc);
    }

    /**
     *
     * @param byteBuf
     */
    public void fromBytes(ByteBuf byteBuf)
    {
        this.doDaylightCycle_tc = byteBuf.readBoolean();
    }

    /**
     *
     */
    public static class Handler implements IMessageHandler<PacketGameRule, IMessage>
    {
        /**
         *
         * @param packetGameRule
         * @param messageContext
         * @return
         */
        public IMessage onMessage(PacketGameRule packetGameRule, MessageContext messageContext)
        {
            UniqueField.CLIENT.addScheduledTask(() ->
            {
                UniqueField.CLIENT.world.getGameRules().setOrCreateGameRule(
                        "doDaylightCycle_tc", Boolean.toString(packetGameRule.doDaylightCycle_tc));

                if (DataTimeControl.ConfigDataWorldTime.Instance.getTimeControlDebug())
                {
                    LogManager.getLogger().info("Network packet for game_rule doDaylightCycle_tc received, value: " +
                            packetGameRule.doDaylightCycle_tc);
                }
            });

            return null;
        }
    }
}
