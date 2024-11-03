package org.imesense.dynamicspawncontrol.technical.eventprocessor.primitive;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.ProjectStructure;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.technical.config.zombiedropitem.DataZombieDropItem;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

import java.util.List;
import java.util.Random;

/**
 *
 */
@Mod.EventBusSubscriber(modid = ProjectStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnDropZombieItem
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public OnDropZombieItem()
    {
		CodeGenericUtil.printInitClassToLog(this.getClass());
		
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
        if (livingDropsEvent.getEntity() instanceof EntityZombie)
        {
            EntityZombie entityZombie = (EntityZombie) livingDropsEvent.getEntity();

            List<EntityItem> drops = livingDropsEvent.getDrops();

            addDamagedItemToDrops(entityZombie, drops, entityZombie.getItemStackFromSlot(EntityEquipmentSlot.HEAD),
                    DataZombieDropItem.ConfigDataZombieDrop.Instance.getHeadDamageFactor());

            addDamagedItemToDrops(entityZombie, drops, entityZombie.getItemStackFromSlot(EntityEquipmentSlot.CHEST),
                    DataZombieDropItem.ConfigDataZombieDrop.Instance.getChestDamageFactor());

            addDamagedItemToDrops(entityZombie, drops, entityZombie.getItemStackFromSlot(EntityEquipmentSlot.LEGS),
                    DataZombieDropItem.ConfigDataZombieDrop.Instance.getLegsDamageFactor());

            addDamagedItemToDrops(entityZombie, drops, entityZombie.getItemStackFromSlot(EntityEquipmentSlot.FEET),
                    DataZombieDropItem.ConfigDataZombieDrop.Instance.getFeetDamageFactor());

            addDamagedItemToDrops(entityZombie, drops, entityZombie.getHeldItemMainhand(),
                    DataZombieDropItem.ConfigDataZombieDrop.Instance.getHandItemDamageFactor());
        }
    }

    /**
     *
     * @param entityZombie
     * @param drops
     * @param originalItem
     * @param damageFactor
     */
    private void addDamagedItemToDrops(EntityZombie entityZombie, List<EntityItem> drops, ItemStack originalItem, double damageFactor)
    {
        if (originalItem.getItem() != Items.AIR)
        {
            if (new Random().nextDouble() < DataZombieDropItem.ConfigDataZombieDrop.Instance.getBreakItem())
            {
                return;
            }

            ItemStack itemStack = originalItem.copy();
            int maxDamage = itemStack.getMaxDamage();

            if (maxDamage > 0)
            {
                Random random = new Random();
                int minDamage = (int) (maxDamage * damageFactor);

                int damageSpread = (int) (maxDamage * DataZombieDropItem.ConfigDataZombieDrop.Instance.getDamageSpreadFactor());
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

            drops.add(new EntityItem(entityZombie.world, entityZombie.posX, entityZombie.posY, entityZombie.posZ, itemStack));
        }
    }
}
