package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
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
import java.util.stream.IntStream;

@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventEntityJoinWorld extends ConceptScriptProcessor
{
    private final Random random = new Random();

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

        StoringScriptData generalStorageData = StoringScriptData.getInstance();

        if (generalStorageData != null)
        {
            List<StoringScriptData.Equipment> configs = generalStorageData.equipmentList;
            List<StoringScriptData.RandomData> randomDataList = generalStorageData.randomDataList;
            List<StoringScriptData.WorldData> worldDataList = generalStorageData.worldDataList;

            if (configs != null && !configs.isEmpty() && randomDataList != null && !randomDataList.isEmpty() && worldDataList != null && !worldDataList.isEmpty())
            {
                List<StoringScriptData.RandomData> filteredRandomData = IntStream.range(0, configs.size())
                        .filter(i -> i < generalStorageData.entityDescriptionsList.size())
                        .filter(i -> generalStorageData.entityDescriptionsList.get(i) != null &&
                                fullEntityType.equals(generalStorageData.entityDescriptionsList.get(i).entityType))
                        .mapToObj(randomDataList::get)
                        .collect(Collectors.toList());

                if (!filteredRandomData.isEmpty())
                {
                    StoringScriptData.RandomData selectedRandomData = Priority.getInstance()
                            .getConfigByPriority(filteredRandomData, UniqueField.RANDOM.self());

                    Integer selectedIndex = randomDataList.indexOf(selectedRandomData);

                    StoringScriptData.Equipment selectedConfig = configs.get(selectedIndex);
                    StoringScriptData.WorldData selectedWorldData = worldDataList.get(selectedIndex);

                    StoringScriptData.EntityDescription entityDescription = generalStorageData.entityDescriptionsList.get(selectedIndex);

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

                    Equipment.getInstance().equipEntity(event.getEntity(), selectedConfig, entityDescription, UniqueField.RANDOM.self());
                }
            }

            List<StoringScriptData.DataSupport> dataSupports = generalStorageData.dataSupportList;

            if (dataSupports != null && !dataSupports.isEmpty())
            {
                List<StoringScriptData.DataSupport> filteredDataSupports = dataSupports.stream()
                        .filter(dataSupport -> dataSupport.entityType.equals(fullEntityType))
                        .collect(Collectors.toList());

                if (!filteredDataSupports.isEmpty())
                {
                    for (StoringScriptData.DataSupport dataSupport : filteredDataSupports)
                    {
                        if (dataSupport.seeSky != null)
                        {
                            Boolean canSeeSky = event.getWorld().canBlockSeeSky(event.getEntity().getPosition());

                            if ((dataSupport.seeSky && !canSeeSky) || (!dataSupport.seeSky && canSeeSky))
                            {
                                continue;
                            }
                        }

                        if (dataSupport.potions != null && !dataSupport.potions.isEmpty())
                        {
                            Potion.getInstance().applyPotionEffects((EntityLivingBase) event.getEntity(), dataSupport.potions, random);
                        }
                    }
                }
            }
        }
    }
}