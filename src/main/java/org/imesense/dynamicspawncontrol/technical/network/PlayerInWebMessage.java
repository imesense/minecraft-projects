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
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;

/**
 *
 */
public final class PlayerInWebMessage implements IMessage
{
    /**
     *
     */
    public BlockPos pos;

    /**
     *
     */
    public PlayerInWebMessage()
    {
        CodeGenericUtils.printInitClassToLog(this.getClass());
    }

    /**
     *
     * @param pos
     */
    public PlayerInWebMessage(BlockPos pos)
    {
        this.pos = pos;
    }

    /**
     *
     * @param buf
     */
    public void fromBytes(ByteBuf buf)
    {
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    /**
     *
     * @param buf
     */
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
    }

    /**
     *
     * @param netWrapper
     */
    public static void register(SimpleNetworkWrapper netWrapper)
    {
        netWrapper.registerMessage(PlayerInWebMessage.Handler.class,
                PlayerInWebMessage.class, 0, Side.CLIENT);
    }

    /**
     *
     */
    public static class Handler implements IMessageHandler<PlayerInWebMessage, IMessage>
    {
        /**
         *
         * @param msg The message
         * @param ctx
         * @return
         */
        public IMessage onMessage(final PlayerInWebMessage msg, MessageContext ctx)
        {
            /**
             *
             */
            Minecraft getMinecraft = Minecraft.getMinecraft();

            /**
             *
             */
            getMinecraft.addScheduledTask(new Runnable()
            {
                /**
                 *
                 */
                public void run()
                {
                    getMinecraft.world.setBlockState(msg.pos, Blocks.WEB.getDefaultState());
                    getMinecraft.player.setInWeb();
                }
            });

            return null;
        }
    }
}
