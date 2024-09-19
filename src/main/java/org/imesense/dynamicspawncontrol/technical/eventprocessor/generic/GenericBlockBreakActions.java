package org.imesense.dynamicspawncontrol.technical.eventprocessor.generic;

public final class GenericBlockBreakActions extends ListActionsSingleEvent<SignalDataGetter>
{
    private final Event.Result _result;

    private static int _countCreatedMaps = 0;

    private final ListActionsBinary _ruleEvaluator;

    public Event.Result getResult() { return _result; }

    /* TODO: убрать этот код в будущем */
    private static final EventResults _abstractResult = new EventResults();

    public boolean match(BlockEvent.BreakEvent event) { return _ruleEvaluator.match(event, EVENT_QUERY); }

    private GenericBlockBreakActions(AttributeMap<?> map, String nameClass)
    {
        super(nameClass);

        Log.writeDataToLogFile(Log._typeLog[0], String.format("Iterator for [%s] number [%d]", nameClass, _countCreatedMaps++));

        this._ruleEvaluator = new ListActionsBinary<>(map, nameClass);

        this.addActions(map);

        this._result = _abstractResult.getResult(map);
    }

    public static GenericBlockBreakActions parse(JsonElement element)
    {
        if (element == null)
        {
            return null;
        }
        else
        {
            AttributeMap<Object> map = ListActionsStaticFactoryBlocks.FACTORY.parse(element);

            return new GenericBlockBreakActions(map, "GenericBlockBreakActions");
        }
    }

    private static final SignalDataAccessor<BlockEvent.BreakEvent> EVENT_QUERY = new SignalDataAccessor<BlockEvent.BreakEvent>()
    {
        @Override
        public int getY(BlockEvent.BreakEvent data)
        {
            return data.getPos().getY();
        }

        @Override
        public World getWorld(BlockEvent.BreakEvent data)
        {
            return data.getWorld();
        }

        @Override
        public BlockPos getPos(BlockEvent.BreakEvent data)
        {
            return data.getPos();
        }

        @Override
        public Entity getEntity(BlockEvent.BreakEvent data)
        {
            return data.getPlayer();
        }

        @Override
        public ItemStack getItem(BlockEvent.BreakEvent data)
        {
            return ItemStack.EMPTY;
        }

        @Override
        public Entity getAttacker(BlockEvent.BreakEvent data)
        {
            return null;
        }

        @Override
        public EntityPlayer getPlayer(BlockEvent.BreakEvent data)
        {
            return data.getPlayer();
        }

        @Override
        public DamageSource getSource(BlockEvent.BreakEvent data)
        {
            return null;
        }

        @Override
        public BlockPos getValidBlockPos(BlockEvent.BreakEvent data)
        {
            return data.getPos();
        }
    };

    public void action(BlockEvent.BreakEvent event)
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
