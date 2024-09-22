package org.imesense.dynamicspawncontrol.technical.eventprocessor.single;

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
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.parsers.GeneralStorageData;

import java.util.*;

/**
 *
 */
@Mod.EventBusSubscriber
public final class OnSingleZombieSummonAID
{
    private final Set<UUID> _spawnedZombies = new HashSet<>();

    private final Set<UUID> _processedZombies = new HashSet<>();

    public OnSingleZombieSummonAID(final String nameClass)
    {
        Log.writeDataToLogFile(0, nameClass);
    }

    public boolean isPositionValid(World world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock().isAir(state, world, pos) && world.getBlockState(pos.down()).getBlock().isFullBlock(state);
    }

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

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onZombieAttack_0(LivingAttackEvent event)
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

            if (_spawnedZombies.contains(event.getEntity().getUniqueID()))
            {
                return;
            }

            if (_processedZombies.contains(event.getEntity().getUniqueID()))
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

                GeneralStorageData parser = GeneralStorageData.getInstance();

                if (parser != null)
                {
                    List<GeneralStorageData.EquipmentConfig> configs = parser.getEquipmentConfigs();

                    if (configs != null && !configs.isEmpty())
                    {
                        GeneralStorageData.EquipmentConfig selectedConfig = getConfigByPriority(configs, random);

                        equipZombie(newZombie, selectedConfig._heldItems, EntityEquipmentSlot.MAINHAND, random);
                        equipZombie(newZombie, selectedConfig._helmets, EntityEquipmentSlot.HEAD, random);
                        equipZombie(newZombie, selectedConfig._chestPlates, EntityEquipmentSlot.CHEST, random);
                        equipZombie(newZombie, selectedConfig._leggings, EntityEquipmentSlot.LEGS, random);
                        equipZombie(newZombie, selectedConfig._boots, EntityEquipmentSlot.FEET, random);
                    }
                }

                _processedZombies.add(event.getEntity().getUniqueID());

                world.spawnEntity(newZombie);

                _spawnedZombies.add(newZombie.getUniqueID());

                newZombie.setAttackTarget(attacker);
            }
        }
    }

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

    private GeneralStorageData.EquipmentConfig getConfigByPriority(List<GeneralStorageData.EquipmentConfig> configs, Random random)
    {
        int totalPriority = configs.stream().mapToInt(config -> config._priority).sum();
        int randomValue = random.nextInt(totalPriority);

        int cumulativePriority = 0;

        for (GeneralStorageData.EquipmentConfig config : configs)
        {
            cumulativePriority += config._priority;

            if (randomValue < cumulativePriority)
            {
                return config;
            }
        }

        return configs.get(configs.size() - 1);
    }
}
