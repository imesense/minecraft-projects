package org.imesense.dynamicspawncontrol.core.script.actioncollector;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

public final class CommandNBT
{
    private static volatile CommandNBT _INSTANCE;

    public static CommandNBT getInstance()
    {
        if (_INSTANCE == null)
        {
            synchronized (CommandNBT.class)
            {
                if (_INSTANCE == null)
                {
                    _INSTANCE = new CommandNBT();
                }
            }
        }

        return _INSTANCE;
    }

    public void applyNbt(Entity entity, String nbtString)
    {
        if (nbtString != null && !nbtString.isEmpty() && entity instanceof EntityLivingBase)
        {
            try
            {
                NBTTagCompound nbtTagCompound = JsonToNBT.getTagFromJson(nbtString);
                ((EntityLivingBase) entity).readEntityFromNBT(nbtTagCompound);
            }
            catch (NBTException exception)
            {
                Log.writeDataToLogFile(2, "Bad NBT for mob: " + exception.getMessage());
            }
        }
    }
}
