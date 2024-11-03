package org.imesense.dynamicspawncontrol.plugin.wumpleutil_1_12_2_2_12_9.util.container;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

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
     * @param enumFacing
     * @param handler
     */
    public SimpleCapabilityProvider(Capability<HANDLER> capability, @Nullable EnumFacing enumFacing, HANDLER handler)
    {
        this.CAPABILITY = capability;
        this.INSTANCE = handler;
        this.FACING = enumFacing;
    }

    /**
     *
     * @param capability
     * @param enumFacing
     * @return
     */
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing enumFacing)
    {
        return capability == this.getCapability();
    }

    /**
     *
     * @param capability
     * @param enumFacing
     * @return
     * @param <T>
     */
    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing enumFacing)
    {
        return this.hasCapability(capability, enumFacing) ?
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
     * @param nbtBase
     */
    public void deserializeNBT(NBTBase nbtBase)
    {
        if (this.getCapability() != null)
        {
            this.getCapability().readNBT(this.getInstance(), this.getFacing(), nbtBase);
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
