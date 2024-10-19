package org.imesense.dynamicspawncontrol.technical.customlibrary.thing;

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
    * @param facing
    * @return
    */
   default boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
   {
      ICapabilityProvider provider = this.capProvider();
      return provider != null && provider.hasCapability(capability, facing);
   }

   /**
    *
    * @param capability
    * @param facing
    * @return
    * @param <T>
    */
   @Nullable
   default <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
   {
      ICapabilityProvider provider = this.capProvider();
      return provider != null ? provider.getCapability(capability, facing) : null;
   }
}
