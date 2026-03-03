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
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.logfile.Logger;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.Random;

@InitLog
public final class CommandNBT
{
    private static volatile CommandNBT _INSTANCE;

    public static CommandNBT getInstance()
    {
        return CodeGeneric.getInstance(CommandNBT.class);
    }

    public CommandNBT()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
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
                Logger.write(2, "Bad NBT for mob: " + exception.getMessage());
            }
        }
    }

    public NBTTagCompound createEnchantmentNbt(JsonObject jsonObject, Random random)
    {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();

        if (jsonObject.has("ench"))
        {
            JsonArray enchantmentsArray = jsonObject.getAsJsonArray("ench");
            NBTTagList enchantmentsList = new NBTTagList();

            for (JsonElement enchantmentsElement : enchantmentsArray)
            {
                JsonObject enchantmentsObject = enchantmentsElement.getAsJsonObject();
                int id = enchantmentsObject.get("id").getAsInt();
                String lvl = enchantmentsObject.get("lvl").getAsString();

                if (enchantmentsObject.has("chance"))
                {
                    int chance = enchantmentsObject.get("chance").getAsInt();

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

                NBTTagCompound nbtTagCompound1 = new NBTTagCompound();

                nbtTagCompound1.setShort("id", (short) id);
                nbtTagCompound1.setShort("lvl", (short) level);
                enchantmentsList.appendTag(nbtTagCompound1);
            }

            nbtTagCompound.setTag("ench", enchantmentsList);
        }

        return nbtTagCompound;
    }
}
