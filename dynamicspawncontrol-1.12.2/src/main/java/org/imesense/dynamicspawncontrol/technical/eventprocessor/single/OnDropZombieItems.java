package org.imesense.dynamicspawncontrol.technical.eventprocessor.single;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.technical.configs.ConfigZombieDropItem;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

import java.util.List;
import java.util.Random;

/**
 *
 */
@Mod.EventBusSubscriber
public final class OnDropZombieItems
{
    /**
     *
     * @param nameClass
     */
    public OnDropZombieItems(final String nameClass)
    {

    }

    /**
     *
     * @param event
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public synchronized void onUpdateLivingDropsEvent_0(LivingDropsEvent event)
    {
        if (event.getEntity() instanceof EntityZombie)
        {
            EntityZombie zombie = (EntityZombie)event.getEntity();

            List<EntityItem> drops = event.getDrops();

            addDamagedItemToDrops(zombie, drops, zombie.getItemStackFromSlot(EntityEquipmentSlot.HEAD), ConfigZombieDropItem.HeadDamageFactor);
            addDamagedItemToDrops(zombie, drops, zombie.getItemStackFromSlot(EntityEquipmentSlot.CHEST), ConfigZombieDropItem.ChestDamageFactor);
            addDamagedItemToDrops(zombie, drops, zombie.getItemStackFromSlot(EntityEquipmentSlot.LEGS), ConfigZombieDropItem.LegsDamageFactor);
            addDamagedItemToDrops(zombie, drops, zombie.getItemStackFromSlot(EntityEquipmentSlot.FEET), ConfigZombieDropItem.FeetDamageFactor);

            addDamagedItemToDrops(zombie, drops, zombie.getHeldItemMainhand(), ConfigZombieDropItem.HandItemDamageFactor);
        }
    }

    /**
     *
     * @param zombie
     * @param drops
     * @param originalItem
     * @param damageFactor
     */
    private void addDamagedItemToDrops(EntityZombie zombie, List<EntityItem> drops, ItemStack originalItem, double damageFactor)
    {
        if (originalItem.getItem() != Items.AIR)
        {
            if (new Random().nextDouble() < ConfigZombieDropItem.BreakItem)
            {
                return;
            }

            ItemStack damagedItem = originalItem.copy();
            int maxDamage = damagedItem.getMaxDamage();

            if (maxDamage > 0)
            {
                Random rand = new Random();
                int minDamage = (int)(maxDamage * damageFactor);

                int damageSpread = (int)(maxDamage * ConfigZombieDropItem.DamageSpreadFactor);
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

            drops.add(new EntityItem(zombie.world, zombie.posX, zombie.posY, zombie.posZ, damagedItem));
        }
    }
}
