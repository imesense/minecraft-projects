package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.imesense.dynamicspawncontrol.core.script.storage.dropitem.data.DropItem;
import org.imesense.dynamicspawncontrol.core.script.storage.dropitem.storage.GeneralDropItem;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

public final class OnEventDropItem
{
    private static volatile OnEventDropItem _INSTANCE;

    public static OnEventDropItem getInstance()
    {
        return CodeGeneric.getInstance(OnEventDropItem.class);
    }

    public void handleUpdateLivingDrops(LivingDropsEvent event)
    {
        Entity entity = event.getEntity();
        ResourceLocation entityResourceLocation = EntityList.getKey(entity);

        if (entityResourceLocation == null)
        {
            return;
        }

        for (DropItem.Data data : GeneralDropItem.getInstance().dropItemList)
        {
            if (entityResourceLocation.equals(data.entity))
            {
                for (DropItem.Data.ItemDrop drop : data.drops)
                {
                    ItemStack itemStack = new ItemStack(Item.REGISTRY.getObject(drop.item), drop.amount);
                    event.getDrops().add(new EntityItem(entity.world, entity.posX, entity.posY, entity.posZ, itemStack));
                }
            }
        }
    }
}
