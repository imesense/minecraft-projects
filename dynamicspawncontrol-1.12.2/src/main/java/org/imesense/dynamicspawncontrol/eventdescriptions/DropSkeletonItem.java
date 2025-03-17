package org.imesense.dynamicspawncontrol.eventdescriptions;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.imesense.dynamicspawncontrol.core.config.DropItem.SkeletonDropConfig;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.List;

public final class DropSkeletonItem
{
    private static volatile DropSkeletonItem _INSTANCE;

    public static DropSkeletonItem getInstance()
    {
        return CodeGeneric.getInstance(DropSkeletonItem.class);
    }

    public DropSkeletonItem()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
    }

    public void handleLivingDrops(LivingDropsEvent event)
    {
        if (event.getEntity() instanceof EntitySkeleton)
        {
            EntitySkeleton skeleton = (EntitySkeleton) event.getEntity();
            List<EntityItem> drops = event.getDrops();

            addDamagedItemToDrops(skeleton, drops, skeleton.getItemStackFromSlot(EntityEquipmentSlot.HEAD),
                    SkeletonDropConfig.getInstance().getHeadDamageFactor());

            addDamagedItemToDrops(skeleton, drops, skeleton.getItemStackFromSlot(EntityEquipmentSlot.CHEST),
                    SkeletonDropConfig.getInstance().getChestDamageFactor());

            addDamagedItemToDrops(skeleton, drops, skeleton.getItemStackFromSlot(EntityEquipmentSlot.LEGS),
                    SkeletonDropConfig.getInstance().getLegsDamageFactor());

            addDamagedItemToDrops(skeleton, drops, skeleton.getItemStackFromSlot(EntityEquipmentSlot.FEET),
                    SkeletonDropConfig.getInstance().getFeetDamageFactor());

            addDamagedItemToDrops(skeleton, drops, skeleton.getHeldItemMainhand(),
                    SkeletonDropConfig.getInstance().getHandItemDamageFactor());

            handleArrowDrops(skeleton, drops);
        }
    }

    private void handleArrowDrops(EntitySkeleton skeleton, List<EntityItem> drops)
    {
        double arrowDropChance = 0.50;

        if (UniqueField.RANDOM.nextDouble() < arrowDropChance)
        {
            boolean arrowsDropped = false;

            for (EntityItem item : drops)
            {
                if (item.getItem().getItem() == Items.ARROW)
                {
                    int currentCount = item.getItem().getCount();
                    item.getItem().setCount(currentCount + 1 + SkeletonDropConfig.getInstance().getArrowsToDrops());
                    arrowsDropped = true;
                    break;
                }
            }

            if (!arrowsDropped)
            {
                addArrowsToDrops(skeleton, drops, SkeletonDropConfig.getInstance().getArrowsToDrops());
            }
        }
    }

    private void addDamagedItemToDrops(EntitySkeleton entitySkeleton, List<EntityItem> drops, ItemStack originalItem, double damageFactor)
    {
        if (originalItem.getItem() != Items.AIR)
        {
            if (UniqueField.RANDOM.nextDouble() < SkeletonDropConfig.getInstance().getBreakItem())
            {
                return;
            }

            ItemStack itemStack = originalItem.copy();
            int maxDamage = itemStack.getMaxDamage();

            if (maxDamage > 0)
            {
                int minDamage = (int) (maxDamage * damageFactor);

                int damageSpread = (int) (maxDamage * SkeletonDropConfig.getInstance().getDamageSpreadFactor());
                int randomDamage = minDamage + UniqueField.RANDOM.nextInt(damageSpread);

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

    private void addArrowsToDrops(EntitySkeleton entitySkeleton, List<EntityItem> drops, byte arrowCount)
    {
        ItemStack itemStack = new ItemStack(Items.ARROW, arrowCount);

        drops.add(new EntityItem
                (entitySkeleton.world, entitySkeleton.posX, entitySkeleton.posY, entitySkeleton.posZ, itemStack));
    }
}

