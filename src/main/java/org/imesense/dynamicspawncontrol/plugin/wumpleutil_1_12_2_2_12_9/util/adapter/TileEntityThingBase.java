package org.imesense.dynamicspawncontrol.plugin.wumpleutil_1_12_2_2_12_9.util.adapter;

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
