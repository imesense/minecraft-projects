package org.imesense.dynamicspawncontrol.plugin.realisticexplosionphysics_1_12_2_1_0_0.physics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.BlockTNT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PhysicsHandler {
   private final World world;
   private final IExplosionHandler explosionHandler;

   public PhysicsHandler(World world, IExplosionHandler explosionHandler) {
      this.world = world;
      this.explosionHandler = explosionHandler;
   }

   @SubscribeEvent
   public final void onExplosion(Detonate event) {
      if (!event.getWorld().isRemote && event.getWorld().equals(this.world)) {
         List<BlockPos> affectedBlocks = event.getAffectedBlocks();
         List<BlockPos> movedBlocks = new ArrayList();

         BlockPos pos;
         for(int i = 0; i < affectedBlocks.size(); ++i) {
            pos = (BlockPos)affectedBlocks.get(i);
            boolean aboveExplosion = (double)pos.getY() >= event.getExplosion().getPosition().y;
            boolean isTNT = this.world.getBlockState(pos).getBlock() instanceof BlockTNT;
            boolean hasTileEntity = this.world.getTileEntity(pos) != null;
            if (aboveExplosion && !isTNT && !hasTileEntity) {
               double distanceX = (double)pos.getX() - event.getExplosion().getPosition().x;
               double distanceZ = (double)pos.getZ() - event.getExplosion().getPosition().z;
               double motionX = distanceX / Math.abs(distanceX) * 0.5D;
               double motionY = 0.5D;
               double motionZ = distanceZ / Math.abs(distanceZ) * 0.5D;
               this.explosionHandler.handleBlock(this, pos, motionX, 0.5D, motionZ);
               movedBlocks.add(pos);
            }
         }

         Iterator var19 = movedBlocks.iterator();

         while(var19.hasNext()) {
            pos = (BlockPos)var19.next();
            affectedBlocks.remove(pos);
         }

      }
   }

   public final World getWorld() {
      return this.world;
   }
}
