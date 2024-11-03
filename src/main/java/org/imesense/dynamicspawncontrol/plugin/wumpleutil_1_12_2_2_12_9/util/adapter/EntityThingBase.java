package org.imesense.dynamicspawncontrol.plugin.wumpleutil_1_12_2_2_12_9.util.adapter;

import net.minecraft.entity.Entity;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 *
 */
public class EntityThingBase implements IThingBase
{
   /**
    *
    */
   public Entity Owner;

   /**
    *
    * @param ownerIn
    */
   public EntityThingBase(Entity ownerIn)
   {
      this.Owner = ownerIn;
   }

   /**
    *
    * @return
    */
   public ICapabilityProvider capProvider()
   {
      return this.Owner;
   }
}
