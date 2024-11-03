package org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.network;

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
    public static SimpleNetworkWrapper Instance;

    /**
     *
     */
    public static void init()
    {
        Instance.registerMessage(PacketTime.Handler.class, PacketTime.class, 0, Side.CLIENT);
        Instance.registerMessage(PacketGameRule.Handler.class, PacketGameRule.class, 1, Side.CLIENT);
    }

    /**
     *
     */
    static
    {
        Instance = NetworkRegistry.INSTANCE.newSimpleChannel("timecontrol_a");
    }
}
