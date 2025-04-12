package org.imesense.dynamicspawncontrol.core.plugin.mod.lilliputian_1_0.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public final class SizeCapabilityStorage implements Capability.IStorage<ISizeCapability>
{
    public SizeCapabilityStorage()
    {

    }

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<ISizeCapability> iSizeCapabilityCapability, ISizeCapability iSizeCapability, EnumFacing enumFacing)
    {
        return null;
    }

    @Override
    public void readNBT(Capability<ISizeCapability> iSizeCapabilityCapability, ISizeCapability iSizeCapability, EnumFacing enumFacing, NBTBase nbtBase)
    {

    }
}
