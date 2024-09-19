package org.imesense.dynamicspawncontrol.technical.eventprocessor.generic;

public final class GenericExperience extends ListActionsSingleEvent<SignalDataGetter>
{
    private final int _xp;

    private final float _multiXp;

    private final float _addingXp;

    private final Event.Result _result;

    private static int _countCreatedMaps = 0;

    private final ListActionsBinary _ruleEvaluator;

    public Event.Result getResult() { return _result; }

    /* TODO: удалить этот код в будущем */
    private static final EventResults _abstractResult = new EventResults();

    private static final AttributeMapFactory<Object> FACTORY = new AttributeMapFactory<>();

    public boolean match(LivingExperienceDropEvent event) { return _ruleEvaluator.match(event, EVENT_QUERY); }

    private GenericExperience(AttributeMap<?> map, String nameClass, int xp, float multiXp, float addingXp)
    {
        super(nameClass);

        Log.writeDataToLogFile(Log._typeLog[0], String.format("Iterator for [%s] number [%d]", nameClass, _countCreatedMaps++));

        this._ruleEvaluator = new ListActionsBinary<>(map, nameClass);

        this.addActions(map);

        this._result = _abstractResult.getResult(map);

        this._xp = xp;
        this._multiXp = multiXp;
        this._addingXp = addingXp;
    }

    public static GenericExperience parse(JsonElement element)
    {
        if (element == null)
        {
            return null;
        }
        else
        {
            AttributeMap<?> map = FACTORY.parse(element);

            int localSetXp = AuxFunctions.getValueFromJson(
                    element.getAsJsonObject(),
                    AuxFunctions.KeyWords.ACTION_SET_XP.getKeyword(),
                    0,
                    (_element, defaultValue) ->
                            _element.getAsJsonPrimitive().isNumber() ? _element.getAsInt() : defaultValue
            );

            float localMultiXp = AuxFunctions.getValueFromJson(
                    element.getAsJsonObject(),
                    AuxFunctions.KeyWords.ACTION_MULTI_XP.getKeyword(),
                    0.f,
                    (_element, defaultValue) ->
                            _element.getAsJsonPrimitive().isNumber() ? _element.getAsFloat() : defaultValue
            );

            float localAddXp = AuxFunctions.getValueFromJson(
                    element.getAsJsonObject(),
                    AuxFunctions.KeyWords.ACTION_ADD_XP.getKeyword(),
                    0.f,
                    (_element, defaultValue) ->
                            _element.getAsJsonPrimitive().isNumber() ? _element.getAsFloat() : defaultValue
            );

            return new GenericExperience(map, "GenericExperience", localSetXp, localMultiXp, localAddXp);
        }
    }

    private static final SignalDataAccessor<LivingExperienceDropEvent> EVENT_QUERY = new SignalDataAccessor<LivingExperienceDropEvent>()
    {
        @Override
        public World getWorld(LivingExperienceDropEvent LivingExperienceDropEvent)
        {
            return LivingExperienceDropEvent.getEntity().getEntityWorld();
        }

        @Override
        public BlockPos getPos(LivingExperienceDropEvent LivingExperienceDropEvent)
        {
            return LivingExperienceDropEvent.getEntity().getPosition();
        }

        @Override
        public BlockPos getValidBlockPos(LivingExperienceDropEvent LivingExperienceDropEvent)
        {
            return LivingExperienceDropEvent.getEntity().getPosition().down();
        }

        @Override
        public int getY(LivingExperienceDropEvent LivingExperienceDropEvent)
        {
            return LivingExperienceDropEvent.getEntity().getPosition().getY();
        }

        @Override
        public Entity getEntity(LivingExperienceDropEvent LivingExperienceDropEvent)
        {
            return LivingExperienceDropEvent.getEntity();
        }

        @Override
        public DamageSource getSource(LivingExperienceDropEvent LivingExperienceDropEvent)
        {
            return null;
        }

        @Override
        public Entity getAttacker(LivingExperienceDropEvent LivingExperienceDropEvent)
        {
            return LivingExperienceDropEvent.getAttackingPlayer();
        }

        @Override
        public EntityPlayer getPlayer(LivingExperienceDropEvent LivingExperienceDropEvent)
        {
            return LivingExperienceDropEvent.getAttackingPlayer();
        }

        @Override
        public ItemStack getItem(LivingExperienceDropEvent LivingExperienceDropEvent)
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

                .attribute(Attribute.create(PLAYER))
                .attribute(Attribute.create(FAKE_PLAYER))
                .attribute(Attribute.create(REAL_PLAYER))
                .attribute(Attribute.createMulti(HELD_ITEM))
                .attribute(Attribute.createMulti(PLAYER_HELD_ITEM))
                .attribute(Attribute.createMulti(OFF_HAND_ITEM))
                .attribute(Attribute.createMulti(BOTH_HANDS_ITEM))

                .attribute(Attribute.create(PROJECTILE))
                .attribute(Attribute.create(EXPLOSION))
                .attribute(Attribute.create(FIRE))
                .attribute(Attribute.create(MAGIC))
                .attribute(Attribute.createMulti(SOURCE))

                .attribute(Attribute.create(RANDOM_KEY_0))
                .attribute(Attribute.create(RANDOM_KEY_1))
                .attribute(Attribute.create(RANDOM_KEY_2))
                .attribute(Attribute.create(RANDOM_KEY_3))
                .attribute(Attribute.create(RANDOM_KEY_4))

                .attribute(Attribute.create(ACTION_RESULT))
        ;
    }

    public int modifyXp(int xpIn)
    {
        if (this._xp != 0)
        {
            xpIn = this._xp;
        }

        return (int) (xpIn * this._multiXp + this._addingXp);
    }
}
