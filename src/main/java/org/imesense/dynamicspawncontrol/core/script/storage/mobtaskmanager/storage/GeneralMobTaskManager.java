package org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.storage;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.fixes.EntityId;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.data.EntityHostilityToID;
import org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.data.EntityHostilityToIdThemToId;
import org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.data.EntityHostilityToThem;
import org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.data.EntityPanicToID;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.w3c.dom.Entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class GeneralMobTaskManager
{
    private static volatile GeneralMobTaskManager _INSTANCE;

    public static GeneralMobTaskManager getInstance()
    {
        if (_INSTANCE == null)
        {
            synchronized (GeneralMobTaskManager.class)
            {
                if (_INSTANCE == null)
                {
                    _INSTANCE = new GeneralMobTaskManager();
                }
            }
        }
        return _INSTANCE;
    }

    private GeneralMobTaskManager()
    {
        CodeGeneric.printInitClassToLog(this.getClass());

        this.addEnemy = new ArrayList<>();
        this.addEnemyByIdPrefix = new ArrayList<>();
        this.addPanicByIdPrefix = new ArrayList<>();
        this.addEnemyToIdThemToId = new ArrayList<>();
    }

    public List<EntityHostilityToThem.Data> addEnemy;
    public List<EntityHostilityToID.Data> addEnemyByIdPrefix;
    public List<EntityPanicToID.Data> addPanicByIdPrefix;
    public List<EntityHostilityToIdThemToId.Data> addEnemyToIdThemToId;

    public static final EntityId FIXER = new EntityId();

    public static String fixEntityId(String id)
    {
        NBTTagCompound nbtXompound = new NBTTagCompound();

        nbtXompound.setString("id", id);
        nbtXompound = FIXER.fixTagCompound(nbtXompound);

        return nbtXompound.getString("id");
    }

    public void addEnemy(EntityHostilityToThem.Data entityHostilityToThemData)
    {
        addEnemy.add(entityHostilityToThemData);
    }

    public void addEnemyByIdPrefix(EntityHostilityToID.Data entityHostilityToIDData)
    {
        addEnemyByIdPrefix.add(entityHostilityToIDData);
    }

    public void addPanicByIdPrefix(EntityPanicToID.Data entityPanicToIDData)
    {
        addPanicByIdPrefix.add(entityPanicToIDData);
    }

    public void addEnemyToIdThemToId(EntityHostilityToIdThemToId.Data entityHostilityToIdThemToIdData)
    {
        addEnemyToIdThemToId.add(entityHostilityToIdThemToIdData);
    }

    public void applyHostility(EntityJoinWorldEvent entityJoinWorldEvent)
    {
        if (!(entityJoinWorldEvent.getEntity() instanceof EntityLiving))
        {
            return;
        }

        EntityLiving currentEntity = (EntityLiving) entityJoinWorldEvent.getEntity();

        for (EntityHostilityToThem.Data hostility : addEnemy)
        {
            for (String enemyToId : hostility.enemies_to)
            {
                for (String targetId : hostility.to_them)
                {
                    String fixedEnemyToId = this.fixEntityId(enemyToId);
                    String fixedTargetId = this.fixEntityId(targetId);

                    EntityEntry enemyToEntityEntry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(fixedEnemyToId));
                    EntityEntry targetEntityEntry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(fixedTargetId));

                    if (enemyToEntityEntry != null && targetEntityEntry != null)
                    {
                        Class<? extends Entity> enemyToEntityClass = (Class<? extends Entity>) enemyToEntityEntry.getEntityClass();
                        Class<? extends Entity> targetEntityClass = (Class<? extends Entity>) targetEntityEntry.getEntityClass();

                        if (enemyToEntityClass.isInstance(currentEntity))
                        {
                            currentEntity.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>((EntityCreature) currentEntity, targetEntityClass.asSubclass(EntityLiving.class), true));
                        }
                        else if (targetEntityClass.isInstance(currentEntity))
                        {
                            currentEntity.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>((EntityCreature) currentEntity, enemyToEntityClass.asSubclass(EntityLiving.class), true));
                        }
                    }
                }
            }
        }
    }

    public void applyHostilityByIdPrefix(EntityJoinWorldEvent entityJoinWorldEvent)
    {
        if (!(entityJoinWorldEvent.getEntity() instanceof EntityLiving))
        {
            return;
        }

        EntityLiving currentEntity = (EntityLiving) entityJoinWorldEvent.getEntity();

        for (EntityHostilityToID.Data hostility : addEnemyByIdPrefix)
        {
            String enemyIdPrefix = hostility.enemy_id;
            String[] targetIds = hostility.to_them;

            Set<Class<? extends EntityLiving>> enemyClassesSet = new HashSet<>();

            for (EntityEntry entityEntry : ForgeRegistries.ENTITIES)
            {
                if (entityEntry.getRegistryName().toString().startsWith(enemyIdPrefix))
                {
                    Class<? extends Entity> entityClass = (Class<? extends Entity>) entityEntry.getEntityClass();
                    if (entityClass != null && EntityLiving.class.isAssignableFrom(entityClass))
                    {
                        enemyClassesSet.add((Class<? extends EntityLiving>) entityClass);
                    }
                }
            }

            Set<Class<? extends EntityLiving>> targetClassesSet = new HashSet<>();

            for (String targetId : targetIds)
            {
                String fixedTargetId = fixEntityId(targetId);
                EntityEntry targetEntityEntry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(fixedTargetId));

                if (targetEntityEntry != null)
                {
                    Class<? extends Entity> targetEntityClass = (Class<? extends Entity>) targetEntityEntry.getEntityClass();
                    if (targetEntityClass != null && EntityLiving.class.isAssignableFrom(targetEntityClass))
                    {
                        targetClassesSet.add((Class<? extends EntityLiving>) targetEntityClass);
                    }
                }
            }

            if (!enemyClassesSet.isEmpty() && !targetClassesSet.isEmpty())
            {
                Class<? extends EntityLiving> currentEntityClass = currentEntity.getClass();

                if (currentEntity instanceof EntityCreature)
                {
                    if (enemyClassesSet.contains(currentEntityClass))
                    {
                        for (Class<? extends EntityLiving> targetClass : targetClassesSet)
                        {
                            currentEntity.targetTasks.addTask(5,
                                    new EntityAINearestAttackableTarget<>((EntityCreature) currentEntity,
                                            targetClass, true));
                        }
                    }
                    else if (targetClassesSet.contains(currentEntityClass))
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

    public void applyPanicByIdPrefix(EntityJoinWorldEvent entityJoinWorldEvent)
    {
        if (!(entityJoinWorldEvent.getEntity() instanceof EntityLiving))
        {
            return;
        }

        EntityLiving currentEntity = (EntityLiving) entityJoinWorldEvent.getEntity();

        for (EntityPanicToID.Data panic : addPanicByIdPrefix)
        {
            String panicIdPrefix = panic.panic_id;
            String[] panicToIds = panic.panic_to;

            Set<Class<? extends EntityLiving>> panicClassesSet = new HashSet<>();

            for (EntityEntry entityEntry : ForgeRegistries.ENTITIES)
            {
                if (entityEntry.getRegistryName().toString().startsWith(panicIdPrefix))
                {
                    Class<? extends Entity> entityClass = (Class<? extends Entity>) entityEntry.getEntityClass();
                    if (entityClass != null && EntityLiving.class.isAssignableFrom(entityClass))
                    {
                        panicClassesSet.add((Class<? extends EntityLiving>) entityClass);
                    }
                }
            }

            Set<Class<? extends EntityLiving>> panicToClassesSet = new HashSet<>();

            for (String panicToId : panicToIds)
            {
                String fixedPanicToId = fixEntityId(panicToId);
                EntityEntry panicToEntityEntry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(fixedPanicToId));

                if (panicToEntityEntry != null)
                {
                    Class<? extends Entity> panicToEntityClass = (Class<? extends Entity>) panicToEntityEntry.getEntityClass();
                    if (panicToEntityClass != null && EntityLiving.class.isAssignableFrom(panicToEntityClass))
                    {
                        panicToClassesSet.add((Class<? extends EntityLiving>) panicToEntityClass);
                    }
                }
            }

            if (!panicClassesSet.isEmpty() && !panicToClassesSet.isEmpty())
            {
                Class<? extends EntityLiving> currentEntityClass = currentEntity.getClass();

                if (currentEntity instanceof EntityCreature)
                {
                    if (panicToClassesSet.contains(currentEntityClass))
                    {
                        for (Class<? extends EntityLiving> panicClass : panicClassesSet)
                        {
                            currentEntity.tasks.addTask(1,
                                    new EntityAIAvoidEntity<>((EntityCreature) currentEntity,
                                            panicClass, 16.0F, 1.5D, 2.0D));
                        }
                    }
                }
            }
        }
    }

    public void applyHostilityToIdThemToId(EntityJoinWorldEvent entityJoinWorldEvent)
    {
        if (!(entityJoinWorldEvent.getEntity() instanceof EntityLiving))
        {
            return;
        }

        EntityLiving currentEntity = (EntityLiving) entityJoinWorldEvent.getEntity();

        for (EntityHostilityToIdThemToId.Data hostility : addEnemyToIdThemToId)
        {
            String enemyIdPrefix = hostility.enemy_id;
            String[] themIdPrefixes = hostility.them_id;

            Set<Class<? extends EntityLiving>> enemyClassesSet = new HashSet<>();

            for (EntityEntry entityEntry : ForgeRegistries.ENTITIES)
            {
                if (entityEntry.getRegistryName().toString().startsWith(enemyIdPrefix))
                {
                    Class<? extends Entity> entityClass = (Class<? extends Entity>) entityEntry.getEntityClass();
                    if (entityClass != null && EntityLiving.class.isAssignableFrom(entityClass))
                    {
                        enemyClassesSet.add((Class<? extends EntityLiving>) entityClass);
                    }
                }
            }

            Set<Class<? extends EntityLiving>> themClassesSet = new HashSet<>();

            for (String themIdPrefix : themIdPrefixes)
            {
                for (EntityEntry entityEntry : ForgeRegistries.ENTITIES)
                {
                    if (entityEntry.getRegistryName().toString().startsWith(themIdPrefix))
                    {
                        Class<? extends Entity> entityClass = (Class<? extends Entity>) entityEntry.getEntityClass();
                        if (entityClass != null && EntityLiving.class.isAssignableFrom(entityClass))
                        {
                            themClassesSet.add((Class<? extends EntityLiving>) entityClass);
                        }
                    }
                }
            }

            if (!enemyClassesSet.isEmpty() && !themClassesSet.isEmpty())
            {
                Class<? extends EntityLiving> currentEntityClass = currentEntity.getClass();

                if (currentEntity instanceof EntityCreature)
                {
                    if (enemyClassesSet.contains(currentEntityClass))
                    {
                        for (Class<? extends EntityLiving> targetClass : themClassesSet)
                        {
                            currentEntity.targetTasks.addTask(5,
                                    new EntityAINearestAttackableTarget<>((EntityCreature) currentEntity,
                                            targetClass, true));
                        }
                    }
                    else if (themClassesSet.contains(currentEntityClass))
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
}
