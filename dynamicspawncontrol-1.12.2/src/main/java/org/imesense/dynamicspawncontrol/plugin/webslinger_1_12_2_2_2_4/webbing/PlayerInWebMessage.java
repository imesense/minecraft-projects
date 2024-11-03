package org.imesense.dynamicspawncontrol.plugin.webslinger_1_12_2_2_2_4.webbing;

import io.netty.buffer.ByteBuf;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.imesense.dynamicspawncontrol.UniqueField;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;

/**
 *
 */
public final class PlayerInWebMessage implements IMessage
{
    /**
     *
     */
    public BlockPos BlockPos;

    /**
     *
     */
    public PlayerInWebMessage()
    {
        CodeGenericUtil.printInitClassToLog(this.getClass());
    }

    /**
     *
     * @param blockPos
     */
    public PlayerInWebMessage(BlockPos blockPos)
    {
        this.BlockPos = blockPos;
    }

    /**
     *
     * @param byteBuf
     */
    public void fromBytes(ByteBuf byteBuf)
    {
        this.BlockPos = new BlockPos(byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt());
    }

    /**
     *
     * @param byteBuf
     */
    public void toBytes(ByteBuf byteBuf)
    {
        byteBuf.writeInt(this.BlockPos.getX());
        byteBuf.writeInt(this.BlockPos.getY());
        byteBuf.writeInt(this.BlockPos.getZ());
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
         * @param MSG
         * @param ctx
         * @return
         */
        public IMessage onMessage(final PlayerInWebMessage MSG, MessageContext ctx)
        {
            /**
             *
             */
            UniqueField.CLIENT.addScheduledTask(new Runnable()
            {
                /**
                 *
                 */
                public void run()
                {
                    UniqueField.CLIENT.world.setBlockState(MSG.BlockPos, Blocks.WEB.getDefaultState());
                    UniqueField.CLIENT.player.setInWeb();
                }
            });

            return null;
        }
    }
}
