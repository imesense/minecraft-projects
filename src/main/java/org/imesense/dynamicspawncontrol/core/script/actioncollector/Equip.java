package org.imesense.dynamicspawncontrol.core.script.actioncollector;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.script.storage.StoringScriptData;

import java.util.*;

public final class Equip
{
    private static volatile Equip _INSTANCE;

    public static Equip getInstance()
    {
        if (_INSTANCE == null)
        {
            synchronized (Equip.class)
            {
                if (_INSTANCE == null)
                {
                    _INSTANCE = new Equip();
                }
            }
        }

        return _INSTANCE;
    }

    private void equipEntityWithItems(EntityLivingBase entity, List<?> items, EntityEquipmentSlot equipmentSlot, Random random) {
        if (items != null && !items.isEmpty())
        {
            Object item = items.get(random.nextInt(items.size()));
            ItemStack itemStack;

            if (item instanceof StoringScriptData.ItemData)
            {
                StoringScriptData.ItemData itemData = (StoringScriptData.ItemData) item;
                itemStack = new ItemStack(Objects.requireNonNull(Item.getByNameOrId(itemData.item)));

                if (itemData.nbt != null)
                {
                    NBTTagCompound nbtTagCompound = CommandNBT.getInstance().createEnchantmentNbt(itemData.nbt, random);
                    itemStack.setTagCompound(nbtTagCompound);
                }
            }
            else if (item instanceof String)
            {
                itemStack = new ItemStack(Objects.requireNonNull(Item.getByNameOrId((String) item)));
            }
            else
            {
                throw new IllegalArgumentException("Unsupported item type: " + item.getClass());
            }

            if (itemStack.getItem() != Items.AIR)
            {
                entity.setItemStackToSlot(equipmentSlot, itemStack);
            }
            else
            {
                Log.writeDataToLogFile(1, "Item not found: " + item);
                throw new RuntimeException("Item not found: " + item);
            }
        }
    }

    public void equipEntity(Entity entity, StoringScriptData.Equipment config, Random random)
    {
        if (entity instanceof EntityLivingBase)
        {
            EntityLivingBase livingEntity = (EntityLivingBase) entity;

            if (config.commandNbt != null)
            {
                CommandNBT.getInstance().applyNbt(livingEntity, config.commandNbt);
            }

            if (!config.isArcher)
            {
                equipEntityWithItems(livingEntity, config.HeldItems, EntityEquipmentSlot.MAINHAND, random);
            }

            equipEntityWithItems(livingEntity, config.Helmets, EntityEquipmentSlot.HEAD, random);
            equipEntityWithItems(livingEntity, config.ChestPlates, EntityEquipmentSlot.CHEST, random);
            equipEntityWithItems(livingEntity, config.Leggings, EntityEquipmentSlot.LEGS, random);
            equipEntityWithItems(livingEntity, config.Boots, EntityEquipmentSlot.FEET, random);

            if (config.HasShield)
            {
                equipEntityWithItems(livingEntity, Collections.singletonList("minecraft:shield"), EntityEquipmentSlot.OFFHAND, random);
            }

            List<StoringScriptData.PotionEffectWithChance> potions = StoringScriptData.Instance.getPotions();
            if (potions != null)
            {
                Potion.getInstance().applyPotionEffects(livingEntity, potions, random);
            }
        }
    }

    public List<StoringScriptData.ItemData> parseItemList(JsonElement element)
    {
        List<StoringScriptData.ItemData> items = new ArrayList<>();

        if (element.isJsonArray())
        {
            JsonArray itemArray = element.getAsJsonArray();

            for (JsonElement itemElement : itemArray)
            {
                StoringScriptData.ItemData itemData = new StoringScriptData.ItemData();

                if (itemElement.isJsonObject())
                {
                    JsonObject itemObject = itemElement.getAsJsonObject();
                    itemData.item = itemObject.get("item").getAsString();

                    if (itemObject.has("nbt"))
                    {
                        itemData.nbt = itemObject.getAsJsonObject("nbt");
                    }
                }
                else if (itemElement.isJsonPrimitive())
                {
                    itemData.item = itemElement.getAsString();
                }

                items.add(itemData);
            }
        }

        return items;
    }
}
