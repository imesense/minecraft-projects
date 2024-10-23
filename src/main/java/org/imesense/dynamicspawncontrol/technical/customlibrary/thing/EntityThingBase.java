package org.imesense.dynamicspawncontrol.technical.customlibrary.thing;

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
   public Entity owner;

   /**
    *
    * @param ownerIn
    */
   public EntityThingBase(Entity ownerIn)
   {
      this.owner = ownerIn;
   }

   /**
    *
    * @return
    */
   public ICapabilityProvider capProvider()
   {
      return this.owner;
   }
}
