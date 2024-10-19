package org.imesense.dynamicspawncontrol.technical.customlibrary.thing;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;

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
