package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.script.storage.dropitem.storage.GeneralDropItem;

@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public class OnEventDropItem
{
    @SubscribeEvent
    public void onUpdateLivingDrops_0(LivingDropsEvent livingDropsEvent)
    {
        Entity entity = livingDropsEvent.getEntity();
        ResourceLocation entityResourceLocation = EntityList.getKey(entity);

        if (entityResourceLocation == null)
        {
            return;
        }

        for (GeneralDropItem.Data data : GeneralDropItem.getInstance().dropItemList)
        {
            if (entityResourceLocation.equals(data.entity))
            {
                for (GeneralDropItem.Data.ItemDrop drop : data.drops)
                {
                    ItemStack itemStack = new ItemStack(Item.REGISTRY.getObject(drop.item), drop.amount);
                    livingDropsEvent.getDrops().add(new EntityItem(entity.world, entity.posX, entity.posY, entity.posZ, itemStack));
                }
            }
        }
    }
}
