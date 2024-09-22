package org.imesense.dynamicspawncontrol.technical.eventprocessor.single;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.parsers.GeneralStorageData;

import java.util.List;

@Mod.EventBusSubscriber
public final class OnSingleJsonCheckSpawn
{
    /**
     *
     * @param nameClass
     */
    public OnSingleJsonCheckSpawn(final String nameClass)
    {
        Log.writeDataToLogFile(0, nameClass);
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public synchronized void onCheckSpawn_0(LivingSpawnEvent.CheckSpawn event)
    {
        if (event.getWorld().isRemote)
        {
            return;
        }

        if (event.getWorld().canBlockSeeSky(event.getEntity().getPosition()))
        {
            EntityLiving entity = (EntityLiving) event.getEntity();
            ResourceLocation entityResource = EntityList.getKey(entity);

            if (entityResource != null)
            {
                List<String> blockedEntities = GeneralStorageData.getInstance().getEntitiesProhibitedOutdoors();

                if (blockedEntities != null && blockedEntities.contains(entityResource.toString()))
                {
                    event.setResult(LivingSpawnEvent.Result.DENY);
                }
            }
        }
    }
}
