package org.imesense.dynamicspawncontrol.technical.eventprocessor.generic;

public final class GenericMapEffectsActions extends ListActionsSingleEvent<SignalDataGetter>
{
    private final int _timeout;

    private static int _countCreatedMaps = 0;

    public int getTimeout() { return _timeout; }

    private final ListActionsBinary _ruleEvaluator;

    private static final AttributeMapFactory<Object> FACTORY = new AttributeMapFactory<>();

    public boolean match(TickEvent.PlayerTickEvent event) { return _ruleEvaluator.match(event, EVENT_QUERY); }

    private GenericMapEffectsActions(AttributeMap<?> map, int timeout, String nameClass)
    {
        super(nameClass);

        Log.writeDataToLogFile(Log._typeLog[0], String.format("Iterator for [%s] number [%d]", nameClass, _countCreatedMaps++));

        this._ruleEvaluator = new ListActionsBinary<>(map, nameClass);

        this.addActions(map);

        this._timeout = timeout > 0 ? timeout : 1;
    }

    public static GenericMapEffectsActions parse(JsonElement element)
    {
        if (element == null)
        {
            return null;
        }
        else
        {
            AttributeMap<?> map = FACTORY.parse(element);

            int localTimeOut = element.getAsJsonObject().has(AuxFunctions.KeyWords.ACTION_TIMEOUT.getKeyword())
                    ? element.getAsJsonObject().get(AuxFunctions.KeyWords.ACTION_TIMEOUT.getKeyword()).getAsInt() : 20;

            return new GenericMapEffectsActions(map, localTimeOut, "GenericMapEffectsActions");
        }
    }

    private static final SignalDataAccessor<TickEvent.PlayerTickEvent> EVENT_QUERY = new SignalDataAccessor<TickEvent.PlayerTickEvent>()
    {
        @Override
        public World getWorld(TickEvent.PlayerTickEvent PlayerTickEvent)
        {
            return PlayerTickEvent.player.getEntityWorld();
        }

        @Override
        public BlockPos getPos(TickEvent.PlayerTickEvent PlayerTickEvent)
        {
            return PlayerTickEvent.player.getPosition();
        }

        @Override
        public BlockPos getValidBlockPos(TickEvent.PlayerTickEvent PlayerTickEvent)
        {
            return PlayerTickEvent.player.getPosition().down();
        }

        @Override
        public int getY(TickEvent.PlayerTickEvent PlayerTickEvent)
        {
            return PlayerTickEvent.player.getPosition().getY();
        }

        @Override
        public Entity getEntity(TickEvent.PlayerTickEvent PlayerTickEvent)
        {
            return PlayerTickEvent.player;
        }

        @Override
        public DamageSource getSource(TickEvent.PlayerTickEvent _PlayerTickEvent)
        {
            return null;
        }

        @Override
        public Entity getAttacker(TickEvent.PlayerTickEvent _PlayerTickEvent)
        {
            return null;
        }

        @Override
        public EntityPlayer getPlayer(TickEvent.PlayerTickEvent PlayerTickEvent)
        {
            return PlayerTickEvent.player;
        }

        @Override
        public ItemStack getItem(TickEvent.PlayerTickEvent _PlayerTickEvent)
        {
            return ItemStack.EMPTY;
        }
    };

    static
    {
        FACTORY
                .attribute(Attribute.create(ID_RULE))
                .attribute(Attribute.create(SEE_SKY))
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
                .attribute(Attribute.createMulti(ACTION_POTION))
                .attribute(Attribute.createMulti(ACTION_GIVE))
                .attribute(Attribute.createMulti(ACTION_DROP))
                .attribute(Attribute.create(ACTION_COMMAND))
                .attribute(Attribute.create(ACTION_FIRE))
                .attribute(Attribute.create(ACTION_EXPLOSION))
                .attribute(Attribute.create(ACTION_CLEAR))
                .attribute(Attribute.create(ACTION_DAMAGE))
                .attribute(Attribute.create(ACTION_SET_BLOCK))
                .attribute(Attribute.create(ACTION_SET_HELD_ITEM))
                .attribute(Attribute.create(ACTION_SET_HELD_AMOUNT))
        ;
    }

    public void action(TickEvent.PlayerTickEvent event)
    {
        SignalDataGetter eventBase = new SignalDataGetter()
        {
            @Override
            public EntityLivingBase getEntityLiving()
            {
                return event.player;
            }

            @Override
            public EntityPlayer getPlayer()
            {
                return event.player;
            }

            @Override
            public World getWorld()
            {
                return event.player.getEntityWorld();
            }

            @Override
            public Entity getEntity()
            {
                return null;
            }

            @Override
            public BlockPos getPosition()
            {
                return event.player.getPosition();
            }
        };

        for (Consumer<SignalDataGetter> action : _actions)
        {
            action.accept(eventBase);
        }
    }
}
