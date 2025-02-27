package org.imesense.dynamicspawncontrol.core.script.actioncollector;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public final class Equip
{
    private static volatile Equip _INSTANCE;

    public static Equip getInstance()
    {
        if (_INSTANCE == null)
        {
            synchronized (Equip.class)
            {
                if (_INSTANCE == null)
                {
                    _INSTANCE = new Equip();
                }
            }
        }

        return _INSTANCE;
    }

    public void equipEntityWithItems(EntityLivingBase entity, List<String> items, EntityEquipmentSlot equipmentSlot, Random random)
    {
        if (items != null && !items.isEmpty())
        {
            String item = items.get(random.nextInt(items.size()));
            ItemStack itemStack = new ItemStack(Objects.requireNonNull(Item.getByNameOrId(item)));

            if (itemStack.getItem() != Items.AIR)
            {
                entity.setItemStackToSlot(equipmentSlot, itemStack);
            }
            else
            {
                Log.writeDataToLogFile(1, "Item not found: " + item);
                throw new RuntimeException("Item not found: " + item);
            }
        }
    }
}
