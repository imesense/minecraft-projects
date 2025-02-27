package org.imesense.dynamicspawncontrol.eventprocessor;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.parser.algo.GeneralStorageData;

import java.util.*;

/**
 *
 */
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventEntityJoinWorld
{
    /**
     *
     */
    private static boolean instanceExists = false;

    public OnEventEntityJoinWorld()
    {
        CodeGeneric.printInitClassToLog(this.getClass());

        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent
    public void onZombieSpecialSpawn(EntityJoinWorldEvent event)
    {
        if (!(event.getEntity() instanceof EntityZombie))
        {
            return;
        }

        EntityZombie entityZombie = (EntityZombie) event.getEntity();

        GeneralStorageData generalStorageData = GeneralStorageData.Instance;
        if (generalStorageData != null)
        {
            List<GeneralStorageData.Equipment> configs = generalStorageData.getEquipmentConfigs();

            if (configs != null && !configs.isEmpty())
            {
                GeneralStorageData.Equipment selectedConfig = getConfigByPriority(configs, UniqueField.RANDOM.self());

                equipZombie(entityZombie, selectedConfig.HeldItems, EntityEquipmentSlot.MAINHAND, UniqueField.RANDOM.self());
                equipZombie(entityZombie, selectedConfig.Helmets, EntityEquipmentSlot.HEAD, UniqueField.RANDOM.self());
                equipZombie(entityZombie, selectedConfig.ChestPlates, EntityEquipmentSlot.CHEST, UniqueField.RANDOM.self());
                equipZombie(entityZombie, selectedConfig.Leggings, EntityEquipmentSlot.LEGS, UniqueField.RANDOM.self());
                equipZombie(entityZombie, selectedConfig.Boots, EntityEquipmentSlot.FEET, UniqueField.RANDOM.self());

                if (selectedConfig.HasShield)
                {
                    equipZombie(entityZombie, Collections.singletonList("minecraft:shield"), EntityEquipmentSlot.OFFHAND, UniqueField.RANDOM.self());
                }
            }
        }
    }

    /**
     *
     * @param entityZombie
     * @param items
     * @param equipmentSlot
     * @param random
     */
    private void equipZombie(EntityZombie entityZombie, List<String> items, EntityEquipmentSlot equipmentSlot, Random random)
    {
        if (items != null && !items.isEmpty())
        {
            String item = items.get(random.nextInt(items.size()));
            ItemStack itemStack = new ItemStack(Objects.requireNonNull(Item.getByNameOrId(item)));

            if (itemStack.getItem() != Items.AIR)
            {
                entityZombie.setItemStackToSlot(equipmentSlot, itemStack);
            }
            else
            {
                Log.writeDataToLogFile(1, "Item not found: " + item);
                throw new RuntimeException("Item not found: " + item);
            }
        }
    }

    /**
     *
     * @param equipmentList
     * @param random
     * @return
     */
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
}