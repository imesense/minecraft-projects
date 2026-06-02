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
import org.imesense.dynamicspawncontrol.core.logfile.LogManager;
import org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.data.*;
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

    public FunctionalMobTaskManager()
    {

    }

    public static final EntityId ENTITY_ID = new EntityId();

    public static String fixEntityId(String id)
    {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();

        nbtTagCompound.setString("id", id);

        nbtTagCompound = ENTITY_ID.fixTagCompound(nbtTagCompound);

        return nbtTagCompound.getString("id");
    }

    private boolean checkDimension(Entity entity, Integer ruleDimension)
    {
        if (ruleDimension == null)
        {
            return true;
        }

        int currentDimension = entity.world.provider.getDimension();
        boolean isValid = currentDimension == ruleDimension;

        if (!isValid)
        {
            LogManager.info(String.format("[MobTask] Skipping rule: dimension mismatch (need %d, got %d)",
                    ruleDimension, currentDimension));
        }

        return isValid;
    }

    public void processAddEnemyData(EntityJoinWorldEvent event)
    {
        EntityLiving currentEntity = (EntityLiving) event.getEntity();
        int currentDimension = currentEntity.world.provider.getDimension();

        for (AddEnemy.Data data : GeneralMobTaskManager.getInstance().addEnemyData)
        {
            if (!checkDimension(currentEntity, data.idDimension))
            {
                continue;
            }

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

                            //Log.write(0, String.format(
                            //        "[MobTask] AddEnemy: %s -> %s in dimension %d",
                            //        enemyToId, targetId, currentDimension
                            //));
                        }
                        else if (targetEntityClass.isInstance(currentEntity))
                        {
                            currentEntity.targetTasks.addTask(5,
                                    new EntityAINearestAttackableTarget<>((EntityCreature) currentEntity,
                                            enemyToEntityClass.asSubclass(EntityLiving.class), true));

                            //Log.write(0, String.format(
                            //        "[MobTask] AddEnemy reverse: %s -> %s in dimension %d",
                            //        targetId, enemyToId, currentDimension
                            //));
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

                            //Log.write(0, String.format(
                            //        "[MobTask] AddEnemy multi: %s enemies -> %d targets in dimension %d",
                            //        currentEntity.getClass().getSimpleName(),
                            //        targetClassesSet.size(),
                            //        currentDimension
                            //));
                        }
                        else if (targetClassesSet.contains(entityClass))
                        {
                            for (Class<? extends EntityLiving> enemyClass : enemyClassesSet)
                            {
                                currentEntity.targetTasks.addTask(5,
                                        new EntityAINearestAttackableTarget<>((EntityCreature) currentEntity,
                                                enemyClass, true));
                            }

                            //Log.write(0, String.format(
                            //        "[MobTask] AddEnemy multi reverse: %s target -> %d enemies in dimension %d",
                            //        currentEntity.getClass().getSimpleName(),
                            //        enemyClassesSet.size(),
                            //        currentDimension
                            //));
                        }
                    }
                }
            }
        }
    }

    public void processAddEnemyIdData(EntityJoinWorldEvent event)
    {
        EntityLiving currentEntity = (EntityLiving) event.getEntity();
        int currentDimension = currentEntity.world.provider.getDimension();

        for (AddEnemyId.Data data : GeneralMobTaskManager.getInstance().addEnemyIdData)
        {
            if (!checkDimension(currentEntity, data.idDimension))
            {
                continue;
            }

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

                        //Log.write(0, String.format(
                        //        "[MobTask] AddEnemyId: %s -> %d prefix targets in dimension %d",
                        //        currentEntity.getClass().getSimpleName(),
                        //        enemyIdClassesSet.size(),
                        //        currentDimension
                        //));
                    }
                    else if (enemyIdClassesSet.contains(currentEntityClass))
                    {
                        for (Class<? extends EntityLiving> targetClass : enemyClassesSet)
                        {
                            currentEntity.targetTasks.addTask(5,
                                    new EntityAINearestAttackableTarget<>((EntityCreature) currentEntity,
                                            targetClass, true));
                        }

                        //Log.write(0, String.format(
                        //        "[MobTask] AddEnemyId reverse: %s -> %d specific enemies in dimension %d",
                        //        currentEntity.getClass().getSimpleName(),
                        //        enemyClassesSet.size(),
                        //        currentDimension
                        //));
                    }
                }
            }
        }
    }

    public void processAddPanicToIdData(EntityJoinWorldEvent event)
    {
        EntityLiving currentEntity = (EntityLiving) event.getEntity();
        int currentDimension = currentEntity.world.provider.getDimension();

        for (AddPanicToId.Data data : GeneralMobTaskManager.getInstance().addPanicToIdData)
        {
            if (!checkDimension(currentEntity, data.idDimension))
            {
                continue;
            }

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

                    //Log.write(0, String.format(
                    //        "[MobTask] AddPanic: %s fears %d entities in dimension %d",
                    //        currentEntity.getClass().getSimpleName(),
                    //        panicIdClassesSet.size(),
                    //        currentDimension
                    //));
                }
            }
        }
    }

    public void processAddEnemyToIdThemToIdData(EntityJoinWorldEvent event)
    {
        EntityLiving currentEntity = (EntityLiving) event.getEntity();
        int currentDimension = currentEntity.world.provider.getDimension();

        for (AddEnemyToIdThemToId.Data data : GeneralMobTaskManager.getInstance().addEnemyToIdThemToIdData)
        {
            if (!checkDimension(currentEntity, data.idDimension))
            {
                continue;
            }

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

                            //Log.write(0, String.format(
                            //        "[MobTask] EnemyToId: %s -> %d them targets in dimension %d",
                            //        currentEntity.getClass().getSimpleName(),
                            //        themIdClassesSet.size(),
                            //        currentDimension
                            //));
                        }

                        if (themIdClassesSet.contains(currentEntityClass))
                        {
                            for (Class<? extends EntityLiving> targetClass : enemyIdClassesSet)
                            {
                                currentEntity.targetTasks.addTask(5,
                                        new EntityAINearestAttackableTarget<>((EntityCreature) currentEntity,
                                                targetClass, true));
                            }

                            //Log.write(0, String.format(
                            //        "[MobTask] EnemyToId reverse: %s -> %d enemy targets in dimension %d",
                            //        currentEntity.getClass().getSimpleName(),
                            //        enemyIdClassesSet.size(),
                            //        currentDimension
                            //));
                        }
                    }
                }
            }
        }
    }

    private Boolean canEntityAttack(EntityLiving entity)
    {
        return (entity instanceof EntityMob ||
                (entity instanceof EntityAnimal && entity.getAttackTarget() != null));
    }
}
