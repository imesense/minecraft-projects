package org.imesense.dynamicspawncontrol.technical.eventprocessor.primitive;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.config.zombiedropitem.DataZombieDropItem;
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
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public OnDropZombieItems()
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
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public synchronized void onUpdateLivingDropsEvent_0(LivingDropsEvent event)
    {
        if (event.getEntity() instanceof EntityZombie)
        {
            EntityZombie zombie = (EntityZombie)event.getEntity();

            List<EntityItem> drops = event.getDrops();

            addDamagedItemToDrops(zombie, drops, zombie.getItemStackFromSlot(EntityEquipmentSlot.HEAD),
                    DataZombieDropItem.ConfigDataZombieDrop.instance.getHeadDamageFactor());

            addDamagedItemToDrops(zombie, drops, zombie.getItemStackFromSlot(EntityEquipmentSlot.CHEST),
                    DataZombieDropItem.ConfigDataZombieDrop.instance.getChestDamageFactor());

            addDamagedItemToDrops(zombie, drops, zombie.getItemStackFromSlot(EntityEquipmentSlot.LEGS),
                    DataZombieDropItem.ConfigDataZombieDrop.instance.getLegsDamageFactor());

            addDamagedItemToDrops(zombie, drops, zombie.getItemStackFromSlot(EntityEquipmentSlot.FEET),
                    DataZombieDropItem.ConfigDataZombieDrop.instance.getFeetDamageFactor());

            addDamagedItemToDrops(zombie, drops, zombie.getHeldItemMainhand(),
                    DataZombieDropItem.ConfigDataZombieDrop.instance.getHandItemDamageFactor());
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
            if (new Random().nextDouble() < DataZombieDropItem.ConfigDataZombieDrop.instance.getBreakItem())
            {
                return;
            }

            ItemStack damagedItem = originalItem.copy();
            int maxDamage = damagedItem.getMaxDamage();

            if (maxDamage > 0)
            {
                Random rand = new Random();
                int minDamage = (int)(maxDamage * damageFactor);

                int damageSpread = (int)(maxDamage * DataZombieDropItem.ConfigDataZombieDrop.instance.getDamageSpreadFactor());
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
