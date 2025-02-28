package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraft.entity.EntityList;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import org.imesense.dynamicspawncontrol.core.script.actioncollector.*;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.script.initializer.ConceptScriptProcessor;
import org.imesense.dynamicspawncontrol.core.script.storage.StoringScriptData;

import java.util.*;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventEntityJoinWorld extends ConceptScriptProcessor
{
    public OnEventEntityJoinWorld()
    {
        super();
    }

    @SubscribeEvent
    public void onEntityJoinWorld(LivingSpawnEvent.CheckSpawn event)
    {
        String entityType = EntityList.getEntityString(event.getEntity());

        if (entityType == null)
        {
            Log.writeDataToLogFile(0, "entityType is null");
            return;
        }

        String fullEntityType = entityType.contains(":") ? entityType : "minecraft:" + entityType.toLowerCase();

        StoringScriptData generalStorageData = StoringScriptData.Instance;

        if (generalStorageData != null)
        {
            List<StoringScriptData.Equipment> configs = generalStorageData.getEquipmentConfigs();

            if (configs != null && !configs.isEmpty())
            {
                List<StoringScriptData.Equipment> filteredConfigs = configs.stream()
                        .filter(config -> config.entityType.equals(fullEntityType))
                        .collect(Collectors.toList());

                if (!filteredConfigs.isEmpty())
                {
                    StoringScriptData.Equipment selectedConfig = Priority.getInstance().getConfigByPriority(filteredConfigs, UniqueField.RANDOM.self());

                    if (!World.getInstance().checkHeight(event.getEntity(), selectedConfig.minHeight, selectedConfig.maxHeight))
                    {
                        return;
                    }

                    if (selectedConfig.seeSky != null)
                    {
                        boolean canSeeSky = event.getWorld().canBlockSeeSky(event.getEntity().getPosition());

                        if ((selectedConfig.seeSky && !canSeeSky) || (!selectedConfig.seeSky && canSeeSky))
                        {
                            return;
                        }
                    }

                    if (selectedConfig.name != null)
                    {
                        event.getEntity().setCustomNameTag(selectedConfig.name);
                        event.getEntity().setAlwaysRenderNameTag(true);
                    }

                    Equipment.getInstance().equipEntity(event.getEntity(), selectedConfig, UniqueField.RANDOM.self());
                }
            }
        }
    }
}
