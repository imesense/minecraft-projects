package org.imesense.dynamicspawncontrol.core.plugin.mod.time_control_mod_forge_1_12_2.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.logging.log4j.LogManager;
import org.imesense.dynamicspawncontrol.core.pluginconfig.timecontrol.PluginTimeControlConfig;

public final class PacketGameRule implements IMessage
{
    private boolean doDaylightCycle_tc;

    public PacketGameRule()
    {
    }

    public PacketGameRule(boolean doDaylightCycle_tc)
    {
        this.doDaylightCycle_tc = doDaylightCycle_tc;
    }

    public void toBytes(ByteBuf byteBuf)
    {
        byteBuf.writeBoolean(this.doDaylightCycle_tc);
    }

    public void fromBytes(ByteBuf byteBuf)
    {
        this.doDaylightCycle_tc = byteBuf.readBoolean();
    }

    public static class Handler implements IMessageHandler<PacketGameRule, IMessage>
    {
        public IMessage onMessage(PacketGameRule packetGameRule, MessageContext messageContext)
        {
            Minecraft.getMinecraft().addScheduledTask(() ->
            {
                Minecraft.getMinecraft().world.getGameRules().setOrCreateGameRule("doDaylightCycle_tc",
                        Boolean.toString(packetGameRule.doDaylightCycle_tc));

                if (PluginTimeControlConfig.getInstance(PluginTimeControlConfig.class).isTimeControlDebug())
                {
                    LogManager.getLogger().info("Network packet for gamerule doDaylightCycle_tc received, value: " +
                            packetGameRule.doDaylightCycle_tc);
                }

            });
            return null;
        }
    }
}
