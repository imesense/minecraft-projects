package org.imesense.dynamicspawncontrol.eventdescriptions;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.config.dataLegacy.ZombieDropItemData;

import java.util.List;

public final class DropZombieItem
{
    public DropZombieItem()
    {
		CodeGeneric.printInitClassToLog(this.getClass());
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
                    ZombieDropItemData.ConfigDataZombieDrop.Instance.getHeadDamageFactor());

            addDamagedItemToDrops(zombie, drops, zombie.getItemStackFromSlot(EntityEquipmentSlot.CHEST),
                    ZombieDropItemData.ConfigDataZombieDrop.Instance.getChestDamageFactor());

            addDamagedItemToDrops(zombie, drops, zombie.getItemStackFromSlot(EntityEquipmentSlot.LEGS),
                    ZombieDropItemData.ConfigDataZombieDrop.Instance.getLegsDamageFactor());

            addDamagedItemToDrops(zombie, drops, zombie.getItemStackFromSlot(EntityEquipmentSlot.FEET),
                    ZombieDropItemData.ConfigDataZombieDrop.Instance.getFeetDamageFactor());

            addDamagedItemToDrops(zombie, drops, zombie.getHeldItemMainhand(),
                    ZombieDropItemData.ConfigDataZombieDrop.Instance.getHandItemDamageFactor());
        }
    }

    private void addDamagedItemToDrops(EntityZombie entityZombie, List<EntityItem> drops, ItemStack originalItem, double damageFactor)
    {
        if (originalItem.getItem() != Items.AIR)
        {
            if (UniqueField.RANDOM.nextDouble() < ZombieDropItemData.ConfigDataZombieDrop.Instance.getBreakItem())
            {
                return;
            }

            ItemStack itemStack = originalItem.copy();
            int maxDamage = itemStack.getMaxDamage();

            if (maxDamage > 0)
            {
                int minDamage = (int) (maxDamage * damageFactor);

                int damageSpread = (int) (maxDamage * ZombieDropItemData.ConfigDataZombieDrop.Instance.getDamageSpreadFactor());
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
