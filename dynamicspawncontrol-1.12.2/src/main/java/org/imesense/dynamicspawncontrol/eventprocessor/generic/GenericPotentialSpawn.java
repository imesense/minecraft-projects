package org.imesense.dynamicspawncontrol.eventprocessor.generic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.fixes.EntityId;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.eventprocessor.generic.keyword.SingleKeyWord;
import org.imesense.dynamicspawncontrol.eventprocessor.listaction.ListActionBinary;
import org.imesense.dynamicspawncontrol.eventprocessor.listaction.ListActionConsumer;
import org.imesense.dynamicspawncontrol.core.attributefactory.Attribute;
import org.imesense.dynamicspawncontrol.core.attributefactory.AttributeMap;
import org.imesense.dynamicspawncontrol.core.attributefactory.AttributeMapFactory;
import org.imesense.dynamicspawncontrol.core.api.AbstractSignalDataAccessor;
import org.imesense.dynamicspawncontrol.core.api.AbstractSignalDataGetter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.imesense.dynamicspawncontrol.eventprocessor.generic.keyword.CommonKeyWord.*;
import static org.imesense.dynamicspawncontrol.eventprocessor.generic.keyword.PotentialSpawn.*;

/**
 *
 */
public final class GenericPotentialSpawn extends ListActionConsumer<AbstractSignalDataGetter>
{
    /**
     *
     */
    private static int countCreatedMaps = 0;

    /**
     *
     */
    private final ListActionBinary RULE_EVALUATOR;

    /**
     *
     */
    public static final EntityId FIXER = new EntityId();

    /**
     *
     */
    private final List<Class<?>> TO_REMOVE_MOBS = new ArrayList<>();

    /**
     *
     * @return
     */
    public List<Class<?>> getToRemoveMobs() { return this.TO_REMOVE_MOBS; }

    /**
     *
     */
    private final List<Biome.SpawnListEntry> SPAWN_ENTRIES = new ArrayList<>();

    /**
     *
     * @return
     */
    public List<Biome.SpawnListEntry> getSpawnEntries() { return this.SPAWN_ENTRIES; }

    /**
     *
     */
    public HashMap<Class<? extends Entity>, Integer> MaxHeight = new HashMap<>();

    /**
     *
     */
    public HashMap<Class<? extends Entity>, Integer> MinHeight = new HashMap<>();

    /**
     *
     */
    public HashMap<Class<? extends Entity>, Float> SpawnChances = new HashMap<>();

    /**
     *
     */
    private static final AttributeMapFactory<Object> FACTORY = new AttributeMapFactory<>();

    /**
     *
     * @param potentialSpawns
     * @return
     */
    public boolean match(WorldEvent.PotentialSpawns potentialSpawns) { return RULE_EVALUATOR.match(potentialSpawns, EVENT_QUERY); }

    /**
     *
     * @param entityClass
     * @return
     */
    public float getMinHeightChance(Class<? extends Entity> entityClass) { return MinHeight.getOrDefault(entityClass, 5); }

    /**
     *
     * @param entityClass
     * @return
     */
    public float getMaxHeightChance(Class<? extends Entity> entityClass) { return MaxHeight.getOrDefault(entityClass, 255); }

    /**
     *
     * @param entityClass
     * @return
     */
    public float getSpawnChance(Class<? extends Entity> entityClass) { return SpawnChances.getOrDefault(entityClass, 0.0f); }

    /**
     *
     * @param attributeMap
     */
    private GenericPotentialSpawn(AttributeMap<?> attributeMap)
    {
        super();

        Log.writeDataToLogFile(0, String.format("Iterator for [%s] number [%d]", GenericPotentialSpawn.class.getName(), countCreatedMaps++));

        this.RULE_EVALUATOR = new ListActionBinary<>(attributeMap);

        for (AttributeMap<?> mobMap : attributeMap.getListA(MOB_STRUCT))
        {
            String entityId = fixEntityId((String) mobMap.get(MOB_NAME));
            EntityEntry typeEntity = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(entityId));
            Class<? extends Entity> typeClass = typeEntity == null ? null : typeEntity.getEntityClass();

            if (typeClass == null)
            {
                Log.writeDataToLogFile(2, "Cannot find mob '" + mobMap.get(MOB_NAME) + "'");
                throw new RuntimeException();
            }

            int weight = CodeGeneric.checkParameter(mobMap, MOB_WEIGHT, 1, 100, "frequency");
            int groupCountMin = CodeGeneric.checkParameter(mobMap, MOB_GROUP_COUNT_MIN, 1, 10, "group_count_min");
            int groupCountMax = CodeGeneric.checkParameter(mobMap, MOB_GROUP_COUNT_MAX, 1, 20, "group_count_max");
            float spawnChance = CodeGeneric.checkParameter(mobMap, MOB_SPAWN_CHANCE, 0.01f, 1.0f, "spawnChanceValue");
            int maxHeight = CodeGeneric.checkParameter(mobMap, MOB_MAX_HEIGHT, 5, 255, "max_height");
            int minHeight = CodeGeneric.checkParameter(mobMap, MOB_MIN_HEIGHT, 5, 255, "min_height");

            Biome.SpawnListEntry entry = new Biome.SpawnListEntry((Class<? extends EntityLiving>) typeClass, weight, groupCountMin, groupCountMax);

            Log.writeDataToLogFile(0, String.format(
                    "Entity [%s:%s] has been added to the spawn list. " +
                            "Data -> SpawnChance [%f], " +
                            "Frequency [%d], " +
                            "Group min [%d], " +
                            "Group max [%d], " +
                            "Max Height [%d] " +
                            "Min Height [%d]",
                    entry, entityId, spawnChance, weight, groupCountMin, groupCountMax, maxHeight, minHeight));

            SPAWN_ENTRIES.add(entry);
            SpawnChances.put(typeClass, spawnChance);
            MaxHeight.put(typeClass, maxHeight);
            MinHeight.put(typeClass, minHeight);
        }
    }

    /**
     *
     */
    private static final AbstractSignalDataAccessor<WorldEvent.PotentialSpawns> EVENT_QUERY = new AbstractSignalDataAccessor<WorldEvent.PotentialSpawns>()
    {
        /**
         *
         * @param potentialSpawns
         * @return
         */
        @Override
        public World getWorld(WorldEvent.PotentialSpawns potentialSpawns)
        {
            return potentialSpawns.getWorld();
        }

        /**
         *
         * @param potentialSpawns
         * @return
         */
        @Override
        public BlockPos getPos(WorldEvent.PotentialSpawns potentialSpawns)
        {
            return potentialSpawns.getPos();
        }

        /**
         *
         * @param potentialSpawns
         * @return
         */
        @Override
        public BlockPos getValidBlockPos(WorldEvent.PotentialSpawns potentialSpawns)
        {
            return potentialSpawns.getPos().down();
        }

        /**
         *
         * @param potentialSpawns
         * @return
         */
        @Override
        public int getY(WorldEvent.PotentialSpawns potentialSpawns)
        {
            return potentialSpawns.getPos().getY();
        }

        /**
         *
         * @param potentialSpawns
         * @return
         */
        @Override
        public Entity getEntity(WorldEvent.PotentialSpawns potentialSpawns)
        {
            return null;
        }

        /**
         *
         * @param potentialSpawns
         * @return
         */
        @Override
        public DamageSource getSource(WorldEvent.PotentialSpawns potentialSpawns)
        {
            return null;
        }

        /**
         *
         * @param potentialSpawns
         * @return
         */
        @Override
        public Entity getAttacker(WorldEvent.PotentialSpawns potentialSpawns)
        {
            return null;
        }

        /**
         *
         * @param potentialSpawns
         * @return
         */
        @Override
        public EntityPlayerMP getPlayer(WorldEvent.PotentialSpawns potentialSpawns)
        {
            return null;
        }

        /**
         *
         * @param potentialSpawns
         * @return
         */
        @Override
        public ItemStack getItem(WorldEvent.PotentialSpawns potentialSpawns)
        {
            return ItemStack.EMPTY;
        }
    };

    /**
     *
     */
    static
    {
        FACTORY
                .attribute(Attribute.create(ID_RULE))

                .attribute(Attribute.create(MOB_NAME))

                .attribute(Attribute.create(MOB_WEIGHT))

                .attribute(Attribute.create(MOB_MAX_HEIGHT))

                .attribute(Attribute.create(MOB_MIN_HEIGHT))

                .attribute(Attribute.create(MOB_SPAWN_CHANCE))

                .attribute(Attribute.create(MOB_GROUP_COUNT_MIN))

                .attribute(Attribute.create(MOB_GROUP_COUNT_MAX))
        ;
    }

    /**
     *
     * @param jsonElement
     * @return
     */
    public static GenericPotentialSpawn parse(JsonElement jsonElement)
    {
        if (jsonElement == null)
        {
            return null;
        }
        else
        {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            if (!jsonObject.has(SingleKeyWord.MAIN_POTENTIAL_SPAWN.MAIN_STRUCT))
            {
                Log.writeDataToLogFile(0, "Not found 'struct' for rule [ { ... } ]");
                throw new RuntimeException();
            }

            AttributeMap<Object> attributeMap = FACTORY.parse(jsonElement);

            JsonArray jsonArray = jsonObject.getAsJsonArray(SingleKeyWord.MAIN_POTENTIAL_SPAWN.MAIN_STRUCT);

            if (jsonArray != null)
            {
                for (JsonElement jsonElement1 : jsonArray)
                {
                    AttributeMap<?> attributeMap1 = FACTORY.parse(jsonElement1);

                    attributeMap.addList(MOB_STRUCT, attributeMap1);
                }
            }

            return new GenericPotentialSpawn(attributeMap);
        }
    }

    /**
     *
     * @param id
     * @return
     */
    public static String fixEntityId(String id)
    {
        NBTTagCompound nbtXompound = new NBTTagCompound();

        nbtXompound.setString("id", id);

        nbtXompound = FIXER.fixTagCompound(nbtXompound);

        return nbtXompound.getString("id");
    }
}
