package org.imesense.dynamicspawncontrol.eventprocessor.eventscript.single;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.parser.algo.GeneralStorageData;

import java.util.List;

/**
 *
 */
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventSingleJsonCheckSpawn
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public OnEventSingleJsonCheckSpawn()
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
     * @param checkSpawn
     */
    @SubscribeEvent
    public void onCheckSpawn_0(LivingSpawnEvent.CheckSpawn checkSpawn)
    {
        if (checkSpawn.getWorld().isRemote)
        {
            return;
        }

        if (checkSpawn.getWorld().canBlockSeeSky(checkSpawn.getEntity().getPosition()))
        {
            EntityLiving entityLiving = (EntityLiving) checkSpawn.getEntity();
            ResourceLocation resourceLocation = EntityList.getKey(entityLiving);

            if (resourceLocation != null)
            {
                List<String> blockedEntities = GeneralStorageData.Instance.getEntitiesProhibitedOutdoors();

                if (blockedEntities != null && blockedEntities.contains(resourceLocation.toString()))
                {
                    checkSpawn.setResult(LivingSpawnEvent.Result.DENY);
                }
            }
        }
    }
}
