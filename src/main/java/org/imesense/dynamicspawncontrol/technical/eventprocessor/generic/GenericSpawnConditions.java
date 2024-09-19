package org.imesense.dynamicspawncontrol.technical.eventprocessor.generic;

public final class GenericSpawnConditions extends ListActionsSingleEvent<SignalDataGetter>
{
    private static int _countCreatedMaps = 0;

    private final ListActionsBinary _ruleEvaluator;

    private static final AttributeMapFactory<Object> FACTORY = new AttributeMapFactory<>();

    public boolean match(LivingSpawnEvent.CheckSpawn event) { return _ruleEvaluator.match(event, EVENT_QUERY); }

    private GenericSpawnConditions(AttributeMap<?> map, String nameClass)
    {
        super(nameClass);

        Log.writeDataToLogFile(Log._typeLog[0], String.format("Iterator for [%s] number [%d]", nameClass, _countCreatedMaps++));

        this._ruleEvaluator = new ListActionsBinary<>(map, nameClass);

        this.addActions(map);
    }

    public static GenericSpawnConditions parse(JsonElement element)
    {
        if (element == null)
        {
            return null;
        }
        else
        {
            AttributeMap<?> map = FACTORY.parse(element);

            return new GenericSpawnConditions(map, "GenericSpawnConditions");
        }
    }

    private static final SignalDataAccessor<LivingSpawnEvent.CheckSpawn> EVENT_QUERY = new SignalDataAccessor<LivingSpawnEvent.CheckSpawn>()
    {
        @Override
        public World getWorld(LivingSpawnEvent.CheckSpawn CheckSpawn)
        {
            return CheckSpawn.getWorld();
        }

        @Override
        public BlockPos getPos(LivingSpawnEvent.CheckSpawn CheckSpawn)
        {
            return new BlockPos(CheckSpawn.getX(), CheckSpawn.getY(), CheckSpawn.getZ());
        }

        @Override
        public BlockPos getValidBlockPos(LivingSpawnEvent.CheckSpawn CheckSpawn)
        {
            return new BlockPos(CheckSpawn.getX(), CheckSpawn.getY() - 1, CheckSpawn.getZ());
        }

        @Override
        public int getY(LivingSpawnEvent.CheckSpawn CheckSpawn)
        {
            return (int) CheckSpawn.getY();
        }

        @Override
        public Entity getEntity(LivingSpawnEvent.CheckSpawn CheckSpawn)
        {
            return CheckSpawn.getEntity();
        }

        @Override
        public DamageSource getSource(LivingSpawnEvent.CheckSpawn CheckSpawn)
        {
            return null;
        }

        @Override
        public Entity getAttacker(LivingSpawnEvent.CheckSpawn CheckSpawn)
        {
            return null;
        }

        @Override
        public EntityPlayer getPlayer(LivingSpawnEvent.CheckSpawn CheckSpawn)
        {
            return getClosestPlayer(CheckSpawn.getWorld(), new BlockPos(CheckSpawn.getX(), CheckSpawn.getY(), CheckSpawn.getZ()));
        }

        @Override
        public ItemStack getItem(LivingSpawnEvent.CheckSpawn CheckSpawn)
        {
            return ItemStack.EMPTY;
        }
    };

    private static EntityPlayer getClosestPlayer(World world, BlockPos blockPos)
    {
        return world.getClosestPlayer(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 100, false);
    }

    static
    {
        FACTORY
                .attribute(Attribute.create(ID_RULE))
                .attribute(Attribute.create(SEE_SKY))
                .attribute(Attribute.create(CAN_SPAWN_HERE))
                .attribute(Attribute.create(NOT_COLLIDING))
                .attribute(Attribute.create(SPAWNER))
                .attribute(Attribute.create(WEATHER))
                .attribute(Attribute.create(STRUCTURE))
                .attribute(Attribute.createMulti(BIOMES))
                .attribute(Attribute.createMulti(BIOMES_TYPE))
                .attribute(Attribute.createMulti(DIMENSION))

                .attribute(Attribute.createMulti(HELMET))
                .attribute(Attribute.createMulti(CHEST_PLATE))
                .attribute(Attribute.createMulti(LEGGINGS))
                .attribute(Attribute.createMulti(BOOTS))

                .attribute(Attribute.create(MIN_TIME))
                .attribute(Attribute.create(MAX_TIME))

                .attribute(Attribute.create(MIN_LIGHT))
                .attribute(Attribute.create(MAX_LIGHT))

                .attribute(Attribute.create(MIN_HEIGHT))
                .attribute(Attribute.create(MAX_HEIGHT))

                .attribute(Attribute.create(DIFFICULTY))
                .attribute(Attribute.create(MIN_DIFFICULTY))
                .attribute(Attribute.create(MAX_DIFFICULTY))

                .attribute(Attribute.create(MIN_SPAWN_DIST))
                .attribute(Attribute.create(MAX_SPAWN_DIST))

                .attribute(Attribute.createMulti(BLOCK))
                .attribute(Attribute.create(BLOCK_OFFSET))

                .attribute(Attribute.create(GET_MOON_PHASE))

                .attribute(Attribute.createMulti(MOB))

                .attribute(Attribute.create(ANIMALS))
                .attribute(Attribute.create(MONSTERS))

                .attribute(Attribute.createMulti(HELD_ITEM))
                .attribute(Attribute.createMulti(PLAYER_HELD_ITEM))
                .attribute(Attribute.createMulti(OFF_HAND_ITEM))
                .attribute(Attribute.createMulti(BOTH_HANDS_ITEM))

                .attribute(Attribute.create(RANDOM_KEY_0))
                .attribute(Attribute.create(RANDOM_KEY_1))
                .attribute(Attribute.create(RANDOM_KEY_2))
                .attribute(Attribute.create(RANDOM_KEY_3))
                .attribute(Attribute.create(RANDOM_KEY_4))

                .attribute(Attribute.create(ACTION_MESSAGE))
                .attribute(Attribute.create(ACTION_ANGRY))
                .attribute(Attribute.createMulti(ACTION_HELD_ITEM))
                .attribute(Attribute.createMulti(ACTION_ARMOR_BOOTS))
                .attribute(Attribute.createMulti(ACTION_ARMOR_LEGS))
                .attribute(Attribute.createMulti(ACTION_ARMOR_CHEST))
                .attribute(Attribute.createMulti(ACTION_ARMOR_HELMET))
                .attribute(Attribute.create(ACTION_SET_NBT))
                .attribute(Attribute.create(ACTION_HEALTH_MULTIPLY))
                .attribute(Attribute.create(ACTION_HEALTH_ADD))
                .attribute(Attribute.create(ACTION_SPEED_MULTIPLY))
                .attribute(Attribute.create(ACTION_SPEED_ADD))
                .attribute(Attribute.create(ACTION_DAMAGE_MULTIPLY))
                .attribute(Attribute.create(ACTION_DAMAGE_ADD))
                .attribute(Attribute.create(ACTION_CUSTOM_NAME))
                .attribute(Attribute.createMulti(ACTION_POTION))
        ;
    }

    public void action(LivingSpawnEvent.CheckSpawn event)
    {
        SignalDataGetter eventBase = new SignalDataGetter()
        {
            @Override
            public EntityLivingBase getEntityLiving()
            {
                return event.getEntityLiving();
            }

            @Override
            public EntityPlayer getPlayer()
            {
                return null;
            }

            @Override
            public World getWorld()
            {
                return event.getWorld();
            }

            @Override
            public Entity getEntity()
            {
                return event.getEntity();
            }

            @Override
            public BlockPos getPosition()
            {
                return event.getEntityLiving().getPosition();
            }
        };

        for (Consumer<SignalDataGetter> action : _actions)
        {
            action.accept(eventBase);
        }
    }
}
