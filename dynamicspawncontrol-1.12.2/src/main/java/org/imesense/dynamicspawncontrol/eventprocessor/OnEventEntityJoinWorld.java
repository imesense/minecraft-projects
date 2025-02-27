package org.imesense.dynamicspawncontrol.eventprocessor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.parser.algo.GeneralStorageData;

import java.util.*;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventEntityJoinWorld
{
    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        String entityType = EntityList.getEntityString(event.getEntity());

        if (entityType == null)
        {
            Log.writeDataToLogFile(0, "entityType is null");
            return;
        }

        String fullEntityType = entityType.contains(":") ? entityType : "minecraft:" + entityType.toLowerCase();

        GeneralStorageData generalStorageData = GeneralStorageData.Instance;

        if (generalStorageData != null)
        {
            List<GeneralStorageData.Equipment> configs = generalStorageData.getEquipmentConfigs();

            if (configs != null && !configs.isEmpty())
            {
                List<GeneralStorageData.Equipment> filteredConfigs = configs.stream()
                        .filter(config -> config.entityType.equals(fullEntityType))
                        .collect(Collectors.toList());

                if (!filteredConfigs.isEmpty())
                {
                    GeneralStorageData.Equipment selectedConfig = getConfigByPriority(filteredConfigs, UniqueField.RANDOM.self());
                    equipEntity(event.getEntity(), selectedConfig, UniqueField.RANDOM.self());
                }
            }
        }
    }

    private void equipEntity(Entity entity, GeneralStorageData.Equipment config, Random random)
    {
        if (entity instanceof EntityLivingBase)
        {
            EntityLivingBase livingEntity = (EntityLivingBase) entity;

            boolean isArcher = false;

            if (!isArcher)
            {
                equipEntityWithItems(livingEntity, config.HeldItems, EntityEquipmentSlot.MAINHAND, random);
            }

            equipEntityWithItems(livingEntity, config.Helmets, EntityEquipmentSlot.HEAD, random);
            equipEntityWithItems(livingEntity, config.ChestPlates, EntityEquipmentSlot.CHEST, random);
            equipEntityWithItems(livingEntity, config.Leggings, EntityEquipmentSlot.LEGS, random);
            equipEntityWithItems(livingEntity, config.Boots, EntityEquipmentSlot.FEET, random);

            if (config.HasShield)
            {
                if (config.HeldItems != null && !config.HeldItems.isEmpty())
                {
                    equipEntityWithItems(livingEntity, Collections.singletonList("minecraft:shield"), EntityEquipmentSlot.OFFHAND, random);
                }
            }

            List<GeneralStorageData.PotionEffectWithChance> potions = GeneralStorageData.Instance.getPotions();

            if (potions != null)
            {
                applyPotionEffects(livingEntity, potions, random);
            }
        }
    }

    private void equipEntityWithItems(EntityLivingBase entity, List<String> items, EntityEquipmentSlot equipmentSlot, Random random)
    {
        if (items != null && !items.isEmpty())
        {
            String item = items.get(random.nextInt(items.size()));
            ItemStack itemStack = new ItemStack(Objects.requireNonNull(Item.getByNameOrId(item)));

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

    private GeneralStorageData.Equipment getConfigByPriority(List<GeneralStorageData.Equipment> equipmentList, Random random)
    {
        int totalPriority = equipmentList.stream().mapToInt(config -> config.Priority).sum();
        int randomValue = random.nextInt(totalPriority);

        int cumulativePriority = 0;

        for (GeneralStorageData.Equipment config : equipmentList)
        {
            cumulativePriority += config.Priority;

            if (randomValue < cumulativePriority)
            {
                return config;
            }
        }

        return equipmentList.get(equipmentList.size() - 1);
    }

    private void applyPotionEffects(EntityLivingBase entity, List<GeneralStorageData.PotionEffectWithChance> potions, Random random)
    {
        if (potions != null && !potions.isEmpty())
        {
            for (GeneralStorageData.PotionEffectWithChance effectWithChance : potions)
            {
                if (random.nextDouble() <= effectWithChance.Chance)
                {
                    PotionEffect effect = effectWithChance.Effect;
                    PotionEffect newEffect = new PotionEffect(effect.getPotion(), effect.getDuration(), effect.getAmplifier());
                    entity.addPotionEffect(newEffect);
                }
            }
        }
    }
}