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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.ProjectStructure;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.parser.GeneralStorageData;

import java.util.*;

/**
 *
 */
@Mod.EventBusSubscriber(modid = ProjectStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnSingleZombieSummonAID
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    private final Set<UUID> spawnedZombies = new HashSet<>();

    /**
     *
     */
    private final Set<UUID> processedZombies = new HashSet<>();

    /**
     *
     */
    public OnSingleZombieSummonAID()
    {
		CodeGenericUtil.printInitClassToLog(this.getClass());
		
        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;
    }

    /**
     *
     * @param world
     * @param blockPos
     * @return
     */
    public boolean isPositionValid(World world, BlockPos blockPos)
    {
        IBlockState iBlockState = world.getBlockState(blockPos);

        return iBlockState.getBlock().isAir(iBlockState, world, blockPos) &&
                world.getBlockState(blockPos.down()).getBlock().isFullBlock(iBlockState);
    }

    /**
     *
     * @param world
     * @param blockPos
     * @param maxAttempts
     * @return
     */
    public BlockPos findValidSpawnPosition(World world, BlockPos blockPos, int maxAttempts)
    {
        Random random = new Random();

        for (int i = 0; i < maxAttempts; i++)
        {
            BlockPos blockPos1 =
                    blockPos.add((random.nextDouble() - 0.50) * 25.00, 0, (random.nextDouble() - 0.50) * 25.00);

            if (isPositionValid(world, blockPos1))
            {
                return blockPos1;
            }
        }

        return blockPos;
    }

    /**
     *
     * @param world
     * @param blockPos
     * @param width
     * @param height
     * @return
     */
    public boolean hasSufficientSpace(World world, BlockPos blockPos, int width, int height)
    {
        for (int x = -width / 2; x <= width / 2; x++)
        {
            for (int z = -width / 2; z <= width / 2; z++)
            {
                for (int y = 0; y < height; y++)
                {
                    BlockPos checkPos = blockPos.add(x, y, z);

                    if (!world.getBlockState(checkPos).getBlock().isAir(world.getBlockState(checkPos), world, checkPos))
                    {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     *
     * @param livingAttackEvent
     */
    @SubscribeEvent
    public synchronized void onZombieAttack_0(LivingAttackEvent livingAttackEvent)
    {
        if (livingAttackEvent.getEntityLiving() instanceof EntityZombie)
        {
            EntityZombie entityZombie = (EntityZombie) livingAttackEvent.getEntityLiving();

            World world = entityZombie.world;

            if (world.isRemote)
            {
                return;
            }

            EntityLivingBase entityLivingBase = null;

            if (livingAttackEvent.getSource().getTrueSource() instanceof EntityLivingBase)
            {
                entityLivingBase = (EntityLivingBase) livingAttackEvent.getSource().getTrueSource();
            }

            if (entityLivingBase == null)
            {
                return;
            }

            if (spawnedZombies.contains(livingAttackEvent.getEntity().getUniqueID()))
            {
                return;
            }

            if (processedZombies.contains(livingAttackEvent.getEntity().getUniqueID()))
            {
                return;
            }

            Random random = new Random();

            BlockPos blockPos =
                    entityZombie.getPosition().add((random.nextDouble() - 0.50) * 5.00, 0.00, (random.nextDouble() - 0.50) * 5.00);

            blockPos = findValidSpawnPosition(world, blockPos, 10);

            if (hasSufficientSpace(world, blockPos, 2, 3))
            {
                EntityZombie entityZombie1 = new EntityZombie(world);
                entityZombie1.setPosition(blockPos.getX(), blockPos.getY(), blockPos.getZ());

                GeneralStorageData generalStorageData = GeneralStorageData.Instance;

                if (generalStorageData != null)
                {
                    List<GeneralStorageData.Equipment> configs = generalStorageData.getEquipmentConfigs();

                    if (configs != null && !configs.isEmpty())
                    {
                        GeneralStorageData.Equipment selectedConfig = getConfigByPriority(configs, random);

                        equipZombie(entityZombie1, selectedConfig.HeldItems, EntityEquipmentSlot.MAINHAND, random);
                        equipZombie(entityZombie1, selectedConfig.Helmets, EntityEquipmentSlot.HEAD, random);
                        equipZombie(entityZombie1, selectedConfig.ChestPlates, EntityEquipmentSlot.CHEST, random);
                        equipZombie(entityZombie1, selectedConfig.Leggings, EntityEquipmentSlot.LEGS, random);
                        equipZombie(entityZombie1, selectedConfig.Boots, EntityEquipmentSlot.FEET, random);
                    }
                }

                processedZombies.add(livingAttackEvent.getEntity().getUniqueID());

                world.spawnEntity(entityZombie1);

                spawnedZombies.add(entityZombie1.getUniqueID());

                entityZombie1.setAttackTarget(entityLivingBase);
            }
        }
    }

    /**
     *
     * @param entityZombie
     * @param items
     * @param entityEquipmentSlot
     * @param random
     */
    private void equipZombie(EntityZombie entityZombie, List<String> items, EntityEquipmentSlot entityEquipmentSlot, Random random)
    {
        if (items != null && !items.isEmpty())
        {
            String item = items.get(random.nextInt(items.size()));
            ItemStack itemStack = new ItemStack(Objects.requireNonNull(Item.getByNameOrId(item)));

            if (itemStack.getItem() != Items.AIR)
            {
                entityZombie.setItemStackToSlot(entityEquipmentSlot, itemStack);
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
