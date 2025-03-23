package org.imesense.dynamicspawncontrol.eventdescriptions;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.config.dropitem.ZombieDropConfig;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.List;

@InitLog
public final class DropZombieItem
{
    public DropZombieItem()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    private static volatile DropZombieItem _INSTANCE;

    public static DropZombieItem getInstance()
    {
        return CodeGeneric.getInstance(DropZombieItem.class);
    }

    public void handleZombieDrops(LivingDropsEvent event)
    {
        if (event.getEntity() instanceof EntityZombie)
        {
            EntityZombie zombie = (EntityZombie) event.getEntity();
            List<EntityItem> drops = event.getDrops();

            addDamagedItemToDrops(zombie, drops, zombie.getItemStackFromSlot(EntityEquipmentSlot.HEAD),
                    ZombieDropConfig.getInstance(ZombieDropConfig.class).getHeadDamageFactor());

            addDamagedItemToDrops(zombie, drops, zombie.getItemStackFromSlot(EntityEquipmentSlot.CHEST),
                    ZombieDropConfig.getInstance(ZombieDropConfig.class).getChestDamageFactor());

            addDamagedItemToDrops(zombie, drops, zombie.getItemStackFromSlot(EntityEquipmentSlot.LEGS),
                    ZombieDropConfig.getInstance(ZombieDropConfig.class).getLegsDamageFactor());

            addDamagedItemToDrops(zombie, drops, zombie.getItemStackFromSlot(EntityEquipmentSlot.FEET),
                    ZombieDropConfig.getInstance(ZombieDropConfig.class).getFeetDamageFactor());

            addDamagedItemToDrops(zombie, drops, zombie.getHeldItemMainhand(),
                    ZombieDropConfig.getInstance(ZombieDropConfig.class).getHandItemDamageFactor());
        }
    }

    private void addDamagedItemToDrops(EntityZombie entityZombie, List<EntityItem> drops, ItemStack originalItem, double damageFactor)
    {
        if (originalItem.getItem() != Items.AIR)
        {
            if (UniqueField.RANDOM.nextDouble() <
                    ZombieDropConfig.getInstance(ZombieDropConfig.class).getBreakItem())
            {
                return;
            }

            ItemStack itemStack = originalItem.copy();
            int maxDamage = itemStack.getMaxDamage();

            if (maxDamage > 0)
            {
                int minDamage = (int) (maxDamage * damageFactor);

                int damageSpread = (int) (maxDamage * ZombieDropConfig.getInstance(ZombieDropConfig.class).getDamageSpreadFactor());
                int randomDamage = minDamage + UniqueField.RANDOM.nextInt(damageSpread);

                itemStack.setItemDamage(randomDamage);
            }

            for (EntityItem entityItem : drops)
            {
                ItemStack itemStack1 = entityItem.getItem();

                if (itemStack1.isItemEqualIgnoreDurability(itemStack))
                {
                    return;
                }
            }

            drops.add(new EntityItem(entityZombie.world, entityZombie.posX, entityZombie.posY, entityZombie.posZ, itemStack));
        }
    }
}
