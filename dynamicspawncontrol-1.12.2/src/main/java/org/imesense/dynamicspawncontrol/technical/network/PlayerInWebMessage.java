package org.imesense.dynamicspawncontrol.technical.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PlayerInWebMessage implements IMessage
{
    public BlockPos pos;

    public PlayerInWebMessage()
    {

    }

    public PlayerInWebMessage(BlockPos pos)
    {
        this.pos = pos;
    }

    public void fromBytes(ByteBuf buf)
    {
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
    }

    public static void register(SimpleNetworkWrapper netWrapper)
    {
        netWrapper.registerMessage(PlayerInWebMessage.Handler.class, PlayerInWebMessage.class, 0, Side.CLIENT);
    }

    public static class Handler implements IMessageHandler<PlayerInWebMessage, IMessage>
    {
        public IMessage onMessage(final PlayerInWebMessage msg, MessageContext ctx)
        {
            final Minecraft mc = Minecraft.getMinecraft();

            mc.addScheduledTask(new Runnable()
            {
                public void run()
                {
                    mc.world.setBlockState(msg.pos, Blocks.WEB.getDefaultState());
                    mc.player.setInWeb();
                }
            });

            return null;
        }
    }
}
