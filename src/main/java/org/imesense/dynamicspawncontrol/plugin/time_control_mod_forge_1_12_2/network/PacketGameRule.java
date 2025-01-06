package org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.logging.log4j.LogManager;
import org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.config.DataTimeControl;

/**
 *
 */
public class PacketGameRule implements IMessage {
    private boolean doDaylightCycle_tc;

    public PacketGameRule() {
    }

    public PacketGameRule(boolean doDaylightCycle_tc) {
        this.doDaylightCycle_tc = doDaylightCycle_tc;
    }

    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(this.doDaylightCycle_tc);
    }

    public void fromBytes(ByteBuf buf) {
        this.doDaylightCycle_tc = buf.readBoolean();
    }

    public static class Handler implements IMessageHandler<PacketGameRule, IMessage> {
        public IMessage onMessage(PacketGameRule message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                Minecraft.getMinecraft().world.getGameRules().setOrCreateGameRule("doDaylightCycle_tc", Boolean.toString(message.doDaylightCycle_tc));
                if (DataTimeControl.ConfigDataWorldTime.Instance.getTimeControlDebug()) {
                    LogManager.getLogger().info("Network packet for gamerule doDaylightCycle_tc received, value: " + message.doDaylightCycle_tc);
                }

            });
            return null;
        }
    }
}
