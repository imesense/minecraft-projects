package org.imesense.dynamicspawncontrol.technical.customlibrary;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeKey;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeMap;
import org.imesense.dynamicspawncontrol.technical.configs.ConfigDebugSingleEvents;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.signal.SignalDataGetter;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.generic.GenericOverrideSpawn;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.imesense.dynamicspawncontrol.technical.customlibrary.MultipleKeyWords.CommonKeyWorlds.*;
import static org.imesense.dynamicspawncontrol.technical.customlibrary.MultipleKeyWords.MobsTaskManager.*;

/**
 *
 * @param <T>
 */
public abstract class ListActionsConsumer<T extends SignalDataGetter>
{
    /**
     *
     */
    protected final List<Consumer<T>> ACTIONS = new ArrayList<>();

    /**
     *
     */
    public ListActionsConsumer()
    {

    }

    /**
     *
     * @param map
     */
    protected void addActions(AttributeMap<?> map)
    {
        if (map.has(ENEMIES_TO) && map.has(TO_THEM))
        {
            this.addEnemy(map, ConfigDebugSingleEvents.DebugActionAddEnemyToLog);
        }

        if (map.has(ENEMIES_TO) && map.has(ENEMY_ID))
        {
            this.addEnemyId(map, ConfigDebugSingleEvents.DebugActionAddEnemyToLog);
        }

        if (map.has(PANIC_TO) && map.has(PANIC_ID))
        {
            this.addPanicToId(map, ConfigDebugSingleEvents.DebugActionPanicToIdLog);
        }

        if (map.has(ENEMY_ID) && map.has(THEM_ID))
        {
            this.addEnemyToIdThemToId(map, ConfigDebugSingleEvents.DebugActionAddEnemyToLog);
        }

        if (map.has(ACTION_MESSAGE))
        {
            this.addDoMessageAction(map);
        }

        if (map.has(ACTION_ANGRY))
        {
            this.addAngryAction(map);
        }

        if (map.has(ACTION_HELD_ITEM))
        {
            this.addHeldItem(map);
        }

        if (map.has(ACTION_ARMOR_BOOTS))
        {
            this.addArmorItem(map, ACTION_ARMOR_BOOTS, EntityEquipmentSlot.FEET);
        }

        if (map.has(ACTION_ARMOR_LEGS))
        {
            this.addArmorItem(map, ACTION_ARMOR_LEGS, EntityEquipmentSlot.LEGS);
        }

        if (map.has(ACTION_ARMOR_HELMET))
        {
            this.addArmorItem(map, ACTION_ARMOR_HELMET, EntityEquipmentSlot.HEAD);
        }

        if (map.has(ACTION_ARMOR_CHEST))
        {
            this.addArmorItem(map, ACTION_ARMOR_CHEST, EntityEquipmentSlot.CHEST);
        }

        if (map.has(ACTION_SET_NBT))
        {
            this.addMobNBT(map);
        }

        if (map.has(ACTION_HEALTH_MULTIPLY) && map.has(ACTION_HEALTH_ADD))
        {
            this.addHealthAction(map);
        }

        if (map.has(ACTION_SPEED_MULTIPLY) && map.has(ACTION_SPEED_ADD))
        {
            this.addSpeedAction(map);
        }

        if (map.has(ACTION_DAMAGE_MULTIPLY) && map.has(ACTION_DAMAGE_ADD))
        {
            this.addDamageAction(map);
        }

        if (map.has(ACTION_CUSTOM_NAME))
        {
            this.addCustomName(map);
        }

        if (map.has(ACTION_POTION))
        {
            this.addPotionsAction(map);
        }

        if (map.has(ACTION_GIVE))
        {
            this.addGiveAction(map);
        }

        if (map.has(ACTION_DROP))
        {
            this.addDropAction(map);
        }

        if (map.has(ACTION_COMMAND))
        {
            this.addCommandAction(map);
        }

        if (map.has(ACTION_FIRE))
        {
            this.addFireAction(map);
        }

        if (map.has(ACTION_EXPLOSION))
        {
            this.addExplosionAction(map);
        }

        if (map.has(ACTION_CLEAR))
        {
            this.addClearAction(map);
        }

        if (map.has(ACTION_DAMAGE))
        {
            this.addDoDamageAction(map);
        }

        if (map.has(ACTION_SET_BLOCK))
        {
            this.addSetBlockAction(map);
        }

        if (map.has(ACTION_SET_HELD_ITEM))
        {
            this.addSetHeldItemAction(map);
        }

        if (map.has(ACTION_SET_HELD_AMOUNT))
        {
            this.addSetHeldAmountAction(map);
        }
    }

    /**
     *
     * @param map
     * @param localDebug
     */
    private void addEnemy(AttributeMap<?> map, boolean localDebug)
    {
        List<String> enemiesTo = map.getList(ENEMIES_TO);
        List<String> toThem = map.getList(TO_THEM);

        if (enemiesTo.size() == 1 && toThem.size() == 1)
        {
            String enemiesTo_s = enemiesTo.get(0);
            String toThem_s = toThem.get(0);

            if (localDebug)
            {
                Log.writeDataToLogFile(0, String.format("Step 1: %s, %s", enemiesTo_s, toThem_s));
            }

            String enemiesTo_s_id = GenericOverrideSpawn.fixEntityId(enemiesTo_s);
            String toThem_s_id = GenericOverrideSpawn.fixEntityId(toThem_s);

            if (localDebug)
            {
                Log.writeDataToLogFile(0, String.format("Step 2: %s, %s", enemiesTo_s_id, toThem_s_id));
            }

            EntityEntry e_enemiesTo_forgeRegEntity = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(enemiesTo_s_id));
            EntityEntry e_toThem_forgeRegEntity = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(toThem_s_id));

            if (localDebug)
            {
                Log.writeDataToLogFile(0, String.format("Step 3: %s, %s", e_enemiesTo_forgeRegEntity, e_toThem_forgeRegEntity));
            }

            Class<? extends Entity> typeClassEnemiesTo = e_enemiesTo_forgeRegEntity == null ? null : e_enemiesTo_forgeRegEntity.getEntityClass();
            Class<? extends Entity> typeClassToThem = e_toThem_forgeRegEntity == null ? null : e_toThem_forgeRegEntity.getEntityClass();

            if (localDebug)
            {
                Log.writeDataToLogFile(0, String.format("Step 4: %s, %s", typeClassEnemiesTo, typeClassToThem));
            }

            if (typeClassEnemiesTo != null && localDebug)
            {
                Log.writeDataToLogFile(0, "Added entity: " + typeClassEnemiesTo.getName());
            }

            if (typeClassToThem != null && localDebug)
            {
                Log.writeDataToLogFile(0, "Added entity: " + typeClassToThem.getName());
            }

            if (typeClassEnemiesTo != null && typeClassToThem != null)
            {
                this.ACTIONS.add(event ->
                {
                    EntityLiving entity = (EntityLiving) event.getEntityLiving();

                    if (typeClassEnemiesTo.isInstance(entity))
                    {
                        entity.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>((EntityCreature) entity, typeClassToThem.asSubclass(EntityLiving.class), true));

                        if (localDebug)
                        {
                            Log.writeDataToLogFile(0, "Added attack target task for entity: " + typeClassEnemiesTo.getName() + " targeting " + typeClassToThem.getName());
                        }
                    }
                    else if (typeClassToThem.isInstance(entity))
                    {
                        entity.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>((EntityCreature) entity, typeClassEnemiesTo.asSubclass(EntityLiving.class), true));

                        if (localDebug)
                        {
                            Log.writeDataToLogFile(0, "Added attack target task for entity: " + typeClassToThem.getName() + " targeting " + typeClassEnemiesTo.getName());
                        }
                    }
                    else
                    {
                        if (localDebug)
                        {
                            Log.writeDataToLogFile(0, "Entity type mismatch, cannot add attack target task.");
                        }
                    }
                });
            }
            else
            {
                if (localDebug)
                {
                    Log.writeDataToLogFile(0, "Failed to add attack target task: Entity class not found for one or both entities.");
                }
            }
        }
        else
        {
            Set<Class<? extends EntityLiving>> classesEnemiesTo = new HashSet<>();
            Set<Class<? extends EntityLiving>> classesToThem = new HashSet<>();

            for (String fEnemiesTo : enemiesTo)
            {
                String enemiesTo_s_id = GenericOverrideSpawn.fixEntityId(fEnemiesTo);

                if (localDebug)
                {
                    Log.writeDataToLogFile(0, String.format("Step 1.1: %s", enemiesTo_s_id));
                }

                EntityEntry e_enemiesTo_forgeRegEntity = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(enemiesTo_s_id));

                if (localDebug)
                {
                    Log.writeDataToLogFile(0, String.format("Step 2.1: %s", e_enemiesTo_forgeRegEntity));
                }

                Class<? extends Entity> typeClassEnemiesTo = e_enemiesTo_forgeRegEntity == null ? null : e_enemiesTo_forgeRegEntity.getEntityClass();

                if (localDebug)
                {
                    Log.writeDataToLogFile(0, String.format("Step 3.1: %s", typeClassEnemiesTo));
                }

                if (typeClassEnemiesTo != null)
                {
                    classesEnemiesTo.add((Class<? extends EntityLiving>) typeClassEnemiesTo);
                }
                else
                {
                    if (localDebug)
                    {
                        Log.writeDataToLogFile(2, "Unknown mob '" + enemiesTo + "'!");
                    }
                }
            }

            for (String fToThem : toThem)
            {
                String toThem_s_id = GenericOverrideSpawn.fixEntityId(fToThem);

                if (localDebug)
                {
                    Log.writeDataToLogFile(0, String.format("Step 1.2: %s", toThem_s_id));
                }

                EntityEntry e_toThem_forgeRegEntity = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(toThem_s_id));

                if (localDebug)
                {
                    Log.writeDataToLogFile(0, String.format("Step 2.2: %s", e_toThem_forgeRegEntity));
                }

                Class<? extends Entity> typeClassToThem = e_toThem_forgeRegEntity == null ? null : e_toThem_forgeRegEntity.getEntityClass();

                if (localDebug)
                {
                    Log.writeDataToLogFile(0, String.format("Step 3.2: %s", typeClassToThem));
                }

                if (typeClassToThem != null)
                {
                    classesToThem.add((Class<? extends EntityLiving>) typeClassToThem);
                }
                else
                {
                    if (localDebug)
                    {
                        Log.writeDataToLogFile(2, "Unknown mob '" + toThem + "'!");
                    }
                }
            }

            if (!classesEnemiesTo.isEmpty() && !classesToThem.isEmpty())
            {
                this.ACTIONS.add(event ->
                {
                    EntityLiving entity = (EntityLiving) event.getEntityLiving();
                    Class<? extends EntityLiving> entityClass = entity.getClass();

                    if (classesEnemiesTo.contains(entityClass))
                    {
                        for (Class<? extends EntityLiving> targetClass : classesToThem)
                        {
                            entity.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>((EntityCreature) entity, targetClass, true));

                            if (localDebug)
                            {
                                Log.writeDataToLogFile(0, "Added attack target task for entity: " + entityClass.getName() + " targeting " + targetClass.getName());
                            }
                        }
                    }
                    else if (classesToThem.contains(entityClass))
                    {
                        for (Class<? extends EntityLiving> targetClass : classesEnemiesTo)
                        {
                            entity.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>((EntityCreature) entity, targetClass, true));

                            if (localDebug)
                            {
                                Log.writeDataToLogFile(0, "Added attack target task for entity: " + entityClass.getName() + " targeting " + targetClass.getName());
                            }
                        }
                    }
                    else
                    {
                        if (localDebug)
                        {
                            Log.writeDataToLogFile(0, "Entity type mismatch, cannot add attack target task.");
                        }
                    }
                });
            }
            else
            {
                if (localDebug)
                {
                    Log.writeDataToLogFile(0, "Failed to add attack target task: No valid entity classes found.");
                }
            }
        }
    }

    /**
     *
     * @param map
     * @param localDebug
     */
    private void addEnemyId(AttributeMap<?> map, boolean localDebug)
    {
        List<String> enemiesTo = map.getList(ENEMIES_TO);
        List<String> enemyIdPrefixes = map.getList(ENEMY_ID);

        Set<Class<? extends EntityLiving>> classesEnemiesTo = new HashSet<>();
        Set<Class<? extends EntityLiving>> classesEnemyId = new HashSet<>();

        try
        {
            for (String fEnemiesTo : enemiesTo)
            {
                String enemiesTo_s_id = GenericOverrideSpawn.fixEntityId(fEnemiesTo);

                if (localDebug)
                {
                    Log.writeDataToLogFile(0, String.format("Step 1.1: %s", enemiesTo_s_id));
                }

                EntityEntry e_enemiesTo_forgeRegEntity = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(enemiesTo_s_id));

                if (localDebug)
                {
                    Log.writeDataToLogFile(0, String.format("Step 2.1: %s", e_enemiesTo_forgeRegEntity));
                }

                Class<? extends Entity> typeClassEnemiesTo = e_enemiesTo_forgeRegEntity == null ? null : e_enemiesTo_forgeRegEntity.getEntityClass();

                if (localDebug)
                {
                    Log.writeDataToLogFile(0, String.format("Step 3.1: %s", typeClassEnemiesTo));
                }

                if (typeClassEnemiesTo != null && EntityLiving.class.isAssignableFrom(typeClassEnemiesTo))
                {
                    classesEnemiesTo.add((Class<? extends EntityLiving>) typeClassEnemiesTo);
                }
                else
                {
                    if (localDebug)
                    {
                        Log.writeDataToLogFile(2, "Unknown or incompatible mob '" + enemiesTo + "'!");
                    }
                }
            }

            for (String enemyIdPrefix : enemyIdPrefixes)
            {
                for (EntityEntry entityEntry : ForgeRegistries.ENTITIES)
                {
                    if (entityEntry.getRegistryName().toString().startsWith(enemyIdPrefix))
                    {
                        Class<? extends Entity> typeClassEnemyId = entityEntry.getEntityClass();

                        if (localDebug)
                        {
                            Log.writeDataToLogFile(0, String.format("Step 1.2: %s", entityEntry.getRegistryName().toString()));
                        }

                        if (typeClassEnemyId != null && EntityLiving.class.isAssignableFrom(typeClassEnemyId))
                        {
                            classesEnemyId.add((Class<? extends EntityLiving>) typeClassEnemyId);
                        }
                        else
                        {
                            if (localDebug)
                            {
                                Log.writeDataToLogFile(2, "Unknown or incompatible mob with prefix '" + enemyIdPrefix + "'!");
                            }
                        }
                    }
                }
            }

            if (!classesEnemiesTo.isEmpty() && !classesEnemyId.isEmpty())
            {
                this.ACTIONS.add(event ->
                {
                    try
                    {
                        EntityLiving entity = (EntityLiving) event.getEntityLiving();
                        Class<? extends EntityLiving> entityClass = entity.getClass();

                        if (localDebug)
                        {
                            Log.writeDataToLogFile(0, "Event entity: " + entityClass.getName());
                        }

                        if (classesEnemiesTo.contains(entityClass))
                        {
                            for (Class<? extends EntityLiving> targetClass : classesEnemyId)
                            {
                                if (localDebug)
                                {
                                    Log.writeDataToLogFile(0, "Adding attack task for: " + entityClass.getName() + " targeting " + targetClass.getName());
                                }

                                entity.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>((EntityCreature) entity, targetClass, true));

                                if (localDebug)
                                {
                                    Log.writeDataToLogFile(0, "Added attack target task for entity: " + entityClass.getName() + " targeting " + targetClass.getName());
                                }
                            }
                        }
                        else if (classesEnemyId.contains(entityClass))
                        {
                            for (Class<? extends EntityLiving> targetClass : classesEnemiesTo)
                            {
                                if (localDebug)
                                {
                                    Log.writeDataToLogFile(0, "Adding attack task for: " + entityClass.getName() + " targeting " + targetClass.getName());
                                }

                                entity.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>((EntityCreature) entity, targetClass, true));

                                if (localDebug)
                                {
                                    Log.writeDataToLogFile(0, "Added attack target task for entity: " + entityClass.getName() + " targeting " + targetClass.getName());
                                }
                            }
                        }
                        else
                        {
                            if (localDebug)
                            {
                                Log.writeDataToLogFile(0, "Entity type mismatch, cannot add attack target task.");
                            }
                        }
                    }
                    catch (Exception exception)
                    {
                        Log.writeDataToLogFile(2, "Error in event action: " + exception.getMessage());
                    }
                });
            }
            else
            {
                if (localDebug)
                {
                    Log.writeDataToLogFile(0, "Failed to add attack target task: No valid entity classes found.");
                }
            }
        }
        catch (Exception exception)
        {
            Log.writeDataToLogFile(2, "Error in addEnemyId method: " + exception.getMessage());
        }
    }

    /**
     *
     * @param map
     * @param localDebug
     */
    private void addPanicToId(AttributeMap<?> map, boolean localDebug)
    {
        List<String> panicTo = map.getList(PANIC_TO);
        List<String> panicIdPrefixes = map.getList(PANIC_ID);

        Set<Class<? extends EntityLiving>> classesPanicTo = new HashSet<>();
        Set<Class<? extends EntityLiving>> classesPanicId = new HashSet<>();

        try
        {
            for (String fPanicTo : panicTo)
            {
                String panicTo_s_id = GenericOverrideSpawn.fixEntityId(fPanicTo);

                if (localDebug)
                {
                    Log.writeDataToLogFile(0, String.format("Step 1.1: %s", panicTo_s_id));
                }

                EntityEntry e_panicTo_forgeRegEntity = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(panicTo_s_id));

                if (localDebug)
                {
                    Log.writeDataToLogFile(0, String.format("Step 2.1: %s", e_panicTo_forgeRegEntity));
                }

                Class<? extends Entity> typeClassPanicTo = e_panicTo_forgeRegEntity == null ? null : e_panicTo_forgeRegEntity.getEntityClass();

                if (localDebug)
                {
                    Log.writeDataToLogFile(0, String.format("Step 3.1: %s", typeClassPanicTo));
                }

                if (typeClassPanicTo != null && EntityLiving.class.isAssignableFrom(typeClassPanicTo))
                {
                    classesPanicTo.add((Class<? extends EntityLiving>) typeClassPanicTo);
                }
                else
                {
                    if (localDebug)
                    {
                        Log.writeDataToLogFile(2, "Unknown or incompatible mob '" + panicTo + "'!");
                    }
                }
            }

            for (String fPanicIdPrefix : panicIdPrefixes)
            {
                for (EntityEntry entityEntry : ForgeRegistries.ENTITIES)
                {
                    if (entityEntry.getRegistryName().toString().startsWith(fPanicIdPrefix))
                    {
                        Class<? extends Entity> typeClassPanicId = entityEntry.getEntityClass();

                        if (localDebug)
                        {
                            Log.writeDataToLogFile(0, String.format("Step 1.2: %s", entityEntry.getRegistryName().toString()));
                        }

                        if (typeClassPanicId != null && EntityLiving.class.isAssignableFrom(typeClassPanicId))
                        {
                            classesPanicId.add((Class<? extends EntityLiving>) typeClassPanicId);
                        }
                        else
                        {
                            if (localDebug)
                            {
                                Log.writeDataToLogFile(2, "Unknown or incompatible mob with prefix '" + fPanicIdPrefix + "'!");
                            }
                        }
                    }
                }
            }

            if (!classesPanicTo.isEmpty() && !classesPanicId.isEmpty())
            {
                this.ACTIONS.add(event ->
                {
                    try
                    {
                        EntityLiving entity = (EntityLiving) event.getEntityLiving();
                        Class<? extends EntityLiving> entityClass = entity.getClass();

                        if (localDebug)
                        {
                            Log.writeDataToLogFile(0, "Event entity: " + entityClass.getName());
                        }

                        if (classesPanicTo.contains(entityClass))
                        {
                            for (Class<? extends EntityLiving> panicClass : classesPanicId)
                            {
                                if (localDebug)
                                {
                                    Log.writeDataToLogFile(0, "Adding panic task for: " + entityClass.getName() + " avoiding " + panicClass.getName());
                                }

                                entity.tasks.addTask(1, new EntityAIAvoidEntity<>((EntityCreature) entity, panicClass, 16.0F, 1.5D, 2.0D));

                                if (localDebug)
                                {
                                    Log.writeDataToLogFile(0, "Added panic task for entity: " + entityClass.getName() + " avoiding " + panicClass.getName());
                                }
                            }
                        }
                    }
                    catch (Exception exception)
                    {
                        Log.writeDataToLogFile(2, "Error in event action: " + exception.getMessage());
                    }
                });
            }
            else
            {
                if (localDebug)
                {
                    Log.writeDataToLogFile(0, "Failed to add panic task: No valid entity classes found.");
                }
            }
        }
        catch (Exception exception)
        {
            Log.writeDataToLogFile(2, "Error in addPanicToId method: " + exception.getMessage());
        }
    }

    /**
     *
     * @param map
     * @param localDebug
     */
    private void addEnemyToIdThemToId(AttributeMap<?> map, boolean localDebug)
    {
        List<String> listEnemyId = map.getList(ENEMY_ID);
        List<String> listThemId = map.getList(THEM_ID);

        Set<Class<? extends EntityLiving>> classesEnemyId = new HashSet<>();
        Set<Class<? extends EntityLiving>> classesThemId = new HashSet<>();

        try
        {
            for (String enemyIdPrefix : listEnemyId)
            {
                for (EntityEntry entityEntry : ForgeRegistries.ENTITIES)
                {
                    if (entityEntry.getRegistryName().toString().startsWith(enemyIdPrefix))
                    {
                        Class<? extends Entity> typeClassEnemyId = entityEntry.getEntityClass();

                        if (localDebug)
                        {
                            Log.writeDataToLogFile(0, String.format("Step 1.1: %s", entityEntry.getRegistryName().toString()));
                        }

                        if (typeClassEnemyId != null && EntityLiving.class.isAssignableFrom(typeClassEnemyId))
                        {
                            classesEnemyId.add((Class<? extends EntityLiving>) typeClassEnemyId);
                        }
                        else
                        {
                            if (localDebug)
                            {
                                Log.writeDataToLogFile(2, "Unknown or incompatible mob with prefix '" + enemyIdPrefix + "'!");
                            }
                        }
                    }
                }
            }

            for (String themIdPrefix : listThemId)
            {
                for (EntityEntry entityEntry : ForgeRegistries.ENTITIES)
                {
                    if (entityEntry.getRegistryName().toString().startsWith(themIdPrefix))
                    {
                        Class<? extends Entity> typeClassThemId = entityEntry.getEntityClass();

                        if (localDebug)
                        {
                            Log.writeDataToLogFile(0, String.format("Step 1.2: %s", entityEntry.getRegistryName().toString()));
                        }

                        if (typeClassThemId != null && EntityLiving.class.isAssignableFrom(typeClassThemId))
                        {
                            classesThemId.add((Class<? extends EntityLiving>) typeClassThemId);
                        }
                        else
                        {
                            if (localDebug)
                            {
                                Log.writeDataToLogFile(2, "Unknown or incompatible mob with prefix '" + themIdPrefix + "'!");
                            }
                        }
                    }
                }
            }

            if (!classesEnemyId.isEmpty() && !classesThemId.isEmpty())
            {
                this.ACTIONS.add(event ->
                {
                    try
                    {
                        EntityLiving entity = (EntityLiving) event.getEntityLiving();
                        Class<? extends EntityLiving> entityClass = entity.getClass();

                        if (localDebug)
                        {
                            Log.writeDataToLogFile(0, "Event entity: " + entityClass.getName());
                        }

                        if (classesEnemyId.contains(entityClass))
                        {
                            for (Class<? extends EntityLiving> targetClass : classesThemId)
                            {
                                if (localDebug) {
                                    Log.writeDataToLogFile(0, "Adding attack task for: " + entityClass.getName() + " targeting " + targetClass.getName());
                                }

                                entity.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>((EntityCreature) entity, targetClass, true));

                                if (localDebug) {
                                    Log.writeDataToLogFile(0, "Added attack target task for entity: " + entityClass.getName() + " targeting " + targetClass.getName());
                                }
                            }
                        }
                        else if (classesThemId.contains(entityClass))
                        {
                            for (Class<? extends EntityLiving> targetClass : classesEnemyId)
                            {
                                if (localDebug)
                                {
                                    Log.writeDataToLogFile(0, "Adding attack task for: " + entityClass.getName() + " targeting " + targetClass.getName());
                                }

                                entity.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>((EntityCreature) entity, targetClass, true));

                                if (localDebug)
                                {
                                    Log.writeDataToLogFile(0, "Added attack target task for entity: " + entityClass.getName() + " targeting " + targetClass.getName());
                                }
                            }
                        }
                        else
                        {
                            if (localDebug)
                            {
                                Log.writeDataToLogFile(0, "Entity type mismatch, cannot add attack target task.");
                            }
                        }
                    }
                    catch (Exception exception)
                    {
                        Log.writeDataToLogFile(2, "Error in event action: " + exception.getMessage());
                    }
                });
            }
            else
            {
                if (localDebug)
                {
                    Log.writeDataToLogFile(0, "Failed to add attack target task: No valid entity classes found.");
                }
            }
        }
        catch (Exception exception)
        {
            Log.writeDataToLogFile(2, "Error in addEnemyToIdThemToId method: " + exception.getMessage());
        }
    }

    /**
     *
     * @param map
     */
    private void addDoMessageAction(AttributeMap<?> map)
    {
        Object message = map.get(ACTION_MESSAGE);

        this.ACTIONS.add(event ->
        {
            EntityPlayer player = event.getPlayer();

            if (player == null)
            {
                player = event.getWorld().getClosestPlayerToEntity(event.getEntityLiving(), 100);
            }

            if (player != null)
            {
                player.sendStatusMessage(new TextComponentString((String)message), false);
            }
        });
    }

    /**
     *
     * @param map
     */
    public void addAngryAction(AttributeMap<?> map)
    {
        Object actionAngry = map.get(ACTION_ANGRY);

        if ((Boolean)actionAngry)
        {
            this.ACTIONS.add(event ->
            {
                EntityLivingBase entityLiving = event.getEntityLiving();

                if (entityLiving instanceof EntityPigZombie)
                {
                    EntityPigZombie pigZombie = (EntityPigZombie) entityLiving;
                    EntityPlayer player = event.getWorld().getClosestPlayerToEntity(entityLiving, 50);

                    if (player != null)
                    {
                        pigZombie.setRevengeTarget(player);
                    }
                }
                else if (entityLiving instanceof EntityLiving)
                {
                    EntityPlayer player = event.getWorld().getClosestPlayerToEntity(entityLiving, 50);

                    if (player != null)
                    {
                        ((EntityLiving) entityLiving).setAttackTarget(player);
                    }
                }
            });
        }
    }

    /**
     *
     * @param map
     */
    private void addHeldItem(AttributeMap<?> map)
    {
        List<Pair<Float, ItemStack>> items = AuxFunctions.getItemsWeighted(map.getList(ACTION_HELD_ITEM));

        if (items.isEmpty())
        {
            return;
        }

        if (items.size() == 1)
        {
            ItemStack item = items.get(0).getRight();

            this.ACTIONS.add(event ->
            {
                EntityLivingBase entityLiving = event.getEntityLiving();

                if (entityLiving != null)
                {
                    if (entityLiving instanceof EntityEnderman)
                    {
                        if (item.getItem() instanceof ItemBlock)
                        {
                            ItemBlock b = (ItemBlock) item.getItem();
                            ((EntityEnderman) entityLiving).setHeldBlockState(b.getBlock().getStateFromMeta(b.getMetadata(item.getItemDamage())));
                        }
                    }
                    else
                    {
                        entityLiving.setHeldItem(EnumHand.MAIN_HAND, item.copy());
                    }
                }
            });
        }
        else
        {
            float total = AuxFunctions.getTotal(items);

            this.ACTIONS.add(event ->
            {
                EntityLivingBase entityLiving = event.getEntityLiving();

                if (entityLiving != null)
                {
                    ItemStack item = AuxFunctions.getRandomItem(items, total);

                    if (entityLiving instanceof EntityEnderman)
                    {
                        if (item.getItem() instanceof ItemBlock)
                        {
                            ItemBlock b = (ItemBlock) item.getItem();
                            ((EntityEnderman) entityLiving).setHeldBlockState(b.getBlock().getStateFromMeta(b.getMetadata(item.getItemDamage())));
                        }
                    }
                    else
                    {
                        entityLiving.setHeldItem(EnumHand.MAIN_HAND, item.copy());
                    }
                }
            });
        }
    }

    /**
     *
     * @param map
     * @param itemKey
     * @param slot
     */
    private void addArmorItem(AttributeMap<?> map, AttributeKey<String> itemKey, EntityEquipmentSlot slot)
    {
        List<Pair<Float, ItemStack>> items = AuxFunctions.getItemsWeighted(map.getList(itemKey));

        if (items.isEmpty())
        {
            return;
        }

        if (items.size() == 1)
        {
            ItemStack item = items.get(0).getRight();

            this.ACTIONS.add(event ->
            {
                EntityLivingBase entityLiving = event.getEntityLiving();

                if (entityLiving != null)
                {
                    entityLiving.setItemStackToSlot(slot, item.copy());
                }
            });
        }
        else
        {
            float total = AuxFunctions.getTotal(items);

            this.ACTIONS.add(event ->
            {
                EntityLivingBase entityLiving = event.getEntityLiving();

                if (entityLiving != null)
                {
                    entityLiving.setItemStackToSlot(slot, AuxFunctions.getRandomItem(items, total));
                }
            });
        }
    }

    /**
     *
     * @param map
     */
    private void addMobNBT(AttributeMap<?> map)
    {
        String mobNbt = (String)map.get(ACTION_SET_NBT);

        if (mobNbt != null)
        {
            NBTTagCompound tagCompound;

            try
            {
                tagCompound = JsonToNBT.getTagFromJson(mobNbt);
            }
            catch (NBTException exception)
            {
                Log.writeDataToLogFile(2, "Bad NBT for mob!");
                return;
            }

            this.ACTIONS.add(event ->
            {
                EntityLivingBase entityLiving = event.getEntityLiving();
                entityLiving.readEntityFromNBT(tagCompound);
            });
        }
    }

    /**
     *
     * @param map
     */
    private void addHealthAction(AttributeMap<?> map)
    {
        Object multiple = map.has(ACTION_HEALTH_MULTIPLY) ? map.get(ACTION_HEALTH_MULTIPLY) : 1.f;
        Object added = map.has(ACTION_HEALTH_ADD) ? map.get(ACTION_HEALTH_ADD) : 0.f;

        this.ACTIONS.add(event ->
        {
            EntityLivingBase entityLiving = event.getEntityLiving();

            if (entityLiving != null)
            {
                if (!entityLiving.getTags().contains("ctrlHealth"))
                {
                    IAttributeInstance entityAttribute = entityLiving.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);

                    double newMax = entityAttribute.getBaseValue() * (Float)multiple + (Float)added;
                    entityAttribute.setBaseValue(newMax);
                    entityLiving.setHealth((float)newMax);
                    entityLiving.addTag("ctrlHealth");
                }
            }
        });
    }

    /**
     *
     * @param map
     */
    private void addSpeedAction(AttributeMap<?> map)
    {
        Object multiple = map.has(ACTION_SPEED_MULTIPLY) ? map.get(ACTION_SPEED_MULTIPLY) : 1.f;
        Object added = map.has(ACTION_SPEED_ADD) ? map.get(ACTION_SPEED_ADD) : 0.f;

        this.ACTIONS.add(event ->
        {
            EntityLivingBase entityLiving = event.getEntityLiving();

            if (entityLiving != null)
            {
                if (!entityLiving.getTags().contains("ctrlSpeed"))
                {
                    IAttributeInstance entityAttribute = entityLiving.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

                    double newMax = entityAttribute.getBaseValue() * (Float)multiple + (Float)added;
                    entityAttribute.setBaseValue(newMax);
                    entityLiving.addTag("ctrlSpeed");
                }
            }
        });
    }

    /**
     *
     * @param map
     */
    private void addDamageAction(AttributeMap<?> map)
    {
        Object multiple = map.has(ACTION_DAMAGE_MULTIPLY) ? map.get(ACTION_DAMAGE_MULTIPLY) : 1.f;
        Object added = map.has(ACTION_DAMAGE_ADD) ? map.get(ACTION_DAMAGE_ADD) : 0.f;

        this.ACTIONS.add(event ->
        {
            EntityLivingBase entityLiving = event.getEntityLiving();

            if (entityLiving != null)
            {
                if (!entityLiving.getTags().contains("ctrlDamage"))
                {
                    IAttributeInstance entityAttribute = entityLiving.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);

                    double newMax = entityAttribute.getBaseValue() * (Float)multiple + (Float)added;
                    entityAttribute.setBaseValue(newMax);
                    entityLiving.addTag("ctrlDamage");
                }
            }
        });
    }

    /**
     *
     * @param map
     */
    private void addCustomName(AttributeMap<?> map)
    {
        Object customName = map.get(ACTION_CUSTOM_NAME);

        if (customName != null)
        {
            this.ACTIONS.add(event ->
            {
                EntityLivingBase entityLiving = event.getEntityLiving();
                entityLiving.setCustomNameTag((String)customName);
            });
        }
    }

    /**
     *
     * @param map
     */
    private void addPotionsAction(AttributeMap<?> map)
    {
        List<AuxFunctions.PotionEffectWithChance> effects = new ArrayList<>();

        for (String actionPotion : map.getList(ACTION_POTION))
        {
            String[] split = Arrays.stream(StringUtils.split(actionPotion, ','))
                    .map(String::trim)
                    .toArray(String[]::new);

            if (split.length < 3 || split.length > 4)
            {
                Log.writeDataToLogFile(2, "Bad potion specifier '" + actionPotion + "'! Use <potion>,<duration>,<amplifier>[,<chance>]");
                continue;
            }

            Potion potion = ForgeRegistries.POTIONS.getValue(new ResourceLocation(split[0]));

            if (potion == null)
            {
                Log.writeDataToLogFile(2, "Can't find potion '" + actionPotion + "'!");
                continue;
            }

            int duration;
            int amplifier;
            double chance = 1.0D; // Default to 100% chance

            try
            {
                duration = Integer.parseInt(split[1]);
                amplifier = Integer.parseInt(split[2]);

                if (split.length == 4)
                {
                    chance = Double.parseDouble(split[3]);
                }
            }
            catch (NumberFormatException exception)
            {
                Log.writeDataToLogFile(2, "Bad duration, amplifier or chance integer for '" + actionPotion + "'!");
                continue;
            }

            effects.add(new AuxFunctions.PotionEffectWithChance(new PotionEffect(potion, duration, amplifier), chance));
        }

        if (!effects.isEmpty())
        {
            this.ACTIONS.add(event ->
            {
                EntityLivingBase living = event.getEntityLiving();

                if (living != null)
                {
                    for (AuxFunctions.PotionEffectWithChance effectWithChance : effects)
                    {
                        if (Math.random() <= effectWithChance.Chance)
                        {
                            PotionEffect effect = effectWithChance.Effect;
                            PotionEffect newEffect = new PotionEffect(effect.getPotion(), effect.getDuration(), effect.getAmplifier());
                            living.addPotionEffect(newEffect);
                        }
                    }
                }
            });
        }
    }

    /**
     *
     * @param map
     */
    private void addGiveAction(AttributeMap<?> map)
    {
        List<Pair<Float, ItemStack>> items = AuxFunctions.getItemsWeighted(map.getList(ACTION_GIVE));

        if (items.isEmpty())
        {
            return;
        }

        if (items.size() == 1)
        {
            ItemStack item = items.get(0).getRight();

            this.ACTIONS.add(event ->
            {
                EntityPlayer player = event.getPlayer();

                if (player != null)
                {
                    if (!player.inventory.addItemStackToInventory(item.copy()))
                    {
                        player.entityDropItem(item.copy(), 1.05f);
                    }
                }
            });
        }
        else
        {
            float total = AuxFunctions.getTotal(items);

            this.ACTIONS.add(event ->
            {
                EntityPlayer player = event.getPlayer();

                if (player != null)
                {
                    ItemStack item = AuxFunctions.getRandomItem(items, total);

                    if (!player.inventory.addItemStackToInventory(item.copy()))
                    {
                        player.entityDropItem(item.copy(), 1.05f);
                    }
                }
            });
        }
    }

    /**
     *
     * @param map
     */
    private void addDropAction(AttributeMap<?> map)
    {
        List<Pair<Float, ItemStack>> items = AuxFunctions.getItemsWeighted(map.getList(ACTION_DROP));

        if (items.isEmpty())
        {
            return;
        }

        if (items.size() == 1)
        {
            ItemStack item = items.get(0).getRight();

            this.ACTIONS.add(event ->
            {
                BlockPos pos = event.getPosition();
                EntityItem entityItem = new EntityItem(event.getWorld(), pos.getX(), pos.getY(), pos.getZ(), item.copy());
                event.getWorld().spawnEntity(entityItem);
            });
        }
        else
        {
            float total = AuxFunctions.getTotal(items);

            this.ACTIONS.add(event ->
            {
                BlockPos pos = event.getPosition();
                ItemStack item = AuxFunctions.getRandomItem(items, total);
                EntityItem entityItem = new EntityItem(event.getWorld(), pos.getX(), pos.getY(), pos.getZ(), item.copy());
                event.getWorld().spawnEntity(entityItem);
            });
        }
    }

    /**
     *
     * @param map
     */
    private void addCommandAction(AttributeMap<?> map)
    {
        Object command = map.get(ACTION_COMMAND);

        this.ACTIONS.add(event ->
        {
            EntityPlayer player = event.getPlayer();
            MinecraftServer server = event.getWorld().getMinecraftServer();

            assert server != null;

            server.commandManager.executeCommand(player != null ? player : new Sender(event.getWorld(), null), (String)command);
        });
    }

    /**
     *
     * @param map
     */
    private void addFireAction(AttributeMap<?> map)
    {
        Object fireAction = map.get(ACTION_FIRE);

        this.ACTIONS.add(event ->
        {
            EntityLivingBase living = event.getEntityLiving();

            if (living != null)
            {
                living.attackEntityFrom(DamageSource.ON_FIRE, 0.1f);
                living.setFire((Integer)fireAction);
            }
        });
    }

    /**
     *
     * @param map
     */
    private void addExplosionAction(AttributeMap<?> map)
    {
        String fireAction = (String)map.get(ACTION_EXPLOSION);
        String[] split = StringUtils.split(fireAction, ",");

        float strength = 1.0f;

        boolean flaming = false;
        boolean smoking = false;

        try
        {
            strength = Float.parseFloat(split[0]);
            flaming = "1".equalsIgnoreCase(split[1]) || "true".equals(split[1].toLowerCase()) || "yes".equals(split[1].toLowerCase());
            smoking = "1".equalsIgnoreCase(split[2]) || "true".equals(split[2].toLowerCase()) || "yes".equals(split[2].toLowerCase());
        }
        catch (Exception exception)
        {

        }

        float finalStrength = strength;
        boolean finalFlaming = flaming;
        boolean finalSmoking = smoking;

        this.ACTIONS.add(event ->
        {
            BlockPos pos = event.getPosition();

            if (pos != null)
            {
                event.getWorld().newExplosion(null, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, finalStrength, finalFlaming, finalSmoking);
            }
        });
    }

    /**
     *
     * @param map
     */
    private void addClearAction(AttributeMap<?> map)
    {
        Object clear = map.get(ACTION_CLEAR);

        if ((Boolean)clear)
        {
            this.ACTIONS.add(event ->
            {
                EntityLivingBase living = event.getEntityLiving();

                if (living != null)
                {
                    living.clearActivePotions();
                }
            });
        }
    }

    /**
     *
     * @param map
     */
    private void addDoDamageAction(AttributeMap<?> map)
    {
        String damage = (String) map.get(ACTION_DAMAGE);
        String[] split = StringUtils.split(damage, "=");
        DamageSource source = AuxFunctions.DamageMap.get(split[0]);

        if (source == null)
        {
            Log.writeDataToLogFile(2, "Can't find damage source '" + split[0] + "'!");
            return;
        }

        float amount = split.length > 1 ? Float.parseFloat(split[1]) : 1.0f;

        this.ACTIONS.add(event ->
        {
            EntityLivingBase living = event.getEntityLiving();

            if (living != null)
            {
                living.attackEntityFrom(source, amount);
            }
        });
    }

    /**
     *
     * @param map
     */
    private void addSetBlockAction(AttributeMap<?> map)
    {
        Function<SignalDataGetter, BlockPos> posFunction;

        if (map.has(BLOCK_OFFSET))
        {
            posFunction = (Function<SignalDataGetter, BlockPos>)
                    AuxFunctions.parseOffset((String)map.get(BLOCK_OFFSET));
        }
        else
        {
            posFunction = event -> event.getPosition();
        }

        Object json = map.get(ACTION_SET_BLOCK);
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse((String)json);

        if (element.isJsonPrimitive())
        {
            String blockName = element.getAsString();
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockName));

            if (block == null)
            {
                Log.writeDataToLogFile(2, "Block '" + blockName + "' is not valid!");
                return;
            }

            IBlockState state = block.getDefaultState();

            this.ACTIONS.add(event ->
            {
                BlockPos pos = posFunction.apply(event);

                if (pos != null)
                {
                    event.getWorld().setBlockState(pos, state, 3);
                }
            });
        }
        else
        {
            JsonObject obj = element.getAsJsonObject();

            if (!obj.has("block"))
            {
                Log.writeDataToLogFile(2, "Block is not valid!");
                return;
            }

            String blockName = obj.get("block").getAsString();
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockName));

            if (block == null)
            {
                Log.writeDataToLogFile(2, "Block '" + blockName + "' is not valid!");
                return;
            }

            IBlockState state = block.getDefaultState();

            if (obj.has("properties"))
            {
                JsonArray propArray = obj.get("properties").getAsJsonArray();

                for (JsonElement el : propArray)
                {
                    JsonObject propObj = el.getAsJsonObject();
                    String name = propObj.get("name").getAsString();
                    String value = propObj.get("value").getAsString();

                    for (IProperty<?> key : state.getPropertyKeys())
                    {
                        if (name.equals(key.getName()))
                        {
                            state = AuxFunctions.set(state, key, value);
                        }
                    }
                }
            }

            IBlockState finalState = state;

            this.ACTIONS.add(event ->
            {
                BlockPos pos = posFunction.apply(event);

                if (pos != null)
                {
                    event.getWorld().setBlockState(pos, finalState, 3);
                }
            });
        }
    }

    /**
     *
     * @param map
     */
    private void addSetHeldItemAction(AttributeMap<?> map)
    {
        Object json = map.get(ACTION_SET_HELD_ITEM);
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse((String)json);
        ItemStack stack;

        if (element.isJsonPrimitive())
        {
            String name = element.getAsString();
            stack = ItemStackBuilder.parseStack(name);
        }
        else if (element.isJsonObject())
        {
            JsonObject obj = element.getAsJsonObject();
            stack = ItemStackBuilder.parseStack(obj);

            if (stack == null)
            {
                return;
            }
        }
        else
        {
            Log.writeDataToLogFile(2, "Item description '" + json + "' is not valid!");
            return;
        }

        this.ACTIONS.add(event -> event.getPlayer().setHeldItem(EnumHand.MAIN_HAND, stack.copy()));
    }

    /**
     *
     * @param map
     */
    private void addSetHeldAmountAction(AttributeMap<?> map)
    {
        String amount = (String)map.get(ACTION_SET_HELD_AMOUNT);

        int add = 0;
        int set = -1;

        if (amount.startsWith("+"))
        {
            add = Integer.parseInt(amount.substring(1));
        }
        else if (amount.startsWith("-"))
        {
            add = -Integer.parseInt(amount.substring(1));
        }
        else if (amount.startsWith("="))
        {
            set = Integer.parseInt(amount.substring(1));
        }
        else
        {
            set = Integer.parseInt(amount);
        }

        int finalSet = set;

        if (finalSet >= 0)
        {
            this.ACTIONS.add(event ->
            {
                ItemStack item = event.getPlayer().getHeldItemMainhand();
                item.setCount(finalSet);
                event.getPlayer().setHeldItem(EnumHand.MAIN_HAND, item.copy());
            });
        }
        else
        {
            int finalAdd = add;

            this.ACTIONS.add(event ->
            {
                ItemStack item = event.getPlayer().getHeldItemMainhand();

                int newCount = item.getCount() + finalAdd;

                if (newCount < 0)
                {
                    newCount = 0;
                }
                else if (newCount >= item.getMaxStackSize())
                {
                    newCount = item.getMaxStackSize()-1;
                }

                item.setCount(newCount);
                event.getPlayer().setHeldItem(EnumHand.MAIN_HAND, item.copy());
            });
        }
    }
}
