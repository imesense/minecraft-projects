package org.imesense.dynamicspawncontrol.technical.eventprocessor.generic;

public final class GenericMobsTaskManager extends ListActionsSingleEvent<SignalDataGetter>
{
    private static int _countCreatedMaps = 0;

    private final ListActionsBinary _ruleEvaluator;

    private static final AttributeMapFactory<Object> FACTORY = new AttributeMapFactory<>();

    public boolean match(EntityJoinWorldEvent event) { return _ruleEvaluator.match(event, EVENT_QUERY_JOIN); }

    private GenericMobsTaskManager(AttributeMap<?> map, String nameClass)
    {
        super(nameClass);

        Log.writeDataToLogFile(Log._typeLog[0], String.format("Iterator for [%s] number [%d]", nameClass, _countCreatedMaps++));

        this._ruleEvaluator = new ListActionsBinary<>(map, nameClass);

        this.addActions(map);
    }

    public static GenericMobsTaskManager parse(JsonElement element)
    {
        if (element == null)
        {
            return null;
        }
        else
        {
            AttributeMap<?> map = FACTORY.parse(element);

            return new GenericMobsTaskManager(map, "GenericMobsTaskManager");
        }
    }

    public static final SignalDataAccessor<EntityJoinWorldEvent> EVENT_QUERY_JOIN = new SignalDataAccessor<EntityJoinWorldEvent>()
    {
        @Override
        public World getWorld(EntityJoinWorldEvent EntityJoinWorldEvent)
        {
            return EntityJoinWorldEvent.getWorld();
        }

        @Override
        public BlockPos getPos(EntityJoinWorldEvent EntityJoinWorldEvent)
        {
            return EntityJoinWorldEvent.getEntity().getPosition();
        }

        @Override
        public BlockPos getValidBlockPos(EntityJoinWorldEvent EntityJoinWorldEvent)
        {
            return EntityJoinWorldEvent.getEntity().getPosition().down();
        }

        @Override
        public int getY(EntityJoinWorldEvent EntityJoinWorldEvent)
        {
            return EntityJoinWorldEvent.getEntity().getPosition().getY();
        }

        @Override
        public Entity getEntity(EntityJoinWorldEvent EntityJoinWorldEvent)
        {
            return EntityJoinWorldEvent.getEntity();
        }

        @Override
        public DamageSource getSource(EntityJoinWorldEvent EntityJoinWorldEvent)
        {
            return null;
        }

        @Override
        public Entity getAttacker(EntityJoinWorldEvent EntityJoinWorldEvent)
        {
            return null;
        }

        @Override
        public EntityPlayer getPlayer(EntityJoinWorldEvent EntityJoinWorldEvent)
        {
            return getClosestPlayer(EntityJoinWorldEvent.getWorld(), EntityJoinWorldEvent.getEntity().getPosition());
        }

        @Override
        public ItemStack getItem(EntityJoinWorldEvent EntityJoinWorldEvent)
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

                .attribute(Attribute.createMulti(DIMENSION))

                .attribute(Attribute.create(GET_MOON_PHASE))

                .attribute(Attribute.createMulti(ENEMIES_TO))

                .attribute(Attribute.createMulti(ENEMY_ID))

                .attribute(Attribute.createMulti(PANIC_TO))

                .attribute(Attribute.createMulti(PANIC_ID))

                .attribute(Attribute.createMulti(TO_THEM))

                .attribute(Attribute.createMulti(THEM_ID))
        ;
    }

    public void action(EntityJoinWorldEvent event)
    {
        SignalDataGetter eventBase = new SignalDataGetter()
        {
            @Override
            public EntityLivingBase getEntityLiving()
            {
                return event.getEntity() instanceof EntityLivingBase ? (EntityLivingBase) event.getEntity() : null;
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
                return event.getEntity() != null ? event.getEntity().getPosition() : null;
            }
        };

        for (Consumer<SignalDataGetter> action : _actions)
        {
            action.accept(eventBase);
        }
    }
}
