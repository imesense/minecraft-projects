package org.imesense.dynamicspawncontrol.technical.customlibrary.thing;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class EntityThingBase implements IThingBase
{
   public Entity owner;

   public EntityThingBase(Entity ownerIn)
   {
      this.owner = ownerIn;
   }

   public World getWorld()
   {
      return this.owner != null ? this.owner.getEntityWorld() : null;
   }

   public BlockPos getPos()
   {
      return this.owner != null ? this.owner.getPosition() : null;
   }

   public boolean isInvalid()
   {
      return this.owner == null || this.owner.isDead;
   }

   public void markDirty()
   {

   }

   public void invalidate()
   {
      if (this.owner != null)
      {
         this.owner.setDead();
      }

      this.owner = null;
   }

   public boolean sameAs(IThing entity)
   {
      if (entity instanceof EntityThingBase)
      {
         return this.owner == ((EntityThingBase)entity).owner;
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
         this.owner.setPositionAndUpdate((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
      }
   }
}
