package org.imesense.dynamicspawncontrol.plugin.realisticexplosionphysics_1_12_2_1_0_0.physics;

import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.util.math.BlockPos;

public class VanillaExplosionHandler implements IExplosionHandler {
   public void handleBlock(PhysicsHandler handler, BlockPos pos, double motionX, double motionY, double motionZ) {
      EntityFallingBlock entity = new EntityFallingBlock(handler.getWorld(), (double)((float)pos.getX() + 0.5F), (double)pos.getY(), (double)((float)pos.getZ() + 0.5F), handler.getWorld().getBlockState(pos));
      entity.motionX = motionX;
      entity.motionY = motionY;
      entity.motionZ = motionZ;
      handler.getWorld().spawnEntity(entity);
   }
}
