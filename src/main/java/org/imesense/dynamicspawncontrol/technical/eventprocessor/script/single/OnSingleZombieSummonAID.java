package org.imesense.dynamicspawncontrol.technical.eventprocessor.script.single;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.technical.parser.GeneralStorageData;

import java.util.*;

/**
 *
 */
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnSingleZombieSummonAID
{
    private static boolean instanceExists = false;
    private final Set<UUID> processedZombies = new HashSet<>();

    public OnSingleZombieSummonAID()
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
    public synchronized void onZombieSpecialSpawn(LivingSpawnEvent.SpecialSpawn event)
    {
        if (!(event.getEntityLiving() instanceof EntityZombie)) {
            return;
        }

        EntityZombie entityZombie = (EntityZombie) event.getEntityLiving();
        World world = entityZombie.world;

        // Игнорируем, если зомби уже обработан
        UUID zombieID = entityZombie.getUniqueID();
        if (processedZombies.contains(zombieID)) {
            return;
        }

        Random random = new Random();

        GeneralStorageData generalStorageData = GeneralStorageData.Instance;
        if (generalStorageData != null) {
            List<GeneralStorageData.Equipment> configs = generalStorageData.getEquipmentConfigs();
            if (configs != null && !configs.isEmpty()) {
                GeneralStorageData.Equipment selectedConfig = getConfigByPriority(configs, random);

                equipZombie(entityZombie, selectedConfig.HeldItems, EntityEquipmentSlot.MAINHAND, random);
                equipZombie(entityZombie, selectedConfig.Helmets, EntityEquipmentSlot.HEAD, random);
                equipZombie(entityZombie, selectedConfig.ChestPlates, EntityEquipmentSlot.CHEST, random);
                equipZombie(entityZombie, selectedConfig.Leggings, EntityEquipmentSlot.LEGS, random);
                equipZombie(entityZombie, selectedConfig.Boots, EntityEquipmentSlot.FEET, random);
            }
        }

        processedZombies.add(zombieID);
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
        if (items != null && !items.isEmpty()) {
            String item = items.get(random.nextInt(items.size()));
            ItemStack itemStack = new ItemStack(Objects.requireNonNull(Item.getByNameOrId(item)));

            if (itemStack.getItem() != Items.AIR) {
                entityZombie.setItemStackToSlot(equipmentSlot, itemStack);
            } else {
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

        for (GeneralStorageData.Equipment config : equipmentList) {
            cumulativePriority += config.Priority;

            if (randomValue < cumulativePriority) {
                return config;
            }
        }

        return equipmentList.get(equipmentList.size() - 1);
    }
}


