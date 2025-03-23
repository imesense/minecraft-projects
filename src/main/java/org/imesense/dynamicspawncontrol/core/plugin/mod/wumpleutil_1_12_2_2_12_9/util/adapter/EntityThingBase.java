package org.imesense.dynamicspawncontrol.core.plugin.mod.wumpleutil_1_12_2_2_12_9.util.adapter;

import net.minecraft.entity.Entity;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public final class EntityThingBase implements IThingBase
{
   public Entity owner;

   public EntityThingBase(Entity entity)
   {
      this.owner = entity;
   }

   public ICapabilityProvider capProvider()
   {
      return this.owner;
   }
}
