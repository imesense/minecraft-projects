package org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.functional;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.fixes.EntityId;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.data.AddEnemy;
import org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.data.AddEnemyId;
import org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.data.AddEnemyToIdThemToId;
import org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.data.AddPanicToId;
import org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.storage.GeneralMobTaskManager;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.HashSet;
import java.util.Set;

public final class FunctionalMobTaskManager
{
    private static volatile FunctionalMobTaskManager _INSTANCE;

    public static FunctionalMobTaskManager getInstance()
    {
        return CodeGeneric.getInstance(FunctionalMobTaskManager.class);
    }

    public static final EntityId FIXER = new EntityId();

    public static String fixEntityId(String id)
    {
        NBTTagCompound nbtXompound = new NBTTagCompound();

        nbtXompound.setString("id", id);

        nbtXompound = FIXER.fixTagCompound(nbtXompound);

        return nbtXompound.getString("id");
    }

    public void processAddEnemyData(EntityJoinWorldEvent event)
    {
        EntityLiving currentEntity = (EntityLiving) event.getEntity();

        for (AddEnemy.Data data : GeneralMobTaskManager.getInstance().addEnemyData)
        {
            String[] enemiesTo = data.enemies_to;
            String[] toThem = data.to_them;

            if (enemiesTo.length == 1 && toThem.length == 1)
            {
                String enemyToId = enemiesTo[0];
                String targetId = toThem[0];

                String fixedEnemyToId = fixEntityId(enemyToId);
                String fixedTargetId = fixEntityId(targetId);

                EntityEntry enemyToEntityEntry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(fixedEnemyToId));
                EntityEntry targetEntityEntry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(fixedTargetId));

                Class<? extends Entity> enemyToEntityClass = enemyToEntityEntry == null ? null : enemyToEntityEntry.getEntityClass();
                Class<? extends Entity> targetEntityClass = targetEntityEntry == null ? null : targetEntityEntry.getEntityClass();

                if (enemyToEntityClass != null && targetEntityClass != null)
                {
                    if (currentEntity instanceof EntityCreature)
                    {
                        if (enemyToEntityClass.isInstance(currentEntity))
                        {
                            currentEntity.targetTasks.addTask(5,
                                    new EntityAINearestAttackableTarget<>((EntityCreature) currentEntity,
                                            targetEntityClass.asSubclass(EntityLiving.class), true));
                        }
                        else if (targetEntityClass.isInstance(currentEntity))
                        {
                            currentEntity.targetTasks.addTask(5,
                                    new EntityAINearestAttackableTarget<>((EntityCreature) currentEntity,
                                            enemyToEntityClass.asSubclass(EntityLiving.class), true));
                        }
                    }
                }
            }
            else
            {
                Set<Class<? extends EntityLiving>> enemyClassesSet = new HashSet<>();
                Set<Class<? extends EntityLiving>> targetClassesSet = new HashSet<>();

                for (String enemyToId : enemiesTo)
                {
                    String fixedEnemyToId = fixEntityId(enemyToId);
                    EntityEntry enemyToEntityEntry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(fixedEnemyToId));

                    Class<? extends Entity> enemyToEntityClass = enemyToEntityEntry == null ? null : enemyToEntityEntry.getEntityClass();

                    if (enemyToEntityClass != null)
                    {
                        enemyClassesSet.add((Class<? extends EntityLiving>) enemyToEntityClass);
                    }
                }

                for (String targetId : toThem)
                {
                    String fixedTargetId = fixEntityId(targetId);
                    EntityEntry targetEntityEntry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(fixedTargetId));

                    Class<? extends Entity> targetEntityClass = targetEntityEntry == null ? null : targetEntityEntry.getEntityClass();

                    if (targetEntityClass != null)
                    {
                        targetClassesSet.add((Class<? extends EntityLiving>) targetEntityClass);
                    }
                }

                if (!enemyClassesSet.isEmpty() && !targetClassesSet.isEmpty())
                {
                    if (currentEntity instanceof EntityCreature)
                    {
                        Class<? extends EntityLiving> entityClass = currentEntity.getClass();

                        if (enemyClassesSet.contains(entityClass))
                        {
                            for (Class<? extends EntityLiving> targetClass : targetClassesSet)
                            {
                                currentEntity.targetTasks.addTask(5,
                                        new EntityAINearestAttackableTarget<>((EntityCreature) currentEntity,
                                                targetClass, true));
                            }
                        }
                        else if (targetClassesSet.contains(entityClass))
                        {
                            for (Class<? extends EntityLiving> enemyClass : enemyClassesSet)
                            {
                                currentEntity.targetTasks.addTask(5,
                                        new EntityAINearestAttackableTarget<>((EntityCreature) currentEntity,
                                                enemyClass, true));
                            }
                        }
                    }
                }
            }
        }
    }

    public void processAddEnemyIdData(EntityJoinWorldEvent event)
    {
        EntityLiving currentEntity = (EntityLiving) event.getEntity();

        for (AddEnemyId.Data data : GeneralMobTaskManager.getInstance().addEnemyIdData)
        {
            String[] enemiesTo = data.enemies_to;
            String[] enemyIdPrefixes = data.enemy_id;

            Set<Class<? extends EntityLiving>> enemyClassesSet = new HashSet<>();
            Set<Class<? extends EntityLiving>> enemyIdClassesSet = new HashSet<>();

            for (String enemyToId : enemiesTo)
            {
                String fixedEnemyToId = fixEntityId(enemyToId);
                EntityEntry enemyToEntityEntry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(fixedEnemyToId));

                Class<? extends Entity> enemyToEntityClass = enemyToEntityEntry == null ? null : enemyToEntityEntry.getEntityClass();

                if (enemyToEntityClass != null && EntityLiving.class.isAssignableFrom(enemyToEntityClass))
                {
                    enemyClassesSet.add((Class<? extends EntityLiving>) enemyToEntityClass);
                }
            }

            for (String enemyIdPrefix : enemyIdPrefixes)
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
                if (currentEntity instanceof EntityCreature)
                {
                    Class<? extends EntityLiving> currentEntityClass = currentEntity.getClass();

                    if (enemyClassesSet.contains(currentEntityClass))
                    {
                        for (Class<? extends EntityLiving> targetClass : enemyIdClassesSet)
                        {
                            currentEntity.targetTasks.addTask(5,
                                    new EntityAINearestAttackableTarget<>((EntityCreature) currentEntity,
                                            targetClass, true));
                        }
                    }
                    else if (enemyIdClassesSet.contains(currentEntityClass))
                    {
                        for (Class<? extends EntityLiving> targetClass : enemyClassesSet)
                        {
                            currentEntity.targetTasks.addTask(5,
                                    new EntityAINearestAttackableTarget<>((EntityCreature) currentEntity,
                                            targetClass, true));
                        }
                    }
                }
            }
        }
    }

    public void processAddPanicToIdData(EntityJoinWorldEvent event)
    {
        EntityLiving currentEntity = (EntityLiving) event.getEntity();

        for (AddPanicToId.Data data : GeneralMobTaskManager.getInstance().addPanicToIdData)
        {
            String[] panicTo = data.panic_to;
            String[] panicIdPrefixes = data.panic_id;

            Set<Class<? extends EntityLiving>> panicToClassesSet = new HashSet<>();
            Set<Class<? extends EntityLiving>> panicIdClassesSet = new HashSet<>();

            for (String panicToId : panicTo)
            {
                String fixedPanicToId = fixEntityId(panicToId);
                EntityEntry panicToEntityEntry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(fixedPanicToId));

                Class<? extends Entity> panicToEntityClass = panicToEntityEntry == null ? null : panicToEntityEntry.getEntityClass();

                if (panicToEntityClass != null && EntityLiving.class.isAssignableFrom(panicToEntityClass))
                {
                    panicToClassesSet.add((Class<? extends EntityLiving>) panicToEntityClass);
                }
            }

            for (String panicIdPrefix : panicIdPrefixes)
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
                Class<? extends EntityLiving> currentEntityClass = currentEntity.getClass();

                if (panicToClassesSet.contains(currentEntityClass))
                {
                    for (Class<? extends EntityLiving> panicClass : panicIdClassesSet)
                    {
                        currentEntity.tasks.addTask(1,
                                new EntityAIAvoidEntity<>((EntityCreature) currentEntity,
                                        panicClass, 16.0F, 1.5D, 2.0D));
                    }
                }
            }
        }
    }

    public void processAddEnemyToIdThemToIdData(EntityJoinWorldEvent event)
    {
        EntityLiving currentEntity = (EntityLiving) event.getEntity();

        for (AddEnemyToIdThemToId.Data data : GeneralMobTaskManager.getInstance().addEnemyToIdThemToIdData)
        {
            String[] enemyIdPrefixes = data.enemy_id;
            String[] themIdPrefixes = data.them_id;

            Set<Class<? extends EntityLiving>> enemyIdClassesSet = new HashSet<>();
            Set<Class<? extends EntityLiving>> themIdClassesSet = new HashSet<>();

            for (String enemyIdPrefix : enemyIdPrefixes)
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

            for (String themIdPrefix : themIdPrefixes)
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
                if (currentEntity instanceof EntityCreature)
                {
                    Class<? extends EntityLiving> currentEntityClass = currentEntity.getClass();

                    if (canEntityAttack(currentEntity))
                    {
                        if (enemyIdClassesSet.contains(currentEntityClass))
                        {
                            for (Class<? extends EntityLiving> targetClass : themIdClassesSet)
                            {
                                currentEntity.targetTasks.addTask(5,
                                        new EntityAINearestAttackableTarget<>((EntityCreature) currentEntity,
                                                targetClass, true));
                            }
                        }

                        if (themIdClassesSet.contains(currentEntityClass))
                        {
                            for (Class<? extends EntityLiving> targetClass : enemyIdClassesSet)
                            {
                                currentEntity.targetTasks.addTask(5,
                                        new EntityAINearestAttackableTarget<>((EntityCreature) currentEntity,
                                                targetClass, true));
                            }
                        }
                    }
                    else
                    {
						// TODO: Это надо сделать на опцию дебаг лога
                        //Log.writeDataToLogFile(1, "Entity " + currentEntity.getClass().getSimpleName() + " cannot attack. Skipping task assignment.");
                    }
                }
            }
        }
    }

    private boolean canEntityAttack(EntityLiving entity)
    {
        return entity instanceof EntityMob || (entity instanceof EntityAnimal && ((EntityAnimal) entity).getAttackTarget() != null);
    }
}
