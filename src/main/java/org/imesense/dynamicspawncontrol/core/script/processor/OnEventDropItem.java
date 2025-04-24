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
import org.imesense.dynamicspawncontrol.core.script.storage.dropitem.data.DropItem;
import org.imesense.dynamicspawncontrol.core.script.storage.dropitem.storage.GeneralDropItem;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.Random;

@InitLog
public final class OnEventDropItem
{
    private static volatile OnEventDropItem _INSTANCE;

    public static OnEventDropItem getInstance()
    {
        return CodeGeneric.getInstance(OnEventDropItem.class);
    }

    public OnEventDropItem()
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
            boolean entityMatches = false;

            for (ResourceLocation allowedEntity : data.entities)
            {
                if (entityResourceLocation.equals(allowedEntity))
                {
                    entityMatches = true;
                    break;
                }
            }

            if (!entityMatches)
            {
                continue;
            }

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
