package org.imesense.dynamicspawncontrol.eventdescriptions;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.config.dropitem.SkeletonDropConfig;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.ArrayList;
import java.util.List;

@InitLog
@TODO(value = "Add param 'getMinDurabilityPercent' in config", showOnce = false, priority = TODO.TodoPriority.HIGH)
public final class DropSkeletonItem
{
    private static volatile DropSkeletonItem _INSTANCE;

    public static DropSkeletonItem getInstance()
    {
        return CodeGeneric.getInstance(DropSkeletonItem.class);
    }

    public DropSkeletonItem()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    public void handleLivingDrops(LivingDropsEvent event)
    {
        if (!(event.getEntity() instanceof EntitySkeleton)) return;

        EntitySkeleton skeleton = (EntitySkeleton) event.getEntity();
        List<EntityItem> drops = event.getDrops();

        DamageSource source = event.getSource();
        if (source != null && (source.isExplosion() || source.isFireDamage()))
        {
            return;
        }

        if (!rollDifficultyChance(skeleton.world.getDifficulty()))
        {
            handleArrowDrops(skeleton, drops);
            return;
        }

        List<ItemStack> equipment = new ArrayList<>();

        addIfValid(equipment, skeleton.getItemStackFromSlot(EntityEquipmentSlot.HEAD));
        addIfValid(equipment, skeleton.getItemStackFromSlot(EntityEquipmentSlot.CHEST));
        addIfValid(equipment, skeleton.getItemStackFromSlot(EntityEquipmentSlot.LEGS));
        addIfValid(equipment, skeleton.getItemStackFromSlot(EntityEquipmentSlot.FEET));
        addIfValid(equipment, skeleton.getHeldItemMainhand());

        if (!equipment.isEmpty())
        {
            ItemStack selected = equipment.get(UniqueField.RANDOM.nextInt(equipment.size()));

            ItemStack drop = selected.copy();

            if (drop.isItemStackDamageable())
            {
                int max = drop.getMaxDamage();
                int dmg = drop.getItemDamage();

                double durability = 1.0 - ((double) dmg / max);

                if (durability < /*SkeletonDropConfig.getInstance(SkeletonDropConfig.class)
                        .getMinDurabilityPercent()*/0.6)
                {
                    dropBrokenItem(skeleton, drops);
                }
                else
                {
                    int spread = (int) (max * SkeletonDropConfig.getInstance(SkeletonDropConfig.class)
                            .getDamageSpreadFactor());

                    if (spread > 0)
                    {
                        drop.setItemDamage(
                                Math.min(max - 1, dmg + UniqueField.RANDOM.nextInt(spread))
                        );
                    }

                    addIfNotDuplicate(skeleton, drops, drop);
                }
            }
            else
            {
                addIfNotDuplicate(skeleton, drops, drop);
            }
        }

        handleArrowDrops(skeleton, drops);
    }

    private void addIfValid(List<ItemStack> list, ItemStack stack)
    {
        if (!stack.isEmpty() && stack.getItem() != Items.AIR)
        {
            list.add(stack);
        }
    }

    private void addIfNotDuplicate(EntitySkeleton skeleton, List<EntityItem> drops, ItemStack drop)
    {
        for (EntityItem item : drops)
        {
            if (item.getItem().isItemEqualIgnoreDurability(drop))
            {
                return;
            }
        }

        drops.add(new EntityItem(skeleton.world, skeleton.posX, skeleton.posY, skeleton.posZ, drop));
    }

    private boolean rollDifficultyChance(EnumDifficulty difficulty)
    {
        double chance;

        switch (difficulty)
        {
            case EASY:   chance = 0.05; break;
            case NORMAL: chance = 0.08; break;
            case HARD:   chance = 0.12; break;
            default:     chance = 0.05;
        }

        return UniqueField.RANDOM.nextDouble() <= chance;
    }

    private void dropBrokenItem(EntitySkeleton skeleton, List<EntityItem> drops)
    {
        drops.add(new EntityItem(skeleton.world, skeleton.posX, skeleton.posY, skeleton.posZ,
                new ItemStack(Items.IRON_NUGGET, 1 + UniqueField.RANDOM.nextInt(2))));
    }

    /* ───────── Стрелы ───────── */

    private void handleArrowDrops(EntitySkeleton skeleton, List<EntityItem> drops)
    {
        double arrowDropChance = 0.50;

        if (UniqueField.RANDOM.nextDouble() > arrowDropChance)
            return;

        for (EntityItem item : drops)
        {
            if (item.getItem().getItem() == Items.ARROW)
            {
                item.getItem().grow(
                        1 + SkeletonDropConfig.getInstance(SkeletonDropConfig.class)
                                .getArrowsToDrops()
                );
                return;
            }
        }

        addArrowsToDrops(skeleton, drops, SkeletonDropConfig.getInstance(SkeletonDropConfig.class).getArrowsToDrops());
    }

    private void addArrowsToDrops(EntitySkeleton skeleton, List<EntityItem> drops, byte arrowCount)
    {
        drops.add(new EntityItem(skeleton.world, skeleton.posX, skeleton.posY, skeleton.posZ, new ItemStack(Items.ARROW, arrowCount)));
    }
}
