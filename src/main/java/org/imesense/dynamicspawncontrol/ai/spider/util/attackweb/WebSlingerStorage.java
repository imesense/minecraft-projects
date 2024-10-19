package org.imesense.dynamicspawncontrol.ai.spider.util.attackweb;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

/**
 *
 */
public final class WebSlingerStorage implements IStorage<IWebSlinger>
{
    /**
     *
     */
    public WebSlingerStorage()
    {
        CodeGenericUtils.printInitClassToLog(this.getClass());
    }

    /**
     *
     * @param capability
     * @param instance
     * @param side
     * @return
     */
    public NBTBase writeNBT(Capability<IWebSlinger> capability, IWebSlinger instance, EnumFacing side)
    {
        return new NBTTagCompound();
    }

    /**
     *
     * @param capability
     * @param instance
     * @param side
     * @param nbt
     */
    public void readNBT(Capability<IWebSlinger> capability, IWebSlinger instance, EnumFacing side, NBTBase nbt)
    {
        // Implementation is not required
    }
}

