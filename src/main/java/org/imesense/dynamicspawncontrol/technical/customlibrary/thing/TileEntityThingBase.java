package org.imesense.dynamicspawncontrol.technical.customlibrary.thing;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class TileEntityThingBase implements IThingBase {
   public TileEntity owner = null;

   public TileEntityThingBase(TileEntity ownerIn)
   {
      this.owner = ownerIn;
   }

   public World getWorld()
   {
      return this.owner != null ? this.owner.getWorld() : null;
   }

   public BlockPos getPos()
   {
      return this.owner != null ? this.owner.getPos() : null;
   }

   public boolean isInvalid()
   {
      return this.owner == null || this.owner.isInvalid();
   }

   public void markDirty()
   {
      if (this.owner != null)
      {
         this.owner.markDirty();
      }

   }

   public void invalidate()
   {
      if (this.owner != null)
      {
         World world = this.getWorld();
         BlockPos pos = this.getPos();

         if (world != null && pos != null)
         {
            world.setBlockToAir(pos);
            world.removeTileEntity(pos);
         }

         this.owner.invalidate();
         this.owner.updateContainingBlockInfo();
      }

      this.owner = null;
   }

   public boolean sameAs(IThing entity)
   {
      if (entity instanceof TileEntityThingBase)
      {
         return this.owner == ((TileEntityThingBase)entity).owner;
      }
      else
      {
         return false;
      }
   }

   public Object object()
   {
      return this.owner;
   }

   public ICapabilityProvider capProvider()
   {
      return this.owner;
   }

   public void forceUpdate()
   {
      if (this.owner != null)
      {
         BlockPos pos = this.getPos();
         World world = this.getWorld();

         if (world != null && pos != null)
         {
            IBlockState state = world.getBlockState(pos);
            world.markBlockRangeForRenderUpdate(pos, pos);
            world.notifyBlockUpdate(pos, state, state, 3);
            world.scheduleBlockUpdate(pos, this.owner.getBlockType(), 0, 0);
         }

         this.owner.markDirty();
      }
   }
}
