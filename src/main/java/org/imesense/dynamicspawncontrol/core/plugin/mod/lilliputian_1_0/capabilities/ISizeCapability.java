package org.imesense.dynamicspawncontrol.core.plugin.mod.lilliputian_1_0.capabilities;

import net.minecraft.nbt.NBTTagCompound;

public interface ISizeCapability
{
    float getScale();

    void setScale(float scale);

    NBTTagCompound saveNBT();

    void loadNBT(NBTTagCompound nbtTagCompound);
}
