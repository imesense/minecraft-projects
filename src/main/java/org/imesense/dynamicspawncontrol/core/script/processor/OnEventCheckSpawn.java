package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.script.actioncollector.*;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.script.storage.checkspawn.data.*;
import org.imesense.dynamicspawncontrol.core.script.storage.checkspawn.datasupport.AdditionalChecks;
import org.imesense.dynamicspawncontrol.core.script.storage.checkspawn.storage.GeneralCheckSpawnStorage;
import org.imesense.dynamicspawncontrol.core.script.storage.checkspawn.storagesupport.SupportCheckSpawnStorage;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@InitLog
@TODO(value = "START REWORKING PARSERS WITH THIS SCRIPT! Optimization is broken, besides redoing the class in the diagram, + " +
        "добавить новые параметры в дебаг", showOnce = false, priority = TODO.TodoPriority.HIGH)
public final class OnEventCheckSpawn
{
    private static volatile OnEventCheckSpawn _INSTANCE;

    public static OnEventCheckSpawn getInstance()
    {
        return CodeGeneric.getInstance(OnEventCheckSpawn.class);
    }

    public OnEventCheckSpawn()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    ///
    private static final List<String> ALLOWED_ENTITIES = Arrays.asList(
        "minecraft:bat"
    );
    ///

    private EntityAttributes.Data cloneWithoutPotion(EntityAttributes.Data src)
    {
        EntityAttributes.Data d = new EntityAttributes.Data();
        d.commandNbt = src.commandNbt;
        d.commandNbtChance = src.commandNbtChance;
        return d;
    }

    private EntityAttributes.Data cloneWithoutCommandNBT(EntityAttributes.Data src)
    {
        EntityAttributes.Data d = new EntityAttributes.Data();
        d.potion = src.potion;
        d.potionChance = src.potionChance;
        return d;
    }

    public void handleLivingSpawnEventCheckSpawn(LivingSpawnEvent.CheckSpawn event)
    {
        // для дебага в 0.2 версии
        if (DisableEventBooleansTest.test == false)
        {
            // Пропускаем спавн из спаунеров
            if (event.getSpawner() != null) {
                return;
            }

            // Пропускаем события, которые уже отменены
            if (event.getResult() == Event.Result.DENY) {
                return;
            }

            Entity entity = event.getEntity();
            net.minecraft.world.World world = event.getWorld();

            // Получаем ID сущности
            ResourceLocation entityId = EntityList.getKey(entity);

            if (entityId == null) {
                return;
            }

            String entityIdString = entityId.toString();

            // Проверяем, разрешена ли сущность
            if (!ALLOWED_ENTITIES.contains(entityIdString)) {
                event.setResult(Event.Result.DENY);

                // Логируем блокировку (опционально)
                if (world.isRemote) {
                    System.out.println("Blocked spawn of: " + entityIdString + " at " +
                            entity.posX + ", " + entity.posY + ", " + entity.posZ);
                }
            }

            if (DisableEventBooleansTest.test)
                return;
        }

        /**
         * В чем тут мем, то что у нас entityType проверяется на каждую сущность, на каждую сущность открывается файл
         * Отсюда идут дикие просадки FPS, нужно переделать это на кеширование с использованием хард-сущности (У нас есть список, с ним работаем)
         * То что проверяем только те сущности, которые у нас указаны в файле спавна в целом
         * Например у нас указан "minecraft:zombie" мы сравниваем его минимально с entityType и смотрим что у нас указано
         * В массиве файла, допустим [сущность: "Зомби", его index профиля и уже этот профиль в готовом виде отправляем в событие]
         * Чтобы не уничтожать фпс в 0
         * Работаем по index и сразу применяем готовый профиль без поиска его по файлу КАЖДЫЙ КАДР!!!
         */
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

                    if (selectedWorldData.idDimension != null)
                    {
                        int currentDimension = event.getWorld().provider.getDimension();

                        if (currentDimension != selectedWorldData.idDimension)
                        {
                            return;
                        }
                    }

                    if (entityDescription != null && entityDescription.name != null)
                    {
                        event.getEntity().setCustomNameTag(entityDescription.name);
                        event.getEntity().setAlwaysRenderNameTag(true);
                    }

                    EntityAttributes.Data finalAttributes = entityAttributes;

                    if (entityAttributes.potion != null)
                    {
                        double chance = entityAttributes.potionChance != null
                                ? entityAttributes.potionChance
                                : 1.0;

                        if (Math.random() > chance)
                        {
                            finalAttributes = cloneWithoutPotion(entityAttributes);
                        }
                    }

                    if (finalAttributes.commandNbt != null)
                    {
                        double chance = finalAttributes.commandNbtChance != null
                                ? finalAttributes.commandNbtChance
                                : 1.0;

                        if (Math.random() > chance)
                        {
                            finalAttributes = cloneWithoutCommandNBT(finalAttributes);
                        }
                    }

                    Equipment.getInstance().equipEntity(event.getEntity(), selectedConfig, entityDescription, finalAttributes, UniqueField.RANDOM.self());
                }
            }

            List<AdditionalChecks.Data> dataSupports = supportStorageScriptData.dataSupportList;

            if (dataSupports != null && !dataSupports.isEmpty())
            {
                List<AdditionalChecks.Data> filteredDataSupports = dataSupports.stream()
                        .filter(dataSupport -> entityType.equals(dataSupport.entityType))
                        .collect(Collectors.toList());

                if (!filteredDataSupports.isEmpty())
                {
                    for (AdditionalChecks.Data dataSupport : filteredDataSupports)
                    {
                        if (dataSupport.seeSky != null)
                        {
                            Boolean canSeeSky = event.getWorld().canBlockSeeSky(event.getEntity().getPosition());

                            if ((dataSupport.seeSky && !canSeeSky) || (!dataSupport.seeSky && canSeeSky))
                            {
                                continue;
                            }
                        }

                        if (dataSupport.idDimension != null)
                        {
                            int currentDimension = event.getWorld().provider.getDimension();

                            if (currentDimension != dataSupport.idDimension)
                            {
                                continue;
                            }
                        }

                        if (dataSupport.potion != null && !dataSupport.potion.isEmpty())
                        {
                            Potion.getInstance().applyPotionEffects((EntityLivingBase) event.getEntity(),
                                    dataSupport.potion, UniqueField.RANDOM.self());
                        }
                    }
                }
            }
        }
    }
}