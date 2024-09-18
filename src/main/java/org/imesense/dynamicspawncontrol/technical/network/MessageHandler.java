package org.imesense.dynamicspawncontrol.technical.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 *
 */
public final class MessageHandler
{
    /**
     *
     */
    public static final SimpleNetworkWrapper INSTANCE;

    /**
     *
     */
    public static void init()
    {
        INSTANCE.registerMessage(PacketTime.Handler.class, PacketTime.class, 0, Side.CLIENT);
        INSTANCE.registerMessage(PacketGameRule.Handler.class, PacketGameRule.class, 1, Side.CLIENT);
    }

    /**
     *
     */
    static
    {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("timecontrol_a");
    }
}
