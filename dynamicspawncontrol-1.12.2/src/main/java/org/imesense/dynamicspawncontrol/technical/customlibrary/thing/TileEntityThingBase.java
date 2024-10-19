package org.imesense.dynamicspawncontrol.technical.customlibrary.thing;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;

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
