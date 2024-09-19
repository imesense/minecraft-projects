package org.imesense.dynamicspawncontrol.technical.eventprocessor.generic;

public final class GenericRightClickActions extends ListActionsSingleEvent<SignalDataGetter>
{
    private final Event.Result _result;

    private static int _countCreatedMaps = 0;

    private final ListActionsBinary _ruleEvaluator;

    public Event.Result getResult() { return _result; }

    /* TODO: удалить в будущем */
    private static final EventResults _abstractResult = new EventResults();

    public boolean match(PlayerInteractEvent.RightClickBlock event) { return _ruleEvaluator.match(event, EVENT_QUERY); }

    private GenericRightClickActions(AttributeMap<?> map, String nameClass)
    {
        super(nameClass);

        Log.writeDataToLogFile(Log._typeLog[0], String.format("Iterator for [%s] number [%d]", nameClass, _countCreatedMaps++));

        this._ruleEvaluator = new ListActionsBinary<>(map, nameClass);

        this.addActions(map);

        this._result = _abstractResult.getResult(map);
    }

    public static GenericRightClickActions parse(JsonElement element)
    {
        if (element == null)
        {
            return null;
        }
        else
        {
            AttributeMap<Object> map = ListActionsStaticFactoryMouse.FACTORY.parse(element);

            return new GenericRightClickActions(map, "GenericRightClickActions");
        }
    }

    private static final SignalDataAccessor<PlayerInteractEvent.RightClickBlock> EVENT_QUERY = new SignalDataAccessor<PlayerInteractEvent.RightClickBlock>()
    {
        @Override
        public World getWorld(PlayerInteractEvent.RightClickBlock RightClickBlock)
        {
            return RightClickBlock.getWorld();
        }

        @Override
        public BlockPos getPos(PlayerInteractEvent.RightClickBlock RightClickBlock)
        {
            return RightClickBlock.getPos();
        }

        @Override
        public BlockPos getValidBlockPos(PlayerInteractEvent.RightClickBlock RightClickBlock)
        {
            return RightClickBlock.getPos();
        }

        @Override
        public int getY(PlayerInteractEvent.RightClickBlock RightClickBlock)
        {
            return RightClickBlock.getPos().getY();
        }

        @Override
        public Entity getEntity(PlayerInteractEvent.RightClickBlock RightClickBlock)
        {
            return RightClickBlock.getEntityPlayer();
        }

        @Override
        public DamageSource getSource(PlayerInteractEvent.RightClickBlock RightClickBlock)
        {
            return null;
        }

        @Override
        public Entity getAttacker(PlayerInteractEvent.RightClickBlock RightClickBlock)
        {
            return null;
        }

        @Override
        public EntityPlayer getPlayer(PlayerInteractEvent.RightClickBlock RightClickBlock)
        {
            return RightClickBlock.getEntityPlayer();
        }

        @Override
        public ItemStack getItem(PlayerInteractEvent.RightClickBlock RightClickBlock)
        {
            return RightClickBlock.getItemStack();
        }
    };

    public void action(PlayerInteractEvent.RightClickBlock event)
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
