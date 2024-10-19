package org.imesense.dynamicspawncontrol.technical.eventprocessor.primitive;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.ProjectStructure;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.config.skeletondropitem.DataSkeletonDropItem;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

import java.util.List;
import java.util.Random;

/**
 *
 */
@Mod.EventBusSubscriber(modid = ProjectStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnDropSkeletonItems
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public OnDropSkeletonItems()
    {
        CodeGenericUtils.printInitClassToLog(this.getClass());

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
    public synchronized void onUpdateLivingDropsEvent_0(LivingDropsEvent event)
    {
        if (event.getEntity() instanceof EntitySkeleton)
        {
            EntitySkeleton skeleton = (EntitySkeleton)event.getEntity();

            List<EntityItem> drops = event.getDrops();

            addDamagedItemToDrops(skeleton, drops, skeleton.getItemStackFromSlot(EntityEquipmentSlot.HEAD),
                    DataSkeletonDropItem.ConfigDataSkeletonDrop.instance.getHeadDamageFactor());

            addDamagedItemToDrops(skeleton, drops, skeleton.getItemStackFromSlot(EntityEquipmentSlot.CHEST),
                    DataSkeletonDropItem.ConfigDataSkeletonDrop.instance.getChestDamageFactor());

            addDamagedItemToDrops(skeleton, drops, skeleton.getItemStackFromSlot(EntityEquipmentSlot.LEGS),
                    DataSkeletonDropItem.ConfigDataSkeletonDrop.instance.getLegsDamageFactor());

            addDamagedItemToDrops(skeleton, drops, skeleton.getItemStackFromSlot(EntityEquipmentSlot.FEET),
                    DataSkeletonDropItem.ConfigDataSkeletonDrop.instance.getFeetDamageFactor());

            addDamagedItemToDrops(skeleton, drops, skeleton.getHeldItemMainhand(),
                    DataSkeletonDropItem.ConfigDataSkeletonDrop.instance.getHandItemDamageFactor());

            Random rand = new Random();
            double arrowDropChance = 0.5;

            if (rand.nextDouble() < arrowDropChance)
            {
                boolean arrowsDropped = false;
                for (EntityItem item : drops)
                {
                    if (item.getItem().getItem() == Items.ARROW)
                    {
                        int currentCount = item.getItem().getCount();

                        item.getItem().setCount
                                (currentCount + 1 + DataSkeletonDropItem.ConfigDataSkeletonDrop.instance.getArrowsToDrops());
                        arrowsDropped = true;

                        break;
                    }
                }

                if (!arrowsDropped)
                {
                    addArrowsToDrops(skeleton, drops, DataSkeletonDropItem.ConfigDataSkeletonDrop.instance.getArrowsToDrops());
                }
            }
        }
    }

    /**
     *
     * @param skeleton
     * @param drops
     * @param originalItem
     * @param damageFactor
     */
    private void addDamagedItemToDrops(EntitySkeleton skeleton, List<EntityItem> drops, ItemStack originalItem, double damageFactor)
    {
        if (originalItem.getItem() != Items.AIR)
        {
            if (new Random().nextDouble() < DataSkeletonDropItem.ConfigDataSkeletonDrop.instance.getBreakItem())
            {
                return;
            }

            ItemStack damagedItem = originalItem.copy();
            int maxDamage = damagedItem.getMaxDamage();

            if (maxDamage > 0)
            {
                Random rand = new Random();
                int minDamage = (int)(maxDamage * damageFactor);

                int damageSpread = (int)(maxDamage * DataSkeletonDropItem.ConfigDataSkeletonDrop.instance.getDamageSpreadFactor());
                int randomDamage = minDamage + rand.nextInt(damageSpread);

                damagedItem.setItemDamage(randomDamage);
            }

            for (EntityItem item : drops)
            {
                ItemStack stack = item.getItem();

                if (stack.isItemEqualIgnoreDurability(damagedItem))
                {
                    return;
                }
            }

            drops.add(new EntityItem(skeleton.world, skeleton.posX, skeleton.posY, skeleton.posZ, damagedItem));
        }
    }

    /**
     *
     * @param skeleton
     * @param drops
     * @param arrowCount
     */
    private void addArrowsToDrops(EntitySkeleton skeleton, List<EntityItem> drops, byte arrowCount)
    {
        ItemStack arrows = new ItemStack(Items.ARROW, arrowCount);
        drops.add(new EntityItem(skeleton.world, skeleton.posX, skeleton.posY, skeleton.posZ, arrows));
    }
}

