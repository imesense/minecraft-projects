package org.imesense.dynamicspawncontrol.technical.eventprocessor.generic;

public final class GenericDropLoot extends ListActionsSingleEvent<SignalDataGetter>
{
    private boolean _removeAll = false;

    private static int _countCreatedMaps = 0;

    private final ListActionsBinary _ruleEvaluator;

    private static final Random _random = new Random();

    public boolean isRemoveAll() { return _removeAll; }

    private final List<Predicate<ItemStack>> _toRemoveItems = new ArrayList<>();

    public List<Predicate<ItemStack>> getToRemoveItems() { return _toRemoveItems; }

    private static final AttributeMapFactory<Object> FACTORY = new AttributeMapFactory<>();

    private final List<Pair<ItemStack, Function<Integer, Integer>>> _toAddItems = new ArrayList<>();

    public List<Pair<ItemStack, Function<Integer, Integer>>> getToAddItems() { return _toAddItems; }

    public boolean match(LivingDropsEvent event) { return _ruleEvaluator.match(event, EVENT_QUERY); }

    private GenericDropLoot(AttributeMap<?> map, String nameClass)
    {
        super(nameClass);

        Log.writeDataToLogFile(Log._typeLog[0], String.format("Iterator for [%s] number [%d]", nameClass, _countCreatedMaps++));

        this._ruleEvaluator = new ListActionsBinary<>(map, nameClass);

        this.addActions(map);

        if (map.has(ACTION_ITEM))
        {
            this.addItem(map);
        }

        if (map.has(KeyWordsGeneral.DroopLoot.ACTION_REMOVE))
        {
            this.removeItem(map);
        }

        if (map.has(KeyWordsGeneral.DroopLoot.ACTION_REMOVE_ALL))
        {
            this._removeAll = (Boolean) map.get(KeyWordsGeneral.DroopLoot.ACTION_REMOVE_ALL);
        }
    }

    public static GenericDropLoot parse(JsonElement element)
    {
        if (element == null)
        {
            return null;
        }
        else
        {
            AttributeMap<?> map = FACTORY.parse(element);

            return new GenericDropLoot(map, "GenericDropLoot");
        }
    }

    private static final SignalDataAccessor<LivingDropsEvent> EVENT_QUERY = new SignalDataAccessor<LivingDropsEvent>()
    {
        @Override
        public World getWorld(LivingDropsEvent LivingDropsEvent)
        {
            return LivingDropsEvent.getEntity().getEntityWorld();
        }

        @Override
        public BlockPos getPos(LivingDropsEvent LivingDropsEvent)
        {
            return LivingDropsEvent.getEntity().getPosition();
        }

        @Override
        public BlockPos getValidBlockPos(LivingDropsEvent LivingDropsEvent)
        {
            return LivingDropsEvent.getEntity().getPosition().down();
        }

        @Override
        public int getY(LivingDropsEvent LivingDropsEvent)
        {
            return LivingDropsEvent.getEntity().getPosition().getY();
        }

        @Override
        public Entity getEntity(LivingDropsEvent LivingDropsEvent)
        {
            return LivingDropsEvent.getEntity();
        }

        @Override
        public DamageSource getSource(LivingDropsEvent LivingDropsEvent)
        {
            return LivingDropsEvent.getSource();
        }

        @Override
        public Entity getAttacker(LivingDropsEvent LivingDropsEvent)
        {
            return LivingDropsEvent.getSource().getTrueSource();
        }

        @Override
        public EntityPlayer getPlayer(LivingDropsEvent LivingDropsEvent)
        {
            Entity entity = LivingDropsEvent.getSource().getTrueSource();
            return entity instanceof EntityPlayer ? (EntityPlayer) entity : null;
        }

        @Override
        public ItemStack getItem(LivingDropsEvent LivingDropsEvent)
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

                .attribute(Attribute.createMulti(ACTION_ITEM))
                .attribute(Attribute.createMulti(ACTION_REMOVE))
                .attribute(Attribute.create(ACTION_REMOVE_ALL))
                .attribute(Attribute.create(ACTION_ITEM_NBT))
                .attribute(Attribute.create(ACTION_ITEM_COUNT))
        ;
    }

    private static Function<Integer, Integer> getCountFunction(@Nullable String itemCount)
    {
        if (itemCount == null)
        {
            return looting -> 1;
        }

        String[] lootTable = StringUtils.split(itemCount, '/');

        int[] min = new int[lootTable.length];
        int[] max = new int[lootTable.length];

        for (int i = 0; i < lootTable.length; i++)
        {
            String[] splitMinMax = StringUtils.split(lootTable[i], '-');

            if (splitMinMax.length == 1)
            {
                try
                {
                    min[i] = max[i] = Integer.parseInt(splitMinMax[0]);
                }
                catch (NumberFormatException e)
                {
                    Log.writeDataToLogFile(Log._typeLog[2], "Bad amount specified in loot rule: " + splitMinMax[0]);
                    min[i] = max[i] = 1;
                }
            }
            else if (splitMinMax.length == 2)
            {
                try
                {
                    min[i] = Integer.parseInt(splitMinMax[0]);
                    max[i] = Integer.parseInt(splitMinMax[1]);
                }
                catch (NumberFormatException e)
                {
                    Log.writeDataToLogFile(Log._typeLog[2], "Bad amounts specified in loot rule: " + splitMinMax[0]);
                    min[i] = max[i] = 1;
                }
            }
            else
            {
                Log.writeDataToLogFile(Log._typeLog[2], "Bad amount range specified in loot rule: " + splitMinMax[0]);
                min[i] = max[i] = 1;
            }
        }

        if (lootTable.length == 1)
        {
            if (min[0] == max[0])
            {
                return looting -> min[0];
            }
            else
            {
                return looting -> _random.nextInt(max[0] - min[0] + 1) + min[0];
            }
        }
        else
        {
            return looting ->
            {
                if (looting >= min.length)
                {
                    return _random.nextInt(max[min.length - 1] - min[min.length - 1] + 1) + min[min.length - 1];
                }
                else if (looting >= 0)
                {
                    return _random.nextInt(max[looting] - min[looting] + 1) + min[looting];
                }
                else
                {
                    return _random.nextInt(max[0] - min[0] + 1) + min[0];
                }
            };
        }
    }

    private static List<Pair<ItemStack, Function<Integer, Integer>>> getItems(List<String> itemNames, @Nullable String nbtJson,
                                                                              @Nullable String itemCount)
    {
        Function<Integer, Integer> countFunction = getCountFunction(itemCount);

        List<Pair<ItemStack, Function<Integer, Integer>>> items = new ArrayList<>();

        for (String name : itemNames)
        {
            ItemStack stack = ItemStackBuilder.parseStack(name);

            if (stack.isEmpty())
            {
                Log.writeDataToLogFile(Log._typeLog[2], "Unknown item '" + name + "'!");
            }
            else
            {
                if (nbtJson != null)
                {
                    try
                    {
                        stack.setTagCompound(JsonToNBT.getTagFromJson(nbtJson));
                    }
                    catch (NBTException e)
                    {
                        Log.writeDataToLogFile(Log._typeLog[2], "Bad nbt for '" + name + "'!");
                    }
                }

                items.add(Pair.of(stack, countFunction));
            }
        }

        return items;
    }

    private void addItem(AttributeMap<?> map)
    {
        Object nbt = map.get(KeyWordsGeneral.DroopLoot.ACTION_ITEM_NBT);
        Object itemCount = map.get(KeyWordsGeneral.DroopLoot.ACTION_ITEM_COUNT);

        // Добавление предметов в список _toAddItems
        this._toAddItems.addAll(getItems(map.getList(ACTION_ITEM), (String)nbt, (String)itemCount));
    }

    private void removeItem(AttributeMap<?> map)
    {
        this._toRemoveItems.addAll(AuxFunctions.getItems((JsonElement)map.getList(KeyWordsGeneral.DroopLoot.ACTION_REMOVE)));
    }
}