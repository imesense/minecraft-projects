package org.imesense.dynamicspawncontrol.ai.spider.utils.attackweb;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class WebSlingerStorage implements IStorage<IWebSlinger>
{
    public NBTBase writeNBT(Capability<IWebSlinger> capability, IWebSlinger instance, EnumFacing side)
    {
        NBTTagCompound tags = new NBTTagCompound();

        if (instance != null)
        {

        }

        return tags;
    }

    public void readNBT(Capability<IWebSlinger> capability, IWebSlinger instance, EnumFacing side, NBTBase nbt)
    {
        NBTTagCompound tags = (NBTTagCompound)nbt;

        if (tags != null && instance != null)
        {

        }
    }
}

