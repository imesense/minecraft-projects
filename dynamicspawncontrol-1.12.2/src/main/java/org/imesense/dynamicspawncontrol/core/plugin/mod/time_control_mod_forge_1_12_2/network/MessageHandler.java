package org.imesense.dynamicspawncontrol.core.plugin.mod.time_control_mod_forge_1_12_2.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class MessageHandler
{
    public static final SimpleNetworkWrapper INSTANCE;

    public static void init()
    {
        INSTANCE.registerMessage(PacketTime.Handler.class, PacketTime.class, 0, Side.CLIENT);
        INSTANCE.registerMessage(PacketGameRule.Handler.class, PacketGameRule.class, 1, Side.CLIENT);
    }

    static
    {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("timecontrol_a");
    }
}
