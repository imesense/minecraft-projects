package org.imesense.dynamicspawncontrol.technical.customlibrary;

import javax.annotation.Nullable;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class SimpleCapabilityProvider<HANDLER> implements ICapabilitySerializable<NBTBase>
{
    private final Capability<HANDLER> capability;
    private final EnumFacing facing;
    private final HANDLER instance;

    public SimpleCapabilityProvider()
    {
        this.capability = null;
        this.facing = null;
        this.instance = null;
    }

    public SimpleCapabilityProvider(Capability<HANDLER> capability, @Nullable EnumFacing facing)
    {
        this(capability, facing, capability != null ? capability.getDefaultInstance() : null);
    }

    public SimpleCapabilityProvider(Capability<HANDLER> capability, @Nullable EnumFacing facing, HANDLER instance)
    {
        this.capability = capability;
        this.instance = instance;
        this.facing = facing;
    }

    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability != null && capability == this.getCapability();
    }

    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        return this.hasCapability(capability, facing) ? this.getCapability().cast(this.getInstance()) : null;
    }

    public NBTBase serializeNBT()
    {
        return (NBTBase)(this.getCapability() == null ? new NBTTagCompound() : this.getCapability().writeNBT(this.getInstance(), this.getFacing()));
    }

    public void deserializeNBT(NBTBase nbt)
    {
        if (this.getCapability() != null)
        {
            this.getCapability().readNBT(this.getInstance(), this.getFacing(), nbt);
        }
    }

    public final Capability<HANDLER> getCapability()
    {
        return this.capability;
    }

    @Nullable
    public EnumFacing getFacing()
    {
        return this.facing;
    }

    public HANDLER getInstance()
    {
        return this.instance;
    }
}
