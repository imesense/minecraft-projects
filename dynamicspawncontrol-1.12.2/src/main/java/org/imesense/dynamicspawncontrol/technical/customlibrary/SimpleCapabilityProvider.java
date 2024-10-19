package org.imesense.dynamicspawncontrol.technical.customlibrary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;

/**
 *
 * @param <HANDLER>
 */
public class SimpleCapabilityProvider<HANDLER> implements ICapabilitySerializable<NBTBase>
{
    /**
     *
     */
    private final HANDLER INSTANCE;

    /**
     *
     */
    private final EnumFacing FACING;

    /**
     *
     */
    private final Capability<HANDLER> CAPABILITY;

    /**
     *
     * @param capability
     * @param facing
     * @param instance
     */
    public SimpleCapabilityProvider(Capability<HANDLER> capability, @Nullable EnumFacing facing, HANDLER instance)
    {
        this.CAPABILITY = capability;
        this.INSTANCE = instance;
        this.FACING = facing;
    }

    /**
     *
     * @param capability
     * @param facing
     * @return
     */
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == this.getCapability();
    }

    /**
     *
     * @param capability
     * @param facing
     * @return
     * @param <T>
     */
    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
    {
        return this.hasCapability(capability, facing) ?
                this.getCapability().cast(this.getInstance()) : null;
    }

    /**
     *
     * @return
     */
    public NBTBase serializeNBT()
    {
        return (this.getCapability() == null ?
                new NBTTagCompound() : this.getCapability().writeNBT(this.getInstance(), this.getFacing()));
    }

    /**
     *
     * @param nbt
     */
    public void deserializeNBT(NBTBase nbt)
    {
        if (this.getCapability() != null)
        {
            this.getCapability().readNBT(this.getInstance(), this.getFacing(), nbt);
        }
    }

    /**
     *
     * @return
     */
    public final Capability<HANDLER> getCapability()
    {
        return this.CAPABILITY;
    }

    /**
     *
     * @return
     */
    @Nullable
    public EnumFacing getFacing()
    {
        return this.FACING;
    }

    /**
     *
     * @return
     */
    public HANDLER getInstance()
    {
        return this.INSTANCE;
    }
}
