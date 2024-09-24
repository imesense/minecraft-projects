package org.imesense.dynamicspawncontrol.technical.eventprocessor.generic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
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
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.attributefactory.Attribute;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeMap;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeMapFactory;
import org.imesense.dynamicspawncontrol.technical.customlibrary.*;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.signal.SignalDataAccessor;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.signal.SignalDataGetter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.imesense.dynamicspawncontrol.technical.customlibrary.MultipleKeyWords.CommonKeyWorlds.*;
import static org.imesense.dynamicspawncontrol.technical.customlibrary.MultipleKeyWords.PotentialSpawn.*;

/**
 *
 */
public final class GenericOverrideSpawn extends ListActionsConsumer<SignalDataGetter>
{
    /**
     *
     */
    private static int countCreatedMaps = 0;

    /**
     *
     */
    private final ListActionsBinary RULE_EVALUATOR;

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
     * @param event
     * @return
     */
    public boolean match(WorldEvent.PotentialSpawns event) { return RULE_EVALUATOR.match(event, EVENT_QUERY); }

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
     * @param map
     */
    private GenericOverrideSpawn(AttributeMap<?> map)
    {
        super();

        Log.writeDataToLogFile(0, String.format("Iterator for [%s] number [%d]", GenericOverrideSpawn.class.getName(), countCreatedMaps++));

        this.RULE_EVALUATOR = new ListActionsBinary<>(map);

        for (AttributeMap<?> mobMap : map.getListA(MOB_STRUCT))
        {
            String entityId = fixEntityId((String) mobMap.get(MOB_NAME));
            EntityEntry typeEntity = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(entityId));
            Class<? extends Entity> typeClass = typeEntity == null ? null : typeEntity.getEntityClass();

            if (typeClass == null)
            {
                Log.writeDataToLogFile(2, "Cannot find mob '" + mobMap.get(MOB_NAME) + "'");
                throw new RuntimeException();
            }

            int weight = CodeGenericUtils.checkParameter(mobMap, MOB_WEIGHT, 1, 100, "frequency");
            int groupCountMin = CodeGenericUtils.checkParameter(mobMap, MOB_GROUP_COUNT_MIN, 1, 10, "group_count_min");
            int groupCountMax = CodeGenericUtils.checkParameter(mobMap, MOB_GROUP_COUNT_MAX, 1, 20, "group_count_max");
            float spawnChance = CodeGenericUtils.checkParameter(mobMap, MOB_SPAWN_CHANCE, 0.01f, 1.0f, "spawnChanceValue");
            int maxHeight = CodeGenericUtils.checkParameter(mobMap, MOB_MAX_HEIGHT, 5, 255, "max_height");
            int minHeight = CodeGenericUtils.checkParameter(mobMap, MOB_MIN_HEIGHT, 5, 255, "min_height");

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
    private static final SignalDataAccessor<WorldEvent.PotentialSpawns> EVENT_QUERY = new SignalDataAccessor<WorldEvent.PotentialSpawns>()
    {
        /**
         *
         * @param PotentialSpawns
         * @return
         */
        @Override
        public World getWorld(WorldEvent.PotentialSpawns PotentialSpawns)
        {
            return PotentialSpawns.getWorld();
        }

        /**
         *
         * @param PotentialSpawns
         * @return
         */
        @Override
        public BlockPos getPos(WorldEvent.PotentialSpawns PotentialSpawns)
        {
            return PotentialSpawns.getPos();
        }

        /**
         *
         * @param PotentialSpawns
         * @return
         */
        @Override
        public BlockPos getValidBlockPos(WorldEvent.PotentialSpawns PotentialSpawns)
        {
            return PotentialSpawns.getPos().down();
        }

        /**
         *
         * @param PotentialSpawns
         * @return
         */
        @Override
        public int getY(WorldEvent.PotentialSpawns PotentialSpawns)
        {
            return PotentialSpawns.getPos().getY();
        }

        /**
         *
         * @param PotentialSpawns
         * @return
         */
        @Override
        public Entity getEntity(WorldEvent.PotentialSpawns PotentialSpawns)
        {
            return null;
        }

        /**
         *
         * @param PotentialSpawns
         * @return
         */
        @Override
        public DamageSource getSource(WorldEvent.PotentialSpawns PotentialSpawns)
        {
            return null;
        }

        /**
         *
         * @param PotentialSpawns
         * @return
         */
        @Override
        public Entity getAttacker(WorldEvent.PotentialSpawns PotentialSpawns)
        {
            return null;
        }

        /**
         *
         * @param PotentialSpawns
         * @return
         */
        @Override
        public EntityPlayer getPlayer(WorldEvent.PotentialSpawns PotentialSpawns)
        {
            return null;
        }

        /**
         *
         * @param PotentialSpawns
         * @return
         */
        @Override
        public ItemStack getItem(WorldEvent.PotentialSpawns PotentialSpawns)
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
     * @param element
     * @return
     */
    public static GenericOverrideSpawn parse(JsonElement element)
    {
        if (element == null)
        {
            return null;
        }
        else
        {
            JsonObject jsonObject = element.getAsJsonObject();

            if (!jsonObject.has(SingleKeyWords.MAIN_OVERRIDE_SPAWN.OVERRIDE_STRUCT))
            {
                Log.writeDataToLogFile(0, "Not found 'struct' for rule [ { ... } ]");
                throw new RuntimeException();
            }

            AttributeMap<Object> map = FACTORY.parse(element);

            JsonArray mobs = jsonObject.getAsJsonArray(SingleKeyWords.MAIN_OVERRIDE_SPAWN.OVERRIDE_STRUCT);

            if (mobs != null)
            {
                for (JsonElement mob : mobs)
                {
                    AttributeMap<?> mobMap = FACTORY.parse(mob);
                    map.addList(MultipleKeyWords.PotentialSpawn.MOB_STRUCT, mobMap);
                }
            }

            return new GenericOverrideSpawn(map);
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
