package org.imesense.dynamicspawncontrol.technical.eventprocessor.primitive;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.config.data.SkeletonDropItemData;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

import java.util.List;
import java.util.Random;

/**
 *
 */
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnDropSkeletonItem
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public OnDropSkeletonItem()
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
     * @param livingDropsEvent
     */
    @SubscribeEvent
    public synchronized void onUpdateLivingDropsEvent_0(LivingDropsEvent livingDropsEvent)
    {
        if (livingDropsEvent.getEntity() instanceof EntitySkeleton)
        {
            EntitySkeleton entitySkeleton = (EntitySkeleton) livingDropsEvent.getEntity();

            List<EntityItem> drops = livingDropsEvent.getDrops();

            addDamagedItemToDrops(entitySkeleton, drops, entitySkeleton.getItemStackFromSlot(EntityEquipmentSlot.HEAD),
                    SkeletonDropItemData.ConfigDataSkeletonDrop.Instance.getHeadDamageFactor());

            addDamagedItemToDrops(entitySkeleton, drops, entitySkeleton.getItemStackFromSlot(EntityEquipmentSlot.CHEST),
                    SkeletonDropItemData.ConfigDataSkeletonDrop.Instance.getChestDamageFactor());

            addDamagedItemToDrops(entitySkeleton, drops, entitySkeleton.getItemStackFromSlot(EntityEquipmentSlot.LEGS),
                    SkeletonDropItemData.ConfigDataSkeletonDrop.Instance.getLegsDamageFactor());

            addDamagedItemToDrops(entitySkeleton, drops, entitySkeleton.getItemStackFromSlot(EntityEquipmentSlot.FEET),
                    SkeletonDropItemData.ConfigDataSkeletonDrop.Instance.getFeetDamageFactor());

            addDamagedItemToDrops(entitySkeleton, drops, entitySkeleton.getHeldItemMainhand(),
                    SkeletonDropItemData.ConfigDataSkeletonDrop.Instance.getHandItemDamageFactor());

            Random rand = new Random();
            double arrowDropChance = 0.50;

            if (rand.nextDouble() < arrowDropChance)
            {
                boolean arrowsDropped = false;

                for (EntityItem item : drops)
                {
                    if (item.getItem().getItem() == Items.ARROW)
                    {
                        int currentCount = item.getItem().getCount();

                        item.getItem().setCount
                                (currentCount + 1 + SkeletonDropItemData.ConfigDataSkeletonDrop.Instance.getArrowsToDrops());

                        arrowsDropped = true;

                        break;
                    }
                }

                if (!arrowsDropped)
                {
                    addArrowsToDrops(entitySkeleton, drops, SkeletonDropItemData.ConfigDataSkeletonDrop.Instance.getArrowsToDrops());
                }
            }
        }
    }

    /**
     *
     * @param entitySkeleton
     * @param drops
     * @param originalItem
     * @param damageFactor
     */
    private void addDamagedItemToDrops(EntitySkeleton entitySkeleton, List<EntityItem> drops, ItemStack originalItem, double damageFactor)
    {
        if (originalItem.getItem() != Items.AIR)
        {
            if (new Random().nextDouble() < SkeletonDropItemData.ConfigDataSkeletonDrop.Instance.getBreakItem())
            {
                return;
            }

            ItemStack itemStack = originalItem.copy();
            int maxDamage = itemStack.getMaxDamage();

            if (maxDamage > 0)
            {
                Random random = new Random();
                int minDamage = (int) (maxDamage * damageFactor);

                int damageSpread = (int) (maxDamage * SkeletonDropItemData.ConfigDataSkeletonDrop.Instance.getDamageSpreadFactor());
                int randomDamage = minDamage + random.nextInt(damageSpread);

                itemStack.setItemDamage(randomDamage);
            }

            for (EntityItem item : drops)
            {
                ItemStack itemStack1 = item.getItem();

                if (itemStack1.isItemEqualIgnoreDurability(itemStack))
                {
                    return;
                }
            }

            drops.add(new EntityItem(entitySkeleton.world,
                    entitySkeleton.posX, entitySkeleton.posY, entitySkeleton.posZ, itemStack));
        }
    }

    /**
     *
     * @param entitySkeleton
     * @param drops
     * @param arrowCount
     */
    private void addArrowsToDrops(EntitySkeleton entitySkeleton, List<EntityItem> drops, byte arrowCount)
    {
        ItemStack itemStack = new ItemStack(Items.ARROW, arrowCount);

        drops.add(new EntityItem
                (entitySkeleton.world, entitySkeleton.posX, entitySkeleton.posY, entitySkeleton.posZ, itemStack));
    }
}

