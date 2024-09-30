package org.imesense.dynamicspawncontrol.technical.eventprocessor.script.single;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.parsers.GeneralStorageData;

import java.util.List;

/**
 *
 */
@Mod.EventBusSubscriber
public final class OnSingleJsonCheckSpawn
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public OnSingleJsonCheckSpawn()
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
