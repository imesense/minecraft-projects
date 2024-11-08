package org.imesense.dynamicspawncontrol.plugin.realisticexplosionphysics_1_12_2_1_0_0;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import org.imesense.dynamicspawncontrol.plugin.realisticexplosionphysics_1_12_2_1_0_0.physics.IExplosionHandler;
import org.imesense.dynamicspawncontrol.plugin.realisticexplosionphysics_1_12_2_1_0_0.physics.PhysicsHandler;
import org.imesense.dynamicspawncontrol.plugin.realisticexplosionphysics_1_12_2_1_0_0.physics.RBPExplosionHandler;
import org.imesense.dynamicspawncontrol.plugin.realisticexplosionphysics_1_12_2_1_0_0.physics.VanillaExplosionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public final class RealisticExplosionPhysics
{
    public static IExplosionHandler handler;
    public static final List<PhysicsHandler> physicsHandlers = new ArrayList();

    public static void postInit(FMLPostInitializationEvent event) {
        boolean rbpActive = false;
        Iterator var2 = Loader.instance().getActiveModList().iterator();

        while(var2.hasNext()) {
            ModContainer mod = (ModContainer)var2.next();
            if (mod.getModId().equals("rbp") && mod.getName().equals("Realistic Block Physics")) {
                rbpActive = true;
            }
        }

        if (rbpActive) {
            handler = new RBPExplosionHandler();
        } else {
            handler = new VanillaExplosionHandler();
        }

    }

    public static void onServerShutdown(FMLServerStoppingEvent event) {
        physicsHandlers.clear();
    }

    public static final PhysicsHandler getPhysicsHandler(World worldIn) {
        for(int i = 0; i < physicsHandlers.size(); ++i) {
            if (((PhysicsHandler)physicsHandlers.get(i)).getWorld().equals(worldIn)) {
                return (PhysicsHandler)physicsHandlers.get(i);
            }
        }

        return null;
    }
}