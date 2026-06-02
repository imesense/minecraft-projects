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
import org.imesense.dynamicspawncontrol.core.logfile.LogManager;
import org.imesense.dynamicspawncontrol.core.script.storage.dropitem.data.DropItem;
import org.imesense.dynamicspawncontrol.core.script.storage.dropitem.storage.GeneralDropItem;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.Random;

@InitLog
@TODO(value = "Поломана оптимизация, к тому же переделать класс на схеме", showOnce = false, priority = TODO.TodoPriority.HIGH)
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

        int currentDimension = entity.world.provider.getDimension();
        Random random = new Random();

        boolean foundRules = false;

        for (DropItem.Data data : GeneralDropItem.getInstance().dropItemList)
        {
            if (!entityResourceLocation.equals(data.entity))
            {
                continue;
            }

            foundRules = true;

            if (data.idDimension != null && data.idDimension != currentDimension)
            {
                LogManager.info(String.format(
                        "[DropItem] Skipping rule for %s: dimension mismatch (need %d, got %d)",
                        data.entity, data.idDimension, currentDimension
                ));
                continue;
            }

            LogManager.info(String.format(
                    "[DropItem] Processing rule for %s in dimension %d",
                    data.entity, currentDimension
            ));

            int itemsAdded = 0;

            for (DropItem.Data.ItemDrop drop : data.drops)
            {
                if (drop.result == Event.Result.DENY)
                {
                    LogManager.info(String.format(
                            "  [DropItem] SKIP item %s: result=DENY",
                            drop.item
                    ));

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

                        itemsAdded++;

                        LogManager.info(String.format(
                                "  [DropItem] ADDED %s x%d (chance: %.2f, result: %s)",
                                drop.item, amount, drop.chance, drop.result
                        ));
                    }
                    else
                    {
                        LogManager.info(String.format(
                                "  [DropItem] SKIP item %s: amount=0",
                                drop.item
                        ));
                    }
                }
                else
                {
                    LogManager.info(String.format(
                            "  [DropItem] FAILED chance for %s: %.2f (result: %s)",
                            drop.item, drop.chance, drop.result
                    ));
                }
            }

            LogManager.info(String.format(
                    "[DropItem] Rule for %s completed: %d items added",
                    data.entity, itemsAdded
            ));
        }

        if (!foundRules)
        {
            //Log.write(0, String.format(
           //         "[DropItem] No rules found for entity %s in dimension %d",
           ///         entityResourceLocation, currentDimension
           // ));
        }
        else
        {
            LogManager.info(String.format(
                    "[DropItem] Finished processing drops for %s in dimension %d. Total drops: %d",
                    entity.getName(), currentDimension, event.getDrops().size()
            ));
        }
    }
}
