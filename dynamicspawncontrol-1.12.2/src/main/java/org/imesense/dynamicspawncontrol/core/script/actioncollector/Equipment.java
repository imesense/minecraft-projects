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
import org.imesense.dynamicspawncontrol.core.script.storage.datadescription.EntityAttributes;
import org.imesense.dynamicspawncontrol.core.script.storage.datadescription.EntityDescription;
import org.imesense.dynamicspawncontrol.core.script.storage.datadescription.EntityEquipment;
import org.imesense.dynamicspawncontrol.core.script.storage.datadescription.ItemDescription;

import java.util.*;

public final class Equipment
{
    private static volatile Equipment _INSTANCE;

    public static Equipment getInstance()
    {
        if (_INSTANCE == null)
        {
            synchronized (Equipment.class)
            {
                if (_INSTANCE == null)
                {
                    _INSTANCE = new Equipment();
                }
            }
        }

        return _INSTANCE;
    }

    private void equipEntityWithItems(EntityLivingBase entityLivingBase,
                                      List<?> listItem, EntityEquipmentSlot entityEquipmentSlot, Random random)
    {
        if (listItem != null && !listItem.isEmpty())
        {
            Object item = listItem.get(random.nextInt(listItem.size()));
            ItemStack itemStack;

            if (item instanceof ItemDescription.Data)
            {
                ItemDescription.Data itemData = (ItemDescription.Data) item;
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
                entityLivingBase.setItemStackToSlot(entityEquipmentSlot, itemStack);
            }
            else
            {
                Log.writeDataToLogFile(1, "Item not found: " + item);
                throw new RuntimeException("Item not found: " + item);
            }
        }
    }

    public void equipEntity(Entity entity, EntityEquipment.Data entityEquipmentData,
                            EntityDescription.Data entityDescriptionData,
                            EntityAttributes.Data entityAttributesData, Random random)
    {
        if (entity instanceof EntityLivingBase)
        {
            EntityLivingBase livingEntity = (EntityLivingBase) entity;

            if (entityAttributesData.commandNbt != null)
            {
                CommandNBT.getInstance().applyNbt(livingEntity, entityAttributesData.commandNbt);
            }

            if (!entityDescriptionData.isArcher)
            {
                equipEntityWithItems(livingEntity, entityEquipmentData.heldItem, EntityEquipmentSlot.MAINHAND, random);
            }

            equipEntityWithItems(livingEntity, entityEquipmentData.helmet, EntityEquipmentSlot.HEAD, random);
            equipEntityWithItems(livingEntity, entityEquipmentData.chestPlate, EntityEquipmentSlot.CHEST, random);
            equipEntityWithItems(livingEntity, entityEquipmentData.legging, EntityEquipmentSlot.LEGS, random);
            equipEntityWithItems(livingEntity, entityEquipmentData.boots, EntityEquipmentSlot.FEET, random);

            if (entityEquipmentData.hasShield)
            {
                equipEntityWithItems(livingEntity, Collections.singletonList("minecraft:shield"), EntityEquipmentSlot.OFFHAND, random);
            }

            if (entityAttributesData.potion != null)
            {
                Potion.getInstance().applyPotionEffects(livingEntity, entityAttributesData.potion, random);
            }
        }
    }

    public List<ItemDescription.Data> parseItemList(JsonElement element)
    {
        List<ItemDescription.Data> items = new ArrayList<>();

        if (element.isJsonArray())
        {
            JsonArray itemArray = element.getAsJsonArray();

            for (JsonElement itemElement : itemArray)
            {
                ItemDescription.Data itemData = new ItemDescription.Data();

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
