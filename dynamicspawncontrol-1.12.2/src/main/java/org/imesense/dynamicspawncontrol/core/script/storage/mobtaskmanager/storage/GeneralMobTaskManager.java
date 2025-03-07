package org.imesense.dynamicspawncontrol.core.script.storage.mobtaskmanager.storage;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.fixes.EntityId;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.w3c.dom.Entity;

import java.util.ArrayList;
import java.util.List;

public final class GeneralMobTaskManager
{
    private static volatile GeneralMobTaskManager _INSTANCE;

    public static GeneralMobTaskManager getInstance()
    {
        if (_INSTANCE == null)
        {
            synchronized (GeneralMobTaskManager.class)
            {
                if (_INSTANCE == null) {
                    _INSTANCE = new GeneralMobTaskManager();
                }
            }
        }
        return _INSTANCE;
    }

    private GeneralMobTaskManager()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
    }

    public static final class EntityHostilityToThem
    {
        public String[] enemies_to;
        public String[] to_them;
    }

    private List<EntityHostilityToThem> addEnemy = new ArrayList<>();

    public void addEnemy(EntityHostilityToThem hostility)
    {
        addEnemy.add(hostility);
    }

    public void applyHostility(EntityJoinWorldEvent event)
    {
        if (!(event.getEntity() instanceof EntityLiving))
        {
            return;
        }

        EntityLiving currentEntity = (EntityLiving) event.getEntity();

        for (EntityHostilityToThem hostility : addEnemy)
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

    public static final EntityId FIXER = new EntityId();

    public static String fixEntityId(String id)
    {
        NBTTagCompound nbtXompound = new NBTTagCompound();

        nbtXompound.setString("id", id);

        nbtXompound = FIXER.fixTagCompound(nbtXompound);

        return nbtXompound.getString("id");
    }
}
