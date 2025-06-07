package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.script.storage.dropitem.data.DropItem;
import org.imesense.dynamicspawncontrol.core.script.storage.dropitem.storage.GeneralDropItem;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.Random;

@InitLog
@TODO(value = "Поломана оптимизация, к тому же переделать класс на схеме", showOnce = false, priority = TODO.TodoPriority.HIGH)
public final class OnEventDropItemOld
{
    private static volatile OnEventDropItemOld _INSTANCE;

    public static OnEventDropItemOld getInstance()
    {
        return CodeGeneric.getInstance(OnEventDropItemOld.class);
    }

    public OnEventDropItemOld()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    public void handleUpdateLivingDrops(LivingDropsEvent event)
    {
        Entity entity = event.getEntity();
        ResourceLocation entityResourceLocation = EntityList.getKey(entity);

        if (entityResourceLocation == null)
        {
            return;
        }

        Random random = new Random();

        for (DropItem.Data data : GeneralDropItem.getInstance().dropItemList)
        {
            if (entityResourceLocation.equals(data.entity))
            {
                for (DropItem.Data.ItemDrop drop : data.drops)
                {
                    if (drop.result == Event.Result.DENY)
                    {
                        continue;
                    }

                    boolean shouldDrop = drop.result == Event.Result.ALLOW ||
                            (drop.result == Event.Result.DEFAULT &&
                                    (drop.chance >= 1.0f || random.nextFloat() <= drop.chance));

                    if (shouldDrop)
                    {
                        int amount = drop.minAmount + (drop.maxAmount > drop.minAmount ?
                                random.nextInt(drop.maxAmount - drop.minAmount + 1) : 0);

                        if (amount > 0)
                        {
                            ItemStack itemStack = new ItemStack(Item.REGISTRY.getObject(drop.item), amount);
                            event.getDrops().add(new EntityItem(entity.world, entity.posX, entity.posY, entity.posZ, itemStack));
                        }
                    }
                }
            }
        }
    }
}
