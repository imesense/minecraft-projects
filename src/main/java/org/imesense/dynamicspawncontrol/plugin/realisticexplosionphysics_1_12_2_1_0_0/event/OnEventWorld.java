package org.imesense.dynamicspawncontrol.plugin.realisticexplosionphysics_1_12_2_1_0_0.event;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Unload;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.plugin.realisticexplosionphysics_1_12_2_1_0_0.physics.PhysicsHandler;

import static org.imesense.dynamicspawncontrol.plugin.realisticexplosionphysics_1_12_2_1_0_0.RealisticExplosionPhysics.*;

public class OnEventWorld
{
    @SubscribeEvent
    public static final void onWorldLoaded(Load event) {
        if (!event.getWorld().isRemote) {
            PhysicsHandler physicsHandler = new PhysicsHandler(event.getWorld(), handler);
            MinecraftForge.EVENT_BUS.register(physicsHandler);
            physicsHandlers.add(physicsHandler);
        }
    }

    @SubscribeEvent
    public static final void onWorldUnloaded(Unload event) {
        if (!event.getWorld().isRemote) {
            PhysicsHandler physicsHandler = getPhysicsHandler(event.getWorld());
            if (physicsHandler != null) {
                physicsHandlers.remove(physicsHandler);
                MinecraftForge.EVENT_BUS.unregister(physicsHandler);
            }

        }
    }
}
