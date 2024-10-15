package org.imesense.dynamicspawncontrol.technical.customlibrary.thing;

//import com.wumple.util.base.misc.Util;
//import com.wumple.util.capability.CapabilityUtils;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;

public interface IThingBase extends ICapabilityProvider
{
   World getWorld();

   BlockPos getPos();

   boolean isInvalid();

   void markDirty();

   void invalidate();

   boolean sameAs(IThing var1);

   Object object();

   default World getWorldBackup(World otherWorld)
   {
      World myWorld = this.getWorld();
      return myWorld != null ? myWorld : otherWorld;
   }

   default int getCount()
   {
      return 1;
   }

   default <T> T as(Class<T> t)
   {
      return CodeGenericUtils.as(this.object(), t);
   }

   default <T> boolean is(Class<T> t)
   {
      return t.isInstance(this.object());
   }

   ICapabilityProvider capProvider();

   default boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
   {
      ICapabilityProvider provider = this.capProvider();
      return provider != null ? provider.hasCapability(capability, facing) : false;
   }

   @Nullable
   default <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
   {
      ICapabilityProvider provider = this.capProvider();
      return provider != null ? provider.getCapability(capability, facing) : null;
   }

   @Nullable
   default <T> T fetchCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
   {
      ICapabilityProvider provider = this.capProvider();
      return CodeGenericUtils.fetchCapability(provider, capability, facing);
   }

   void forceUpdate();
}
