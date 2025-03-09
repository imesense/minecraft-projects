package org.imesense.dynamicspawncontrol.event;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.config.data.SkeletonDropItemData;

import java.util.List;

@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class DropSkeletonItem
{
    public DropSkeletonItem()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
    }

    @SubscribeEvent
    public void onUpdateLivingDropsEvent_0(LivingDropsEvent livingDropsEvent)
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

            double arrowDropChance = 0.50;

            if (UniqueField.RANDOM.nextDouble() < arrowDropChance)
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

    private void addDamagedItemToDrops(EntitySkeleton entitySkeleton, List<EntityItem> drops, ItemStack originalItem, double damageFactor)
    {
        if (originalItem.getItem() != Items.AIR)
        {
            if (UniqueField.RANDOM.nextDouble() < SkeletonDropItemData.ConfigDataSkeletonDrop.Instance.getBreakItem())
            {
                return;
            }

            ItemStack itemStack = originalItem.copy();
            int maxDamage = itemStack.getMaxDamage();

            if (maxDamage > 0)
            {
                int minDamage = (int) (maxDamage * damageFactor);

                int damageSpread = (int) (maxDamage * SkeletonDropItemData.ConfigDataSkeletonDrop.Instance.getDamageSpreadFactor());
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

