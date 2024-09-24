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
    public static SimpleNetworkWrapper instance;

    /**
     *
     */
    public static void init()
    {
        instance.registerMessage(PacketTime.Handler.class, PacketTime.class, 0, Side.CLIENT);
        instance.registerMessage(PacketGameRule.Handler.class, PacketGameRule.class, 1, Side.CLIENT);
    }

    /**
     *
     */
    static
    {
        instance = NetworkRegistry.INSTANCE.newSimpleChannel("timecontrol_a");
    }
}
