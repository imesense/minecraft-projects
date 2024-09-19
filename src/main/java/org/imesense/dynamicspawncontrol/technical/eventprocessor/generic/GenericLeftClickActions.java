package org.imesense.dynamicspawncontrol.technical.eventprocessor.generic;

public final class GenericLeftClickActions extends ListActionsSingleEvent<SignalDataGetter>
{
    private final Event.Result _result;

    private static int _countCreatedMaps = 0;

    private final ListActionsBinary _ruleEvaluator;

    public Event.Result getResult() { return _result; }

    /* TODO: удалить в будущем */
    private static final EventResults _abstractResult = new EventResults();

    public boolean match(PlayerInteractEvent.LeftClickBlock event) { return _ruleEvaluator.match(event, EVENT_QUERY); }

    private GenericLeftClickActions(AttributeMap<?> map, String nameClass)
    {
        super(nameClass);
        Log.writeDataToLogFile(Log._typeLog[0], String.format("Iterator for [%s] number [%d]", nameClass, _countCreatedMaps++));

        this._ruleEvaluator = new ListActionsBinary<>(map, nameClass);

        this.addActions(map);

        this._result = _abstractResult.getResult(map);
    }

    public static GenericLeftClickActions parse(JsonElement element)
    {
        if (element == null)
        {
            return null;
        }
        else
        {
            AttributeMap<Object> map = ListActionsStaticFactoryMouse.FACTORY.parse(element);

            return new GenericLeftClickActions(map, "GenericLeftClickActions");
        }
    }

    private static final SignalDataAccessor<PlayerInteractEvent.LeftClickBlock> EVENT_QUERY = new SignalDataAccessor<PlayerInteractEvent.LeftClickBlock>()
    {
        @Override
        public World getWorld(PlayerInteractEvent.LeftClickBlock LeftClickBlock)
        {
            return LeftClickBlock.getWorld();
        }

        @Override
        public BlockPos getPos(PlayerInteractEvent.LeftClickBlock LeftClickBlock)
        {
            return LeftClickBlock.getPos();
        }

        @Override
        public BlockPos getValidBlockPos(PlayerInteractEvent.LeftClickBlock LeftClickBlock)
        {
            return LeftClickBlock.getPos();
        }

        @Override
        public int getY(PlayerInteractEvent.LeftClickBlock LeftClickBlock)
        {
            return LeftClickBlock.getPos().getY();
        }

        @Override
        public Entity getEntity(PlayerInteractEvent.LeftClickBlock LeftClickBlock)
        {
            return LeftClickBlock.getEntityPlayer();
        }

        @Override
        public DamageSource getSource(PlayerInteractEvent.LeftClickBlock LeftClickBlock)
        {
            return null;
        }

        @Override
        public Entity getAttacker(PlayerInteractEvent.LeftClickBlock LeftClickBlock)
        {
            return null;
        }

        @Override
        public EntityPlayer getPlayer(PlayerInteractEvent.LeftClickBlock LeftClickBlock)
        {
            return LeftClickBlock.getEntityPlayer();
        }

        @Override
        public ItemStack getItem(PlayerInteractEvent.LeftClickBlock LeftClickBlock)
        {
            return LeftClickBlock.getItemStack();
        }
    };

    public void action(PlayerInteractEvent.LeftClickBlock event)
    {
        SignalDataGetter eventBase = new SignalDataGetter()
        {
            @Override
            public EntityLivingBase getEntityLiving()
            {
                return event.getEntityPlayer();
            }

            @Override
            public EntityPlayer getPlayer()
            {
                return event.getEntityPlayer();
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
                return event.getPos();
            }
        };

        for (Consumer<SignalDataGetter> action : _actions)
        {
            action.accept(eventBase);
        }
    }
}
