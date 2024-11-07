package org.imesense.dynamicspawncontrol.event;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.config.data.ZombieDropItemData;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

import java.util.List;

/**
 *
 */
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventDropZombieItem
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public OnEventDropZombieItem()
    {
		CodeGeneric.printInitClassToLog(this.getClass());
		
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
    public void onUpdateLivingDropsEvent_0(LivingDropsEvent livingDropsEvent)
    {
        if (livingDropsEvent.getEntity() instanceof EntityZombie)
        {
            EntityZombie entityZombie = (EntityZombie) livingDropsEvent.getEntity();

            List<EntityItem> drops = livingDropsEvent.getDrops();

            addDamagedItemToDrops(entityZombie, drops, entityZombie.getItemStackFromSlot(EntityEquipmentSlot.HEAD),
                    ZombieDropItemData.ConfigDataZombieDrop.Instance.getHeadDamageFactor());

            addDamagedItemToDrops(entityZombie, drops, entityZombie.getItemStackFromSlot(EntityEquipmentSlot.CHEST),
                    ZombieDropItemData.ConfigDataZombieDrop.Instance.getChestDamageFactor());

            addDamagedItemToDrops(entityZombie, drops, entityZombie.getItemStackFromSlot(EntityEquipmentSlot.LEGS),
                    ZombieDropItemData.ConfigDataZombieDrop.Instance.getLegsDamageFactor());

            addDamagedItemToDrops(entityZombie, drops, entityZombie.getItemStackFromSlot(EntityEquipmentSlot.FEET),
                    ZombieDropItemData.ConfigDataZombieDrop.Instance.getFeetDamageFactor());

            addDamagedItemToDrops(entityZombie, drops, entityZombie.getHeldItemMainhand(),
                    ZombieDropItemData.ConfigDataZombieDrop.Instance.getHandItemDamageFactor());
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
