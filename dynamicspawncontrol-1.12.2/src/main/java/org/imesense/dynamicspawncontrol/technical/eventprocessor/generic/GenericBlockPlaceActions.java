package org.imesense.dynamicspawncontrol.technical.eventprocessor.generic;

public final class GenericBlockPlaceActions extends ListActionsSingleEvent<SignalDataGetter>
{
    private final Event.Result _result;

    private static int _countCreatedMaps = 0;

    private final ListActionsBinary _ruleEvaluator;

    public Event.Result getResult() { return _result; }

    /* TODO: удалить в будущем */
    private static final EventResults _abstractResult = new EventResults();

    @Deprecated
    public boolean match(BlockEvent.PlaceEvent event) { return _ruleEvaluator.match(event, EVENT_QUERY); }

    private GenericBlockPlaceActions(AttributeMap<?> map, String nameClass)
    {
        super(nameClass);

        Log.writeDataToLogFile(Log._typeLog[0], String.format("Iterator for [%s] number [%d]", nameClass, _countCreatedMaps++));

        this._ruleEvaluator = new ListActionsBinary<>(map, nameClass);

        this.addActions(map);

        this._result = _abstractResult.getResult(map);
    }

    public static GenericBlockPlaceActions parse(JsonElement element)
    {
        if (element == null)
        {
            return null;
        }
        else
        {
            AttributeMap<Object> map = ListActionsStaticFactoryBlocks.FACTORY.parse(element);

            return new GenericBlockPlaceActions(map, "GenericBlockPlaceActions");
        }
    }

    @Deprecated
    private static final SignalDataAccessor<BlockEvent.PlaceEvent> EVENT_QUERY = new SignalDataAccessor<BlockEvent.PlaceEvent>()
    {
        @Override
        public int getY(BlockEvent.PlaceEvent data)
        {
            return data.getPos().getY();
        }

        @Override
        public World getWorld(BlockEvent.PlaceEvent data)
        {
            return data.getWorld();
        }

        @Override
        public BlockPos getPos(BlockEvent.PlaceEvent data)
        {
            return data.getPos();
        }

        @Override
        public Entity getEntity(BlockEvent.PlaceEvent data)
        {
            return data.getPlayer();
        }

        @Override
        public ItemStack getItem(BlockEvent.PlaceEvent data)
        {
            return data.getItemInHand();
        }

        @Override
        public Entity getAttacker(BlockEvent.PlaceEvent data)
        {
            return null;
        }

        @Override
        public EntityPlayer getPlayer(BlockEvent.PlaceEvent data)
        {
            return data.getPlayer();
        }

        @Override
        public DamageSource getSource(BlockEvent.PlaceEvent data)
        {
            return null;
        }

        @Override
        public BlockPos getValidBlockPos(BlockEvent.PlaceEvent data)
        {
            return data.getPos();
        }
    };

    @Deprecated
    public void action(BlockEvent.PlaceEvent event)
    {
        SignalDataGetter eventBase = new SignalDataGetter()
        {
            @Override
            public EntityLivingBase getEntityLiving()
            {
                return event.getPlayer();
            }

            @Override
            public EntityPlayer getPlayer()
            {
                return event.getPlayer();
            }

            @Override
            public World getWorld()
            {
                return event.getWorld();
            }

            @Override
            public Entity getEntity()
            {
                return null;
            }

            @Override
            public BlockPos getPosition()
            {
                return event.getPos();
            }
        };

        for (Consumer<SignalDataGetter> action : _actions)
        {
            action.accept(eventBase);
        }
    }
}
