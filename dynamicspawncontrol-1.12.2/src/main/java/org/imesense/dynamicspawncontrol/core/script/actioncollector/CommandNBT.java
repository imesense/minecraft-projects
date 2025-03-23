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
import org.imesense.dynamicspawncontrol.core.logfile.Log;
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
                Log.writeDataToLogFile(2, "Bad NBT for mob: " + exception.getMessage());
            }
        }
    }

    public NBTTagCompound createEnchantmentNbt(JsonObject nbtObject, Random random)
    {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();

        if (nbtObject.has("ench"))
        {
            JsonArray enchantmentsArray = nbtObject.getAsJsonArray("ench");
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

                NBTTagCompound enchantmentsTag = new NBTTagCompound();

                enchantmentsTag.setShort("id", (short) id);
                enchantmentsTag.setShort("lvl", (short) level);
                enchantmentsList.appendTag(enchantmentsTag);
            }

            nbtTagCompound.setTag("ench", enchantmentsList);
        }

        return nbtTagCompound;
    }
}
