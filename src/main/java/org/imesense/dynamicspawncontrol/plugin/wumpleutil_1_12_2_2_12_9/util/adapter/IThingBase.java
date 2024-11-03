package org.imesense.dynamicspawncontrol.plugin.wumpleutil_1_12_2_2_12_9.util.adapter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 *
 */
public interface IThingBase extends ICapabilityProvider
{
   /**
    *
    * @return
    */
   ICapabilityProvider capProvider();

   /**
    *
    * @param capability
    * @param enumFacing
    * @return
    */
   default boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing enumFacing)
   {
      ICapabilityProvider iCapabilityProvider = this.capProvider();
      return iCapabilityProvider != null && iCapabilityProvider.hasCapability(capability, enumFacing);
   }

   /**
    *
    * @param capability
    * @param enumFacing
    * @return
    * @param <T>
    */
   @Nullable
   default <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing enumFacing)
   {
      ICapabilityProvider iCapabilityProvider = this.capProvider();
      return iCapabilityProvider != null ? iCapabilityProvider.getCapability(capability, enumFacing) : null;
   }
}
