package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import org.imesense.dynamicspawncontrol.core.script.actioncollector.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.script.storage.checkspawn.data.*;
import org.imesense.dynamicspawncontrol.core.script.storage.checkspawn.storage.GeneralCheckSpawnStorage;
import org.imesense.dynamicspawncontrol.core.script.storage.checkspawn.storage.SupportCheckSpawnStorage;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class OnEventCheckSpawn
{
    private static volatile OnEventCheckSpawn _INSTANCE;

    public static OnEventCheckSpawn getInstance()
    {
        return CodeGeneric.getInstance(OnEventCheckSpawn.class);
    }

    public void handleLivingSpawnEventCheckSpawn(LivingSpawnEvent.CheckSpawn event)
    {
        ResourceLocation entityType = EntityList.getKey(event.getEntity());
        
        GeneralCheckSpawnStorage generalStorageData = GeneralCheckSpawnStorage.getInstance();
        SupportCheckSpawnStorage supportStorageScriptData = SupportCheckSpawnStorage.getInstance();

        if (generalStorageData != null)
        {
            List<EntityEquipment.Data> configs = generalStorageData.entityEquipmentList;
            List<ProfilePriority.Data> randomDataList = generalStorageData.profilePriorityList;
            List<GameWorld.Data> worldDataList = generalStorageData.gameWorldList;

            if (configs != null && !configs.isEmpty() && randomDataList != null && !randomDataList.isEmpty() && worldDataList != null && !worldDataList.isEmpty())
            {
                List<ProfilePriority.Data> filteredRandomData = IntStream.range(0, configs.size())
                        .filter(i -> i < generalStorageData.entityDescriptionsList.size())
                        .filter(i -> generalStorageData.entityDescriptionsList.get(i) != null &&
                                entityType.equals(generalStorageData.entityDescriptionsList.get(i).entityType))
                        .mapToObj(randomDataList::get)
                        .collect(Collectors.toList());

                if (!filteredRandomData.isEmpty())
                {
                    ProfilePriority.Data selectedRandomData = Priority.getInstance()
                            .getConfigByPriority(filteredRandomData, UniqueField.RANDOM.self());

                    Integer selectedIndex = randomDataList.indexOf(selectedRandomData);

                    EntityEquipment.Data selectedConfig = configs.get(selectedIndex);
                    GameWorld.Data selectedWorldData = worldDataList.get(selectedIndex);
                    EntityAttributes.Data entityAttributes = generalStorageData.entityAttributesList.get(selectedIndex);
                    EntityDescription.Data entityDescription = generalStorageData.entityDescriptionsList.get(selectedIndex);

                    if (!World.getInstance().checkHeight(event.getEntity(), selectedWorldData.minHeight, selectedWorldData.maxHeight))
                    {
                        return;
                    }

                    if (selectedWorldData.seeSky != null)
                    {
                        Boolean canSeeSky = event.getWorld().canBlockSeeSky(event.getEntity().getPosition());

                        if ((selectedWorldData.seeSky && !canSeeSky) || (!selectedWorldData.seeSky && canSeeSky))
                        {
                            return;
                        }
                    }

                    if (entityDescription != null && entityDescription.name != null)
                    {
                        event.getEntity().setCustomNameTag(entityDescription.name);
                        event.getEntity().setAlwaysRenderNameTag(true);
                    }

                    Equipment.getInstance().equipEntity(event.getEntity(), selectedConfig, entityDescription, entityAttributes, UniqueField.RANDOM.self());
                }
            }

            List<SupportCheckSpawnStorage.DataSupport> dataSupports = supportStorageScriptData.dataSupportList;

            if (dataSupports != null && !dataSupports.isEmpty())
            {
                List<SupportCheckSpawnStorage.DataSupport> filteredDataSupports = dataSupports.stream()
                        .filter(dataSupport -> entityType.equals(dataSupport.entityType))
                        .collect(Collectors.toList());

                if (!filteredDataSupports.isEmpty())
                {
                    for (SupportCheckSpawnStorage.DataSupport dataSupport : filteredDataSupports)
                    {
                        if (dataSupport.seeSky != null)
                        {
                            Boolean canSeeSky = event.getWorld().canBlockSeeSky(event.getEntity().getPosition());

                            if ((dataSupport.seeSky && !canSeeSky) || (!dataSupport.seeSky && canSeeSky))
                            {
                                continue;
                            }
                        }

                        if (dataSupport.potion != null && !dataSupport.potion.isEmpty())
                        {
                            Potion.getInstance().applyPotionEffects((EntityLivingBase) event.getEntity(), dataSupport.potion, UniqueField.RANDOM.self());
                        }
                    }
                }
            }
        }
    }
}