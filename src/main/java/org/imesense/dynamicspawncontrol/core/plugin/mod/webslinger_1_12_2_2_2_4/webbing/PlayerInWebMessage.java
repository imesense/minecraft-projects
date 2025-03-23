package org.imesense.dynamicspawncontrol.core.plugin.mod.webslinger_1_12_2_2_2_4.webbing;

import io.netty.buffer.ByteBuf;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

@InitLog
public final class PlayerInWebMessage implements IMessage
{
    public BlockPos blockPos;

    public PlayerInWebMessage()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    public PlayerInWebMessage(BlockPos blockPos)
    {
        this.blockPos = blockPos;
    }

    public void fromBytes(ByteBuf byteBuf)
    {
        this.blockPos = new BlockPos(byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt());
    }

    public void toBytes(ByteBuf byteBuf)
    {
        byteBuf.writeInt(this.blockPos.getX());
        byteBuf.writeInt(this.blockPos.getY());
        byteBuf.writeInt(this.blockPos.getZ());
    }

    public static void register(SimpleNetworkWrapper simpleNetworkWrapper)
    {
        simpleNetworkWrapper.registerMessage(PlayerInWebMessage.Handler.class,
                PlayerInWebMessage.class, 0, Side.CLIENT);
    }

    public static class Handler implements IMessageHandler<PlayerInWebMessage, IMessage>
    {
        public IMessage onMessage(PlayerInWebMessage playerInWebMessage, MessageContext messageContext)
        {
            UniqueField.CLIENT.addScheduledTask(new Runnable()
            {
                public void run()
                {
                    UniqueField.CLIENT.world.setBlockState(playerInWebMessage.blockPos, Blocks.WEB.getDefaultState());
                    UniqueField.CLIENT.player.setInWeb();
                }
            });

            return null;
        }
    }
}
