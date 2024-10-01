package org.imesense.dynamicspawncontrol.technical.customlibrary;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeMap;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.generic.GenericPotentialSpawn;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.signal.SignalDataGetter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static org.imesense.dynamicspawncontrol.technical.customlibrary.MultipleKeyWords.MobTaskManager.*;

/**
 *
 * @param <T>
 */
public abstract class ListActionsConsumerMobTaskManager<T extends SignalDataGetter>
{
    /**
     *
     */
    protected final List<Consumer<T>> ACTIONS = new ArrayList<>();

    /**
     *
     */
    public ListActionsConsumerMobTaskManager()
    {

    }

    /**
     *
     * @param map
     */
    protected void addActions(AttributeMap<?> map)
    {
        /**
         *
         */
        final boolean hasEnemiesTo = map.has(ENEMIES_TO);
        final boolean hasToThem = map.has(TO_THEM);

        /**
         *
         */
        final boolean hasEnemyId = map.has(ENEMY_ID);
        final boolean hasThemId = map.has(THEM_ID);

        /**
         *
         */
        final boolean hasPanicTo = map.has(PANIC_TO);
        final boolean hasPanicId = map.has(PANIC_ID);

        try
        {
            if (hasEnemiesTo && hasToThem)
            {
                this.addEnemy(map);
            }
            else if (hasEnemiesTo && hasEnemyId)
            {
                this.addEnemyId(map);
            }
            else if (hasPanicTo && hasPanicId)
            {
                this.addPanicToId(map);
            }
            else if (hasEnemyId && hasThemId)
            {
                this.addEnemyToIdThemToId(map);
            }
            else
            {
                Log.writeDataToLogFile(2, "None of the required conditions were met in addActions.");
                throw new IllegalArgumentException("Error: Invalid attribute map configuration in addActions. Required conditions not met.");
            }
        }
        catch (Exception exception)
        {
            Log.writeDataToLogFile(2, "Exception: " + exception.getMessage());
            throw exception;
        }
    }

    /**
     *
     * @param map
     */
    private void addEnemy(AttributeMap<?> map)
    {
        List<String> enemiesToList = map.getList(ENEMIES_TO);
        List<String> targetList = map.getList(TO_THEM);

        if (enemiesToList.size() == 1 && targetList.size() == 1)
        {
            String enemyToId = enemiesToList.get(0);
            String targetId = targetList.get(0);

            String fixedEnemyToId = GenericPotentialSpawn.fixEntityId(enemyToId);
            String fixedTargetId = GenericPotentialSpawn.fixEntityId(targetId);

            EntityEntry enemyToEntityEntry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(fixedEnemyToId));
            EntityEntry targetEntityEntry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(fixedTargetId));

            Class<? extends Entity> enemyToEntityClass = enemyToEntityEntry == null ? null : enemyToEntityEntry.getEntityClass();
            Class<? extends Entity> targetEntityClass = targetEntityEntry == null ? null : targetEntityEntry.getEntityClass();

            if (enemyToEntityClass != null && targetEntityClass != null)
            {
                this.ACTIONS.add(event ->
                {
                    EntityLiving currentEntity = (EntityLiving) event.getEntityLiving();

                    if (enemyToEntityClass.isInstance(currentEntity))
                    {
                        currentEntity.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>((EntityCreature) currentEntity, targetEntityClass.asSubclass(EntityLiving.class), true));
                    }
                    else if (targetEntityClass.isInstance(currentEntity))
                    {
                        currentEntity.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>((EntityCreature) currentEntity, enemyToEntityClass.asSubclass(EntityLiving.class), true));
                    }
                });
            }
        }
        else
        {
            Set<Class<? extends EntityLiving>> enemyClassesSet = new HashSet<>();
            Set<Class<? extends EntityLiving>> targetClassesSet = new HashSet<>();

            for (String enemyToId : enemiesToList)
            {
                String fixedEnemyToId = GenericPotentialSpawn.fixEntityId(enemyToId);

                EntityEntry enemyToEntityEntry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(fixedEnemyToId));
                Class<? extends Entity> enemyToEntityClass = enemyToEntityEntry == null ? null : enemyToEntityEntry.getEntityClass();

                if (enemyToEntityClass != null)
                {
                    enemyClassesSet.add((Class<? extends EntityLiving>) enemyToEntityClass);
                }
            }

            for (String targetId : targetList)
            {
                String fixedTargetId = GenericPotentialSpawn.fixEntityId(targetId);

                EntityEntry targetEntityEntry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(fixedTargetId));
                Class<? extends Entity> targetEntityClass = targetEntityEntry == null ? null : targetEntityEntry.getEntityClass();

                if (targetEntityClass != null)
                {
                    targetClassesSet.add((Class<? extends EntityLiving>) targetEntityClass);
                }
            }

            if (!enemyClassesSet.isEmpty() && !targetClassesSet.isEmpty())
            {
                this.ACTIONS.add(event ->
                {
                    EntityLiving currentEntity = (EntityLiving) event.getEntityLiving();
                    Class<? extends EntityLiving> entityClass = currentEntity.getClass();

                    if (enemyClassesSet.contains(entityClass))
                    {
                        for (Class<? extends EntityLiving> targetClass : targetClassesSet)
                        {
                            currentEntity.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>((EntityCreature) currentEntity, targetClass, true));
                        }
                    }
                    else if (targetClassesSet.contains(entityClass))
                    {
                        for (Class<? extends EntityLiving> enemyClass : enemyClassesSet)
                        {
                            currentEntity.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>((EntityCreature) currentEntity, enemyClass, true));
                        }
                    }
                });
            }
        }
    }

    /**
     *
     * @param map
     */
    private void addEnemyId(AttributeMap<?> map)
    {
        List<String> enemiesToList = map.getList(ENEMIES_TO);
        List<String> enemyIdPrefixList = map.getList(ENEMY_ID);

        Set<Class<? extends EntityLiving>> enemyClassesSet = new HashSet<>();
        Set<Class<? extends EntityLiving>> enemyIdClassesSet = new HashSet<>();

        for (String enemyToId : enemiesToList)
        {
            String fixedEnemyToId = GenericPotentialSpawn.fixEntityId(enemyToId);
            EntityEntry enemyToEntityEntry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(fixedEnemyToId));
            Class<? extends Entity> enemyToEntityClass = enemyToEntityEntry == null ? null : enemyToEntityEntry.getEntityClass();

            if (enemyToEntityClass != null && EntityLiving.class.isAssignableFrom(enemyToEntityClass))
            {
                enemyClassesSet.add((Class<? extends EntityLiving>) enemyToEntityClass);
            }
        }

        for (String enemyIdPrefix : enemyIdPrefixList)
        {
            for (EntityEntry entityEntry : ForgeRegistries.ENTITIES)
            {
                if (entityEntry.getRegistryName().toString().startsWith(enemyIdPrefix))
                {
                    Class<? extends Entity> entityClassFromRegistry = entityEntry.getEntityClass();

                    if (entityClassFromRegistry != null && EntityLiving.class.isAssignableFrom(entityClassFromRegistry))
                    {
                        enemyIdClassesSet.add((Class<? extends EntityLiving>) entityClassFromRegistry);
                    }
                }
            }
        }

        if (!enemyClassesSet.isEmpty() && !enemyIdClassesSet.isEmpty())
        {
            this.ACTIONS.add(event ->
            {
                EntityLiving currentEntity = (EntityLiving) event.getEntityLiving();
                Class<? extends EntityLiving> currentEntityClass = currentEntity.getClass();

                if (enemyClassesSet.contains(currentEntityClass))
                {
                    for (Class<? extends EntityLiving> targetClass : enemyIdClassesSet)
                    {
                        currentEntity.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>((EntityCreature) currentEntity, targetClass, true));
                    }
                }
                else if (enemyIdClassesSet.contains(currentEntityClass))
                {
                    for (Class<? extends EntityLiving> targetClass : enemyClassesSet)
                    {
                        currentEntity.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>((EntityCreature) currentEntity, targetClass, true));
                    }
                }
            });
        }
    }

    /**
     *
     * @param map
     */
    private void addPanicToId(AttributeMap<?> map)
    {
        List<String> panicToList = map.getList(PANIC_TO);
        List<String> panicIdPrefixList = map.getList(PANIC_ID);

        Set<Class<? extends EntityLiving>> panicToClassesSet = new HashSet<>();
        Set<Class<? extends EntityLiving>> panicIdClassesSet = new HashSet<>();

        for (String panicToId : panicToList)
        {
            String fixedPanicToId = GenericPotentialSpawn.fixEntityId(panicToId);

            EntityEntry panicToEntityEntry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(fixedPanicToId));

            Class<? extends Entity> panicToEntityClass = panicToEntityEntry == null ? null : panicToEntityEntry.getEntityClass();

            if (panicToEntityClass != null && EntityLiving.class.isAssignableFrom(panicToEntityClass))
            {
                panicToClassesSet.add((Class<? extends EntityLiving>) panicToEntityClass);
            }
        }

        for (String panicIdPrefix : panicIdPrefixList)
        {
            for (EntityEntry entityEntry : ForgeRegistries.ENTITIES)
            {
                if (entityEntry.getRegistryName().toString().startsWith(panicIdPrefix))
                {
                    Class<? extends Entity> panicIdEntityClass = entityEntry.getEntityClass();

                    if (panicIdEntityClass != null && EntityLiving.class.isAssignableFrom(panicIdEntityClass))
                    {
                        panicIdClassesSet.add((Class<? extends EntityLiving>) panicIdEntityClass);
                    }
                }
            }
        }

        if (!panicToClassesSet.isEmpty() && !panicIdClassesSet.isEmpty())
        {
            this.ACTIONS.add(event ->
            {
                EntityLiving currentEntity = (EntityLiving) event.getEntityLiving();
                Class<? extends EntityLiving> currentEntityClass = currentEntity.getClass();

                if (panicToClassesSet.contains(currentEntityClass))
                {
                    for (Class<? extends EntityLiving> panicClass : panicIdClassesSet)
                    {
                        currentEntity.tasks.addTask(1, new EntityAIAvoidEntity<>((EntityCreature) currentEntity, panicClass, 16.0F, 1.5D, 2.0D));
                    }
                }
            });
        }
    }

    /**
     *
     * @param map
     */
    private void addEnemyToIdThemToId(AttributeMap<?> map)
    {
        List<String> enemyIdList = map.getList(ENEMY_ID);
        List<String> themIdList = map.getList(THEM_ID);

        Set<Class<? extends EntityLiving>> enemyIdClassesSet = new HashSet<>();
        Set<Class<? extends EntityLiving>> themIdClassesSet = new HashSet<>();

        for (String enemyIdPrefix : enemyIdList)
        {
            for (EntityEntry entityEntry : ForgeRegistries.ENTITIES)
            {
                if (entityEntry.getRegistryName().toString().startsWith(enemyIdPrefix))
                {
                    Class<? extends Entity> enemyEntityClass = entityEntry.getEntityClass();

                    if (enemyEntityClass != null && EntityLiving.class.isAssignableFrom(enemyEntityClass))
                    {
                        enemyIdClassesSet.add((Class<? extends EntityLiving>) enemyEntityClass);
                    }
                }
            }
        }

        for (String themIdPrefix : themIdList)
        {
            for (EntityEntry entityEntry : ForgeRegistries.ENTITIES)
            {
                if (entityEntry.getRegistryName().toString().startsWith(themIdPrefix))
                {
                    Class<? extends Entity> themEntityClass = entityEntry.getEntityClass();

                    if (themEntityClass != null && EntityLiving.class.isAssignableFrom(themEntityClass))
                    {
                        themIdClassesSet.add((Class<? extends EntityLiving>) themEntityClass);
                    }
                }
            }
        }

        if (!enemyIdClassesSet.isEmpty() && !themIdClassesSet.isEmpty())
        {
            this.ACTIONS.add(event ->
            {
                EntityLiving currentEntity = (EntityLiving) event.getEntityLiving();
                Class<? extends EntityLiving> currentEntityClass = currentEntity.getClass();

                if (enemyIdClassesSet.contains(currentEntityClass))
                {
                    for (Class<? extends EntityLiving> targetClass : themIdClassesSet)
                    {
                        currentEntity.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>((EntityCreature) currentEntity, targetClass, true));
                    }
                }
                else if (themIdClassesSet.contains(currentEntityClass))
                {
                    for (Class<? extends EntityLiving> targetClass : enemyIdClassesSet)
                    {
                        currentEntity.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>((EntityCreature) currentEntity, targetClass, true));
                    }
                }
            });
        }
    }
}
