package org.imesense.dynamicspawncontrol.technical.eventprocessor.generic;

public final class GenericOverrideSpawn extends ListActionsSingleEvent<SignalDataGetter>
{
    private static int _countCreatedMaps = 0;

    private final ListActionsBinary _ruleEvaluator;

    public static final EntityId FIXER = new EntityId();

    private final List<Class<?>> _toRemoveMobs = new ArrayList<>();

    public List<Class<?>> getToRemoveMobs() { return _toRemoveMobs; }

    private final List<Biome.SpawnListEntry> _spawnEntries = new ArrayList<>();

    public List<Biome.SpawnListEntry> getSpawnEntries() { return _spawnEntries; }

    public HashMap<Class<? extends Entity>, Integer> _maxHeight = new HashMap<>();

    public HashMap<Class<? extends Entity>, Integer> _minHeight = new HashMap<>();

    public HashMap<Class<? extends Entity>, Float> _spawnChances = new HashMap<>();

    private static final AttributeMapFactory<Object> FACTORY = new AttributeMapFactory<>();

    public boolean match(WorldEvent.PotentialSpawns event) { return _ruleEvaluator.match(event, EVENT_QUERY); }

    public float getMinHeightChance(Class<? extends Entity> entityClass) { return _minHeight.getOrDefault(entityClass, 5); }

    public float getMaxHeightChance(Class<? extends Entity> entityClass) { return _maxHeight.getOrDefault(entityClass, 255); }

    public float getSpawnChance(Class<? extends Entity> entityClass) { return _spawnChances.getOrDefault(entityClass, 0.0f); }

    private GenericOverrideSpawn(AttributeMap<?> map, String nameClass)
    {
        super(nameClass);

        Log.writeDataToLogFile(Log._typeLog[0], String.format("Iterator for [%s] number [%d]", nameClass, _countCreatedMaps++));

        this._ruleEvaluator = new ListActionsBinary<>(map, nameClass);

        for (AttributeMap<?> mobMap : map.getListA(MOB_STRUCT))
        {
            String entityId = fixEntityId((String) mobMap.get(MOB_NAME));
            EntityEntry typeEntity = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(entityId));
            Class<? extends Entity> typeClass = typeEntity == null ? null : typeEntity.getEntityClass();

            if (typeClass == null)
            {
                Log.writeDataToLogFile(Log._typeLog[2], "Cannot find mob '" + mobMap.get(MOB_NAME) + "'");
                throw new RuntimeException();
            }

            int weight = AuxFunctions.checkParameter(mobMap, MOB_WEIGHT, 1, 100, "frequency");
            int groupCountMin = AuxFunctions.checkParameter(mobMap, MOB_GROUP_COUNT_MIN, 1, 10, "group_count_min");
            int groupCountMax = AuxFunctions.checkParameter(mobMap, MOB_GROUP_COUNT_MAX, 1, 20, "group_count_max");
            float spawnChance = AuxFunctions.checkParameter(mobMap, MOB_SPAWN_CHANCE, 0.01f, 1.0f, "spawnChanceValue");
            int maxHeight = AuxFunctions.checkParameter(mobMap, MOB_MAX_HEIGHT, 5, 255, "max_height");
            int minHeight = AuxFunctions.checkParameter(mobMap, MOB_MIN_HEIGHT, 5, 255, "min_height");

            Biome.SpawnListEntry entry = new Biome.SpawnListEntry((Class<? extends EntityLiving>) typeClass, weight, groupCountMin, groupCountMax);

            Log.writeDataToLogFile(Log._typeLog[0], String.format(
                    "Entity [%s:%s] has been added to the spawn list. " +
                            "Data -> SpawnChance [%f], " +
                            "Frequency [%d], " +
                            "Group min [%d], " +
                            "Group max [%d], " +
                            "Max Height [%d] " +
                            "Min Height [%d]",
                    entry, entityId, spawnChance, weight, groupCountMin, groupCountMax, maxHeight, minHeight));

            _spawnEntries.add(entry);
            _spawnChances.put(typeClass, spawnChance);
            _maxHeight.put(typeClass, maxHeight);
            _minHeight.put(typeClass, minHeight);
        }
    }

    private static final SignalDataAccessor<WorldEvent.PotentialSpawns> EVENT_QUERY = new SignalDataAccessor<WorldEvent.PotentialSpawns>()
    {
        @Override
        public World getWorld(WorldEvent.PotentialSpawns PotentialSpawns)
        {
            return PotentialSpawns.getWorld();
        }

        @Override
        public BlockPos getPos(WorldEvent.PotentialSpawns PotentialSpawns)
        {
            return PotentialSpawns.getPos();
        }

        @Override
        public BlockPos getValidBlockPos(WorldEvent.PotentialSpawns PotentialSpawns)
        {
            return PotentialSpawns.getPos().down();
        }

        @Override
        public int getY(WorldEvent.PotentialSpawns PotentialSpawns)
        {
            return PotentialSpawns.getPos().getY();
        }

        @Override
        public Entity getEntity(WorldEvent.PotentialSpawns PotentialSpawns)
        {
            return null;
        }

        @Override
        public DamageSource getSource(WorldEvent.PotentialSpawns PotentialSpawns)
        {
            return null;
        }

        @Override
        public Entity getAttacker(WorldEvent.PotentialSpawns PotentialSpawns)
        {
            return null;
        }

        @Override
        public EntityPlayer getPlayer(WorldEvent.PotentialSpawns PotentialSpawns)
        {
            return null;
        }

        @Override
        public ItemStack getItem(WorldEvent.PotentialSpawns PotentialSpawns)
        {
            return ItemStack.EMPTY;
        }
    };


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

    public static GenericOverrideSpawn parse(JsonElement element)
    {
        if (element == null)
        {
            return null;
        }
        else
        {
            JsonObject jsonObject = element.getAsJsonObject();

            if (!jsonObject.has(AuxFunctions.KeyWords.ACTION_OVERRIDE_STRUCT.getKeyword()))
            {
                Log.writeDataToLogFile(Log._typeLog[0], "Not found 'struct' for rule [ { ... } ]");
                throw new RuntimeException();
            }

            AttributeMap<Object> map = FACTORY.parse(element);

            JsonArray mobs = jsonObject.getAsJsonArray
                    (AuxFunctions.KeyWords.ACTION_OVERRIDE_STRUCT.getKeyword());

            if (mobs != null)
            {
                for (JsonElement mob : mobs)
                {
                    AttributeMap<?> mobMap = FACTORY.parse(mob);
                    map.addList(KeyWordsGeneral.PotentialSpawn.MOB_STRUCT, mobMap);
                }
            }

            return new GenericOverrideSpawn(map, "GenericOverrideSpawn");
        }
    }

    public static String fixEntityId(String id)
    {
        NBTTagCompound nbtXompound = new NBTTagCompound();

        nbtXompound.setString("id", id);

        nbtXompound = FIXER.fixTagCompound(nbtXompound);

        return nbtXompound.getString("id");
    }
}
