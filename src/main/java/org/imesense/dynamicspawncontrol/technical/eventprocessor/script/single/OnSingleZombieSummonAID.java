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
     * @param pos
     * @return
     */
    public boolean isPositionValid(World world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock().isAir(state, world, pos) && world.getBlockState(pos.down()).getBlock().isFullBlock(state);
    }

    /**
     *
     * @param world
     * @param originalPos
     * @param maxAttempts
     * @return
     */
    public BlockPos findValidSpawnPosition(World world, BlockPos originalPos, int maxAttempts)
    {
        Random random = new Random();

        for (int i = 0; i < maxAttempts; i++)
        {
            BlockPos potentialPos = originalPos.add((random.nextDouble() - 0.5) * 25.0, 0, (random.nextDouble() - 0.5) * 25.0);
            if (isPositionValid(world, potentialPos))
            {
                return potentialPos;
            }
        }

        return originalPos;
    }

    /**
     *
     * @param world
     * @param pos
     * @param width
     * @param height
     * @return
     */
    public boolean hasSufficientSpace(World world, BlockPos pos, int width, int height)
    {
        for (int x = -width / 2; x <= width / 2; x++)
        {
            for (int z = -width / 2; z <= width / 2; z++)
            {
                for (int y = 0; y < height; y++)
                {
                    BlockPos checkPos = pos.add(x, y, z);

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
     * @param event
     */
    @SubscribeEvent
    public synchronized void onZombieAttack_0(LivingAttackEvent event)
    {
        if (event.getEntityLiving() instanceof EntityZombie)
        {
            EntityZombie zombie = (EntityZombie) event.getEntityLiving();

            EntityLivingBase attacker = null;
            if (event.getSource().getTrueSource() instanceof EntityLivingBase)
            {
                attacker = (EntityLivingBase) event.getSource().getTrueSource();
            }

            if (attacker == null)
            {
                return;
            }

            World world = zombie.world;

            if (world.isRemote)
            {
                return;
            }

            if (spawnedZombies.contains(event.getEntity().getUniqueID()))
            {
                return;
            }

            if (processedZombies.contains(event.getEntity().getUniqueID()))
            {
                return;
            }

            Random random = new Random();

            BlockPos spawnPos =
                    zombie.getPosition().add((random.nextDouble() - 0.5) * 5.0, 0, (random.nextDouble() - 0.5) * 5.0);

            spawnPos = findValidSpawnPosition(world, spawnPos, 10);

            if (hasSufficientSpace(world, spawnPos, 2, 3))
            {
                EntityZombie newZombie = new EntityZombie(world);
                newZombie.setPosition(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());

                GeneralStorageData parser = GeneralStorageData.instance;

                if (parser != null)
                {
                    List<GeneralStorageData.Equipment> configs = parser.getEquipmentConfigs();

                    if (configs != null && !configs.isEmpty())
                    {
                        GeneralStorageData.Equipment selectedConfig = getConfigByPriority(configs, random);

                        equipZombie(newZombie, selectedConfig.HeldItems, EntityEquipmentSlot.MAINHAND, random);
                        equipZombie(newZombie, selectedConfig.Helmets, EntityEquipmentSlot.HEAD, random);
                        equipZombie(newZombie, selectedConfig.ChestPlates, EntityEquipmentSlot.CHEST, random);
                        equipZombie(newZombie, selectedConfig.Leggings, EntityEquipmentSlot.LEGS, random);
                        equipZombie(newZombie, selectedConfig.Boots, EntityEquipmentSlot.FEET, random);
                    }
                }

                processedZombies.add(event.getEntity().getUniqueID());

                world.spawnEntity(newZombie);

                spawnedZombies.add(newZombie.getUniqueID());

                newZombie.setAttackTarget(attacker);
            }
        }
    }

    /**
     *
     * @param zombie
     * @param items
     * @param slot
     * @param random
     */
    private void equipZombie(EntityZombie zombie, List<String> items, EntityEquipmentSlot slot, Random random)
    {
        if (items != null && !items.isEmpty())
        {
            String item = items.get(random.nextInt(items.size()));
            ItemStack itemStack = new ItemStack(Objects.requireNonNull(Item.getByNameOrId(item)));

            if (itemStack.getItem() != Items.AIR)
            {
                zombie.setItemStackToSlot(slot, itemStack);
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
     * @param configs
     * @param random
     * @return
     */
    private GeneralStorageData.Equipment getConfigByPriority(List<GeneralStorageData.Equipment> configs, Random random)
    {
        int totalPriority = configs.stream().mapToInt(config -> config.Priority).sum();
        int randomValue = random.nextInt(totalPriority);

        int cumulativePriority = 0;

        for (GeneralStorageData.Equipment config : configs)
        {
            cumulativePriority += config.Priority;

            if (randomValue < cumulativePriority)
            {
                return config;
            }
        }

        return configs.get(configs.size() - 1);
    }
}
