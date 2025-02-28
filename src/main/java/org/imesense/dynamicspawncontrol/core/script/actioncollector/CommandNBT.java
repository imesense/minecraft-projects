package org.imesense.dynamicspawncontrol.core.script.actioncollector;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

import java.util.Random;

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

    public NBTTagCompound createEnchantmentNbt(JsonObject nbtObject, Random random)
    {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();

        if (nbtObject.has("ench"))
        {
            JsonArray enchArray = nbtObject.getAsJsonArray("ench");
            NBTTagList enchList = new NBTTagList();

            for (JsonElement enchElement : enchArray)
            {
                JsonObject enchObject = enchElement.getAsJsonObject();
                int id = enchObject.get("id").getAsInt();
                String lvl = enchObject.get("lvl").getAsString();

                if (enchObject.has("chance"))
                {
                    int chance = enchObject.get("chance").getAsInt();

                    if (random.nextInt(100) >= chance)
                    {
                        continue;
                    }
                }

                int level;
                if (lvl.contains(":"))
                {
                    String[] range = lvl.split(":");
                    int min = Integer.parseInt(range[0]);
                    int max = Integer.parseInt(range[1]);
                    level = random.nextInt(max - min + 1) + min;
                }
                else
                {
                    level = Integer.parseInt(lvl);
                }

                NBTTagCompound enchTag = new NBTTagCompound();
                enchTag.setShort("id", (short) id);
                enchTag.setShort("lvl", (short) level);
                enchList.appendTag(enchTag);
            }

            nbtTagCompound.setTag("ench", enchList);
        }

        return nbtTagCompound;
    }
}
