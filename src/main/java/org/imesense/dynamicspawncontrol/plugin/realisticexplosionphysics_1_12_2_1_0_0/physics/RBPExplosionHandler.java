package org.imesense.dynamicspawncontrol.plugin.realisticexplosionphysics_1_12_2_1_0_0.physics;

import net.minecraft.util.math.BlockPos;

public class RBPExplosionHandler implements IExplosionHandler {
   public void handleBlock(PhysicsHandler handler, BlockPos pos, double motionX, double motionY, double motionZ) {
      RealisticBlockPhysics.summonFallingBlock(handler.getWorld(), pos, handler.getWorld().getBlockState(pos), motionX, motionY, motionZ);
   }
}
