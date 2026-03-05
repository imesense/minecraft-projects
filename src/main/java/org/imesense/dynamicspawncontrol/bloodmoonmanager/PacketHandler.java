package org.imesense.dynamicspawncontrol.bloodmoonmanager;

import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;

public class PacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID);

    public static void init() {
        INSTANCE.registerMessage(HandleBloodmoonStatus.class, MessageBloodmoonStatus.class, 0, Side.CLIENT);
    }
}
