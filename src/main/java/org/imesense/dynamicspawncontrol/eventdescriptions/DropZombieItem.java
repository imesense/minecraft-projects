package org.imesense.dynamicspawncontrol.eventdescriptions;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.config.dropitem.ZombieDropConfig;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.ArrayList;
import java.util.List;

@InitLog
@TODO(value = "Add param 'getMinDurabilityPercent' in config", showOnce = false, priority = TODO.TodoPriority.HIGH)
public final class DropZombieItem
{
    private static volatile DropZombieItem _INSTANCE;

    public DropZombieItem()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    public static DropZombieItem getInstance()
    {
        return CodeGeneric.getInstance(DropZombieItem.class);
    }

    public void handleZombieDrops(LivingDropsEvent event)
    {
        if (!(event.getEntity() instanceof EntityZombie)) return;

        EntityZombie zombie = (EntityZombie) event.getEntity();
        List<EntityItem> drops = event.getDrops();

        DamageSource source = event.getSource();

        if (source != null && (source.isExplosion() || source.isFireDamage()))
        {
            return;
        }

        if (!rollDifficultyChance(zombie.world.getDifficulty()))
        {
            return;
        }

        List<ItemStack> equipment = new ArrayList<>();

        addIfValid(equipment, zombie.getItemStackFromSlot(EntityEquipmentSlot.HEAD));
        addIfValid(equipment, zombie.getItemStackFromSlot(EntityEquipmentSlot.CHEST));
        addIfValid(equipment, zombie.getItemStackFromSlot(EntityEquipmentSlot.LEGS));
        addIfValid(equipment, zombie.getItemStackFromSlot(EntityEquipmentSlot.FEET));
        addIfValid(equipment, zombie.getHeldItemMainhand());

        if (equipment.isEmpty())
            return;

        ItemStack selected = equipment.get(
                UniqueField.RANDOM.nextInt(equipment.size())
        );

        ItemStack drop = selected.copy();

        if (drop.isItemStackDamageable())
        {
            int max = drop.getMaxDamage();
            int dmg = drop.getItemDamage();

            double durability = 1.0 - ((double) dmg / max);

            if (durability < /*ZombieDropConfig.getInstance(ZombieDropConfig.class)
                    .getMinDurabilityPercent()*/0.6)
            {
                dropBrokenItem(zombie, drops);
                return;
            }

            int spread = (int) (max * ZombieDropConfig.getInstance(ZombieDropConfig.class)
                    .getDamageSpreadFactor());

            if (spread > 0)
            {
                drop.setItemDamage(
                        Math.min(max - 1, dmg + UniqueField.RANDOM.nextInt(spread))
                );
            }
        }

        for (EntityItem entityItem : drops)
        {
            if (entityItem.getItem().isItemEqualIgnoreDurability(drop))
            {
                return;
            }
        }

        drops.add(new EntityItem(zombie.world, zombie.posX, zombie.posY, zombie.posZ, drop));
    }

    private void addIfValid(List<ItemStack> list, ItemStack stack)
    {
        if (!stack.isEmpty() && stack.getItem() != Items.AIR)
        {
            list.add(stack);
        }
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

    private void dropBrokenItem(EntityZombie zombie, List<EntityItem> drops)
    {
        drops.add(new EntityItem(zombie.world, zombie.posX, zombie.posY, zombie.posZ,
                    new ItemStack(Items.IRON_NUGGET, 1 + UniqueField.RANDOM.nextInt(2))
        ));
    }
}
