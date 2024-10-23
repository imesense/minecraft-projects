package org.imesense.dynamicspawncontrol.technical.customlibrary.thing;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 *
 */
public class TileEntityThingBase implements IThingBase
{
   /**
    *
    */
   public final TileEntity OWNER;

   /**
    *
    * @param ownerIn
    */
   public TileEntityThingBase(TileEntity ownerIn)
   {
      this.OWNER = ownerIn;
   }

   /**
    *
    * @return
    */
   public ICapabilityProvider capProvider()
   {
      return this.OWNER;
   }
}
