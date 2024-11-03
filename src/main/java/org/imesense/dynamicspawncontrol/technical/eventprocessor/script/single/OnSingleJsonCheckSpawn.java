package org.imesense.dynamicspawncontrol.technical.eventprocessor.script.single;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.ProjectStructure;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.parser.GeneralStorageData;

import java.util.List;

/**
 *
 */
@Mod.EventBusSubscriber(modid = ProjectStructure.STRUCT_INFO_MOD.MOD_ID)
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
		CodeGenericUtil.printInitClassToLog(this.getClass());
		
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
    public synchronized void onCheckSpawn_0(LivingSpawnEvent.CheckSpawn checkSpawn)
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
