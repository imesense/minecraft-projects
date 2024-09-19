package org.imesense.dynamicspawncontrol.technical.customlibrary;

/**
 *
 * @param <T>
 */
public class ListActionsBinary<T extends SignalDataGetter>
{
    /**
     *
     */
    public final List<BiFunction<Event, SignalDataAccessor, Boolean>> _arrayList = new ArrayList<>();

    /**
     *
     * @param map
     * @param nameClass
     */
    public ListActionsBinary(AttributeMap<?> map, String nameClass)
    {
        Log.writeDataToLogFile(Log._typeLog[0], nameClass);
        this.CreateListActions(map);
    }

    /**
     *
     * @param event
     * @param query
     * @return
     */
    public boolean match(Event event, SignalDataAccessor<T> query)
    {
        for (BiFunction<Event, SignalDataAccessor, Boolean> rule : _arrayList)
        {
            if (!rule.apply(event, query))
            {
                return false;
            }
        }

        return true;
    }

    /**
     *
     * @param map
     */
    public void CreateListActions(AttributeMap<?> map)
    {
        if (map.has(KeyWordsGeneral.CommonKeyWorlds.SEE_SKY))
        {
            this.addSeeSkyCheck(map);
        }

        if (map.has(KeyWordsGeneral.SpawnCondition.CAN_SPAWN_HERE))
        {
            this.addCanSpawnHereCheck(map);
        }

        if (map.has(KeyWordsGeneral.SpawnCondition.NOT_COLLIDING))
        {
            this.addNotCollidingCheck(map);
        }

        if (map.has(KeyWordsGeneral.SpawnCondition.SPAWNER))
        {
            this.addSpawnerCheck(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.WEATHER))
        {
            this.addWeatherCheck(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.BIOMES))
        {
            addBiomesCheck(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.BIOMES_TYPE))
        {
            this.addBiomesTypesCheck(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.STRUCTURE))
        {
            this.addStructureCheck(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.DIMENSION))
        {
            this.addDimensionCheck(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.HELMET))
        {
            this.addHelmetCheck(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.CHEST_PLATE))
        {
            this.addChestPlateCheck(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.LEGGINGS))
        {
            this.addLeggingsCheck(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.BOOTS))
        {
            this.addBootsCheck(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.MIN_TIME))
        {
            this.addMinTimeCheck(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.MAX_TIME))
        {
            this.addMaxTimeCheck(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.MIN_LIGHT))
        {
            this.addMinLightCheck(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.MAX_LIGHT))
        {
            this.addMaxLightCheck(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.MIN_HEIGHT))
        {
            this.addMinHeightCheck(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.MAX_HEIGHT))
        {
            this.addMaxHeightCheck(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.DIFFICULTY))
        {
            this.addDifficultyCheck(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.MIN_DIFFICULTY))
        {
            this.addMinAdditionalDifficultyCheck(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.MAX_DIFFICULTY))
        {
            this.addMaxAdditionalDifficultyCheck(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.MIN_SPAWN_DIST))
        {
            this.addMinSpawnDistCheck(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.MAX_SPAWN_DIST))
        {
            this.addMaxSpawnDistCheck(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.BLOCK))
        {
            this.addBlocksCheck(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.GET_MOON_PHASE))
        {
            this.addCheckMoonPhase(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.MOB))
        {
            this.addMobsCheck(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.ANIMALS))
        {
            this.addInterfaceAnimalsCheck(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.MONSTERS))
        {
            this.addInterfaceMonstersCheck(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.PLAYER))
        {
            this.addPlayerCheck(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.FAKE_PLAYER))
        {
            this.addFakePlayerCheck(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.REAL_PLAYER))
        {
            this.addRealPlayerCheck(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.HELD_ITEM))
        {
            this.addHeldItemCheck(map, KeyWordsGeneral.CommonKeyWorlds.HELD_ITEM);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.PLAYER_HELD_ITEM))
        {
            this.addHeldItemCheck(map, KeyWordsGeneral.CommonKeyWorlds.PLAYER_HELD_ITEM);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.OFF_HAND_ITEM))
        {
            this.addOffHandItemCheck(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.BOTH_HANDS_ITEM))
        {
            this.addBothHandsItemCheck(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.EXPLOSION))
        {
            this.addExplosionCheck(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.PROJECTILE))
        {
            this.addProjectileCheck(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.FIRE))
        {
            this.addFireCheck(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.MAGIC))
        {
            this.addMagicCheck(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.SOURCE))
        {
            this.addSourceCheck(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.RANDOM_KEY_0))
        {
            this.addRandomCheck_0(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.RANDOM_KEY_1))
        {
            this.addRandomCheck_1(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.RANDOM_KEY_2))
        {
            this.addRandomCheck_2(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.RANDOM_KEY_3))
        {
            this.addRandomCheck_3(map);
        }

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.RANDOM_KEY_4))
        {
            this.addRandomCheck_4(map);
        }
    }

    /**
     *
     * @param map
     */
    private void addSeeSkyCheck(AttributeMap<?> map)
    {
        Object seeSky = map.get(KeyWordsGeneral.CommonKeyWorlds.SEE_SKY);

        if ((Boolean)seeSky)
        {
            _arrayList.add((event,query) ->
                    query.getWorld(event).canBlockSeeSky(query.getPos(event)));
        }
        else
        {
            _arrayList.add((event,query) ->
                    !query.getWorld(event).canBlockSeeSky(query.getPos(event)));
        }
    }

    /**
     *
     * @param map
     */
    private void addCanSpawnHereCheck(AttributeMap<?> map)
    {
        Object canSpawn = map.get(KeyWordsGeneral.SpawnCondition.CAN_SPAWN_HERE);

        if ((Boolean)canSpawn)
        {
            _arrayList.add((event, query) ->
            {
                Entity entity = query.getEntity(event);

                if (entity instanceof EntityLiving)
                {
                    return ((EntityLiving) entity).getCanSpawnHere();
                }
                else
                {
                    return false;
                }
            });
        }
        else
        {
            _arrayList.add((event, query) ->
            {
                Entity entity = query.getEntity(event);

                if (entity instanceof EntityLiving)
                {
                    return !((EntityLiving) entity).getCanSpawnHere();
                }
                else
                {
                    return true;
                }
            });
        }
    }

    /**
     *
     * @param map
     */
    private void addNotCollidingCheck(AttributeMap<?> map)
    {
        Object notCollidingCheck = map.get(KeyWordsGeneral.SpawnCondition.NOT_COLLIDING);

        if ((Boolean)notCollidingCheck)
        {
            _arrayList.add((event, query) ->
            {
                Entity entity = query.getEntity(event);

                if (entity instanceof EntityLiving)
                {
                    return ((EntityLiving) entity).isNotColliding();
                }
                else
                {
                    return false;
                }
            });
        }
        else
        {
            _arrayList.add((event, query) ->
            {
                Entity entity = query.getEntity(event);

                if (entity instanceof EntityLiving)
                {
                    return !((EntityLiving) entity).isNotColliding();
                }
                else
                {
                    return true;
                }
            });
        }
    }

    /**
     *
     * @param map
     */
    private void addSpawnerCheck(AttributeMap<?> map)
    {
        Object spawner = map.get(KeyWordsGeneral.SpawnCondition.SPAWNER);

        if ((Boolean)spawner)
        {
            _arrayList.add((event, query) ->
            {
                if (event instanceof LivingSpawnEvent.CheckSpawn)
                {
                    LivingSpawnEvent.CheckSpawn checkSpawn = (LivingSpawnEvent.CheckSpawn) event;
                    return checkSpawn.isSpawner();
                }
                else
                {
                    return false;
                }
            });
        }
        else
        {
            _arrayList.add((event, query) ->
            {
                if (event instanceof LivingSpawnEvent.CheckSpawn)
                {
                    LivingSpawnEvent.CheckSpawn checkSpawn = (LivingSpawnEvent.CheckSpawn) event;
                    return !checkSpawn.isSpawner();
                }
                else
                {
                    return false;
                }
            });
        }
    }

    /**
     *
     * @param map
     */
    private void addBiomesCheck(AttributeMap<?> map)
    {
        List<String> biomes = map.getList(KeyWordsGeneral.CommonKeyWorlds.BIOMES);

        if (biomes.size() == 1)
        {
            String biomesName = biomes.get(0);

            _arrayList.add((event,query) ->
            {
                Biome biome = query.getWorld(event).getBiome(query.getPos(event));
                return biomesName.equals(biome.getBiomeName());
            });
        }
        else
        {
            Set<String> biomesName = new HashSet<>(biomes);

            _arrayList.add((event,query) ->
            {
                Biome biome = query.getWorld(event).getBiome(query.getPos(event));
                return biomesName.contains(biome.getBiomeName());
            });
        }
    }

    /**
     *
     * @param map
     */
    private void addBiomesTypesCheck(AttributeMap<?> map)
    {
        List<String> biomesTypes = map.getList(KeyWordsGeneral.CommonKeyWorlds.BIOMES_TYPE);

        if (biomesTypes.size() == 1)
        {
            String biomesType = biomesTypes.get(0);

            BiomeDictionary.Type type = BiomeDictionary.Type.getType(biomesType);

            _arrayList.add((event,query) ->
            {
                Biome biome = query.getWorld(event).getBiome(query.getPos(event));
                return BiomeDictionary.getTypes(biome).contains(type);
            });
        }
        else
        {
            Set<BiomeDictionary.Type> types = new HashSet<>();

            for (String s : biomesTypes)
            {
                types.add(BiomeDictionary.Type.getType(s));
            }

            _arrayList.add((event,query) ->
            {
                Biome biome = query.getWorld(event).getBiome(query.getPos(event));
                return BiomeDictionary.getTypes(biome).stream().anyMatch(s -> types.contains(s));
            });
        }
    }

    /**
     *
     * @param map
     */
    private void addWeatherCheck(AttributeMap<?> map)
    {
        Object weatherObject = map.get(KeyWordsGeneral.CommonKeyWorlds.WEATHER);

        if (weatherObject instanceof String)
        {
            String weather = (String) weatherObject;

            boolean raining = weather.toLowerCase().startsWith("rain");
            boolean thunder = weather.toLowerCase().startsWith("thunder");

            if (raining)
            {
                _arrayList.add((event, query) ->
                        query.getWorld(event).isRaining());
            }
            else if (thunder)
            {
                _arrayList.add((event, query) ->
                        query.getWorld(event).isThundering());
            }
            else
            {
                Log.writeDataToLogFile(Log._typeLog[2], "Unknown weather '" + weather + "'! Use 'rain' or 'thunder'");
            }
        }
        else
        {
            Log.writeDataToLogFile(Log._typeLog[2], "Weather is not a string object!");
        }
    }

    /**
     *
     * @param map
     */
    private void addStructureCheck(AttributeMap<?> map)
    {
        Object structure = map.get(KeyWordsGeneral.CommonKeyWorlds.STRUCTURE);

        _arrayList.add((event,query) ->
                Structures._structuresCache.isInStructure(query.getWorld(event), (String) structure, query.getPos(event)));
    }

    /**
     *
     * @param map
     */
    private void addDimensionCheck(AttributeMap<?> map)
    {
        List<Integer> dimensions = map.getListI(KeyWordsGeneral.CommonKeyWorlds.DIMENSION);

        if (dimensions.size() == 1)
        {
            Integer dim = dimensions.get(0);

            _arrayList.add((event, query) ->
                    query.getWorld(event).provider.getDimension() == dim);
        }
        else
        {
            Set<Integer> dims = new HashSet<>(dimensions);

            _arrayList.add((event, query) ->
                    dims.contains(query.getWorld(event).provider.getDimension()));
        }
    }

    /**
     *
     * @param map
     */
    public void addHelmetCheck(AttributeMap<?> map)
    {
        List<Predicate<ItemStack>> items = AuxFunctions.getItems(map.getList(KeyWordsGeneral.CommonKeyWorlds.HELMET));

        _arrayList.add((event, query) ->
        {
            EntityPlayer player = query.getPlayer(event);

            if (player != null)
            {
                ItemStack armorItem = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                if (!armorItem.isEmpty())
                {
                    return items.stream().anyMatch(item -> item.test(armorItem));
                }
            }

            return false;
        });
    }

    /**
     *
     * @param map
     */
    public void addChestPlateCheck(AttributeMap<?> map)
    {
        List<Predicate<ItemStack>> items = AuxFunctions.getItems(map.getList(KeyWordsGeneral.CommonKeyWorlds.CHEST_PLATE));

        _arrayList.add((event, query) ->
        {
            EntityPlayer player = query.getPlayer(event);

            if (player != null)
            {
                ItemStack armorItem = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                if (!armorItem.isEmpty())
                {
                    return items.stream().anyMatch(item -> item.test(armorItem));
                }
            }

            return false;
        });
    }

    /**
     *
     * @param map
     */
    public void addLeggingsCheck(AttributeMap<?> map)
    {
        List<Predicate<ItemStack>> items = AuxFunctions.getItems(map.getList(KeyWordsGeneral.CommonKeyWorlds.LEGGINGS));

        _arrayList.add((event, query) ->
        {
            EntityPlayer player = query.getPlayer(event);

            if (player != null)
            {
                ItemStack armorItem = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
                if (!armorItem.isEmpty())
                {
                    return items.stream().anyMatch(item -> item.test(armorItem));
                }
            }

            return false;
        });
    }

    /**
     *
     * @param map
     */
    public void addBootsCheck(AttributeMap<?> map)
    {
        List<Predicate<ItemStack>> items = AuxFunctions.getItems(map.getList(KeyWordsGeneral.CommonKeyWorlds.BOOTS));

        _arrayList.add((event, query) ->
        {
            EntityPlayer player = query.getPlayer(event);

            if (player != null)
            {
                ItemStack armorItem = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
                if (!armorItem.isEmpty())
                {
                    return items.stream().anyMatch(item -> item.test(armorItem));
                }
            }

            return false;
        });
    }

    /**
     *
     * @param map
     */
    private void addMinTimeCheck(AttributeMap<?> map)
    {
        Object minTime = map.get(KeyWordsGeneral.CommonKeyWorlds.MIN_TIME);

        _arrayList.add((event, query) ->
        {
            long time = query.getWorld(event).getWorldTime();
            return (time % 24000) >= (Integer) minTime;
        });
    }

    /**
     *
     * @param map
     */
    private void addMaxTimeCheck(AttributeMap<?> map)
    {
        Object maxTime = map.get(KeyWordsGeneral.CommonKeyWorlds.MAX_TIME);

        _arrayList.add((event, query) ->
        {
            long time = query.getWorld(event).getWorldTime();
            return (time % 24000) <= (Integer) maxTime;
        });
    }

    /**
     *
     * @param map
     */
    private void addMinLightCheck(AttributeMap<?> map)
    {
        Object minLight = map.get(KeyWordsGeneral.CommonKeyWorlds.MIN_LIGHT);

        _arrayList.add((event,query) ->
        {
            BlockPos pos = query.getPos(event);
            return query.getWorld(event).getLight(pos, true) >= (Integer) minLight;
        });
    }

    /**
     *
     * @param map
     */
    private void addMaxLightCheck(AttributeMap<?> map)
    {
        Object maxLight = map.get(KeyWordsGeneral.CommonKeyWorlds.MAX_LIGHT);

        _arrayList.add((event,query) ->
        {
            BlockPos pos = query.getPos(event);
            return query.getWorld(event).getLight(pos, true) <= (Integer) maxLight;
        });
    }

    /**
     *
     * @param map
     */
    private void addMinHeightCheck(AttributeMap<?> map)
    {
        Object minHeight = map.get(KeyWordsGeneral.CommonKeyWorlds.MIN_HEIGHT);

        _arrayList.add((event,query) ->
                query.getY(event) >= (Integer) minHeight);
    }

    /**
     *
     * @param map
     */
    private void addMaxHeightCheck(AttributeMap<?> map)
    {
        Object maxHeight = map.get(KeyWordsGeneral.CommonKeyWorlds.MAX_HEIGHT);

        _arrayList.add((event,query) ->
                query.getY(event) <= (Integer) maxHeight);
    }

    /**
     *
     * @param map
     */
    private void addMinAdditionalDifficultyCheck(AttributeMap<?> map)
    {
        Object minDifficulty = map.get(KeyWordsGeneral.CommonKeyWorlds.MIN_DIFFICULTY);

        _arrayList.add((event,query) ->
                query.getWorld(event).getDifficultyForLocation(query.getPos(event)).getAdditionalDifficulty() >= (Float) minDifficulty);
    }

    /**
     *
     * @param map
     */
    private void addDifficultyCheck(AttributeMap<?> map)
    {
        EnumDifficulty enumDifficulty = null;

        Object difficulty = map.get(KeyWordsGeneral.CommonKeyWorlds.DIFFICULTY);

        for (EnumDifficulty _difficulty : EnumDifficulty.values())
        {
            if (_difficulty.getDifficultyResourceKey().endsWith("." + difficulty))
            {
                enumDifficulty = _difficulty;
                break;
            }
        }

        if (enumDifficulty != null)
        {
            EnumDifficulty finalDiff = enumDifficulty;

            _arrayList.add((event,query) ->
                    query.getWorld(event).getDifficulty() == finalDiff);
        }
        else
        {
            Log.writeDataToLogFile(Log._typeLog[2], "Unknown difficulty '" + difficulty + "'! Use one of 'easy', 'normal', 'hard',  or 'peaceful'");
            throw new RuntimeException();
        }
    }

    /**
     *
     * @param map
     */
    private void addMaxAdditionalDifficultyCheck(AttributeMap<?> map)
    {
        Object maxDifficulty = map.get(KeyWordsGeneral.CommonKeyWorlds.MAX_DIFFICULTY);

        _arrayList.add((event,query) ->
                query.getWorld(event).getDifficultyForLocation(query.getPos(event)).getAdditionalDifficulty() <= (Float) maxDifficulty);
    }

    /**
     *
     * @param map
     */
    private void addMinSpawnDistCheck(AttributeMap<?> map)
    {
        Object degree = map.get(KeyWordsGeneral.CommonKeyWorlds.MIN_SPAWN_DIST);

        _arrayList.add((event,query) ->
        {
            BlockPos pos = query.getPos(event);
            double sqDist = pos.distanceSq(query.getWorld(event).getSpawnPoint());

            return sqDist >= (Float) degree * (Float) degree;
        });
    }

    /**
     *
     * @param map
     */
    private void addMaxSpawnDistCheck(AttributeMap<?> map)
    {
        Object degree = map.get(KeyWordsGeneral.CommonKeyWorlds.MAX_SPAWN_DIST);

        _arrayList.add((event, query) ->
        {
            BlockPos pos = query.getPos(event);
            double sqDist = pos.distanceSq(query.getWorld(event).getSpawnPoint());

            return sqDist <= (Float) degree * (Float) degree;
        });
    }

    /**
     *
     * @param map
     */
    private void addBlocksCheck(AttributeMap<?> map)
    {
        BiFunction<Event, SignalDataAccessor, BlockPos> posFunction;

        if (map.has(KeyWordsGeneral.CommonKeyWorlds.BLOCK_OFFSET))
        {
            posFunction = AuxFunctions.parseOffset((String)map.get(KeyWordsGeneral.CommonKeyWorlds.BLOCK_OFFSET));
        }
        else
        {
            posFunction = (event, query) ->
                    query.getPos(event);
        }

        List<String> blocks = map.getList(KeyWordsGeneral.CommonKeyWorlds.BLOCK);

        if (blocks.size() == 1)
        {
            String json = blocks.get(0);
            BiPredicate<World, BlockPos> blockMatcher = AuxFunctions.parseBlock(json);

            if (blockMatcher != null)
            {
                _arrayList.add((event, query) ->
                {
                    BlockPos pos = posFunction.apply(event, query);

                    return pos != null && blockMatcher.test(query.getWorld(event), pos);
                });
            }
        }
        else
        {
            List<BiPredicate<World, BlockPos>> blockMatchers = new ArrayList<>();

            for (String block : blocks)
            {
                BiPredicate<World, BlockPos> blockMatcher = AuxFunctions.parseBlock(block);

                if (blockMatcher == null)
                {
                    return;
                }

                blockMatchers.add(blockMatcher);
            }

            _arrayList.add((event,query) ->
            {
                BlockPos pos = posFunction.apply(event, query);

                if (pos != null)
                {
                    World world = query.getWorld(event);

                    for (BiPredicate<World, BlockPos> matcher : blockMatchers)
                    {
                        if (matcher.test(world, pos))
                        {
                            return true;
                        }
                    }
                }

                return false;
            });
        }
    }

    /**
     *
     * @param _map
     */
    private void addCheckMoonPhase(AttributeMap<?> _map)
    {
        Object moon = _map.get(KeyWordsGeneral.CommonKeyWorlds.GET_MOON_PHASE);

        _arrayList.add((event,query) ->
                query.getWorld(event).getMoonPhase() == (Integer)moon);
    }

    /**
     *
     * @param map
     */
    private void addMobsCheck(AttributeMap<?> map)
    {
        List<String> listMobs = map.getList(KeyWordsGeneral.CommonKeyWorlds.MOB);

        if (listMobs.size() == 1)
        {
            String name = listMobs.get(0);
            String id = GenericOverrideSpawn.fixEntityId(name);
            EntityEntry _forgeRegEntity0 = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(id));
            Class<? extends Entity> typeClass = _forgeRegEntity0 == null ? null : _forgeRegEntity0.getEntityClass();

            if (typeClass != null)
            {
                _arrayList.add((event, query) ->
                        typeClass.equals(query.getEntity(event).getClass()));
            }
            else
            {
                Log.writeDataToLogFile(Log._typeLog[2], "Unknown mob '" + name + "'!");
                throw new RuntimeException();
            }
        }
        else
        {
            Set<Class<?>> classes = new HashSet<>();

            for (String name : listMobs)
            {
                String id = GenericOverrideSpawn.fixEntityId(name);
                EntityEntry _forgeRegEntity1 = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(id));
                Class<? extends Entity> typeClass = _forgeRegEntity1 == null ? null : _forgeRegEntity1.getEntityClass();

                if (typeClass != null)
                {
                    classes.add(typeClass);
                }
                else
                {
                    Log.writeDataToLogFile(Log._typeLog[2], "Unknown mob '" + name + "'!");
                    throw new RuntimeException();
                }
            }

            if (!classes.isEmpty())
            {
                _arrayList.add((event, query) ->
                        classes.contains(query.getEntity(event).getClass()));
            }
        }
    }

    /**
     *
     * @param map
     */
    private void addInterfaceAnimalsCheck(AttributeMap<?> map)
    {
        Object animalsObj = map.get(KeyWordsGeneral.CommonKeyWorlds.ANIMALS);

        if ((Boolean)animalsObj)
        {
            _arrayList.add((event, query) ->
                    (query.getEntity(event) instanceof IAnimals
                            && !(query.getEntity(event) instanceof IMob)));
        }
        else
        {
            _arrayList.add((event, query) ->
                    !(query.getEntity(event) instanceof IAnimals
                            && !(query.getEntity(event) instanceof IMob)));
        }
    }

    /**
     *
     * @param map
     */
    private void addInterfaceMonstersCheck(AttributeMap<?> map)
    {
        Object monstersObj = map.get(KeyWordsGeneral.CommonKeyWorlds.MONSTERS);

        if ((Boolean)monstersObj)
        {
            _arrayList.add((event, query) ->
                    query.getEntity(event) instanceof IMob);
        }
        else
        {
            _arrayList.add((event, query) ->
                    !(query.getEntity(event) instanceof IMob));
        }
    }

    /**
     *
     * @param map
     */
    private void addPlayerCheck(AttributeMap<?> map)
    {
        Object asPlayer = map.get(KeyWordsGeneral.CommonKeyWorlds.PLAYER);

        if ((Boolean)asPlayer)
        {
            _arrayList.add((event, query) ->
                    query.getAttacker(event) instanceof EntityPlayer);
        }
        //else
        //{
        //    _arrayList.add((event, query) -> query.getAttacker(event) instanceof EntityPlayer);
        //}
    }

    /**
     *
     * @param _map
     */
    private void addFakePlayerCheck(AttributeMap<?> _map)
    {
        Object asPlayer = _map.get(KeyWordsGeneral.CommonKeyWorlds.FAKE_PLAYER);

        if ((Boolean)asPlayer)
        {
            _arrayList.add((event, query) -> query.getAttacker(event) == null ? false : AuxFunctions.isFakePlayer(query.getAttacker(event)));
        }
        else
        {
            _arrayList.add((event, query) -> query.getAttacker(event) == null ? true : !AuxFunctions.isFakePlayer(query.getAttacker(event)));
        }
    }

    /**
     *
     * @param map
     */
    private void addRealPlayerCheck(AttributeMap<?> map)
    {
        Object asPlayer = map.get(KeyWordsGeneral.CommonKeyWorlds.REAL_PLAYER);

        if ((Boolean)asPlayer)
        {
            _arrayList.add((event, query) -> query.getAttacker(event) == null ? false : AuxFunctions.isRealPlayer(query.getAttacker(event)));
        }
        else
        {
            _arrayList.add((event, query) -> query.getAttacker(event) == null ? true : !AuxFunctions.isRealPlayer(query.getAttacker(event)));
        }
    }

    /**
     *
     * @param map
     * @param key
     */
    public void addHeldItemCheck(AttributeMap<?> map, AttributeKey<String> key)
    {
        List<Predicate<ItemStack>> items = AuxFunctions.getItems(map.getList(key));

        _arrayList.add((event,query) ->
        {
            EntityPlayer player = query.getPlayer(event);

            if (player != null)
            {
                ItemStack main_hand = player.getHeldItemMainhand();

                if (!main_hand.isEmpty())
                {
                    for (Predicate<ItemStack> item : items)
                    {
                        if (item.test(main_hand))
                        {
                            return true;
                        }
                    }
                }
            }

            return false;
        });
    }

    /**
     *
     * @param map
     */
    public void addOffHandItemCheck(AttributeMap<?> map)
    {
        List<Predicate<ItemStack>> items = AuxFunctions.getItems(map.getList(KeyWordsGeneral.CommonKeyWorlds.OFF_HAND_ITEM));

        _arrayList.add((event,query) ->
        {
            EntityPlayer player = query.getPlayer(event);

            if (player != null)
            {
                ItemStack offhand = player.getHeldItemOffhand();

                if (!offhand.isEmpty())
                {
                    for (Predicate<ItemStack> item : items)
                    {
                        if (item.test(offhand))
                        {
                            return true;
                        }
                    }
                }
            }

            return false;
        });
    }

    /**
     *
     * @param map
     */
    private void addExplosionCheck(AttributeMap<?> map)
    {
        Object explosion = map.get(KeyWordsGeneral.CommonKeyWorlds.EXPLOSION);

        if ((Boolean)explosion)
        {
            _arrayList.add((event, query) -> query.getSource(event) == null ? false : query.getSource(event).isExplosion());
        }
        else
        {
            _arrayList.add((event, query) -> query.getSource(event) == null ? true : !query.getSource(event).isExplosion());
        }
    }

    /**
     *
     * @param map
     */
    private void addProjectileCheck(AttributeMap<?> map)
    {
        Object projectile = map.get(KeyWordsGeneral.CommonKeyWorlds.PROJECTILE);

        if ((Boolean)projectile)
        {
            _arrayList.add((event, query) -> query.getSource(event) == null ? false : query.getSource(event).isProjectile());
        }
        else
        {
            _arrayList.add((event, query) -> query.getSource(event) == null ? true : !query.getSource(event).isProjectile());
        }
    }

    /**
     *
     * @param map
     */
    private void addFireCheck(AttributeMap<?> map)
    {
        Object fire = map.get(KeyWordsGeneral.CommonKeyWorlds.FIRE);

        if ((Boolean)fire)
        {
            _arrayList.add((event, query) -> query.getSource(event) == null ? false : query.getSource(event).isFireDamage());
        }
        else
        {
            _arrayList.add((event, query) -> query.getSource(event) == null ? true : !query.getSource(event).isFireDamage());
        }
    }

    /**
     *
     * @param map
     */
    private void addMagicCheck(AttributeMap<?> map)
    {
        Object magic = map.get(KeyWordsGeneral.CommonKeyWorlds.MAGIC);

        if ((Boolean)magic)
        {
            _arrayList.add((event, query) -> query.getSource(event) == null ? false : query.getSource(event).isMagicDamage());
        }
        else
        {
            _arrayList.add((event, query) -> query.getSource(event) == null ? true : !query.getSource(event).isMagicDamage());
        }
    }

    /**
     *
     * @param map
     */
    private void addSourceCheck(AttributeMap<?> map)
    {
        List<String> sources = map.getList(KeyWordsGeneral.CommonKeyWorlds.SOURCE);
        Set<String> sourceSet = new HashSet<>(sources);

        _arrayList.add((event, query) ->
        {
            if (query.getSource(event) == null)
            {
                return false;
            }

            return sourceSet.contains(query.getSource(event).getDamageType());
        });
    }

    /**
     *
     * @param map
     */
    public void addBothHandsItemCheck(AttributeMap<?> map)
    {
        List<Predicate<ItemStack>> items = AuxFunctions.getItems(map.getList(KeyWordsGeneral.CommonKeyWorlds.BOTH_HANDS_ITEM));

        _arrayList.add((event,query) ->
        {
            EntityPlayer player = query.getPlayer(event);

            if (player != null)
            {
                ItemStack offhand = player.getHeldItemOffhand();

                if (!offhand.isEmpty())
                {
                    for (Predicate<ItemStack> item : items)
                    {
                        if (item.test(offhand))
                        {
                            return true;
                        }
                    }
                }

                ItemStack mainHand = player.getHeldItemMainhand();

                if (!mainHand.isEmpty())
                {
                    for (Predicate<ItemStack> item : items)
                    {
                        if (item.test(mainHand))
                        {
                            return true;
                        }
                    }
                }
            }

            return false;
        });
    }

    /**
     *
     * @param map
     */
    private void addRandomCheck_0(AttributeMap<?> map)
    {
        Object _random = map.get(KeyWordsGeneral.CommonKeyWorlds.RANDOM_KEY_0);

        _arrayList.add((event, query) ->
                new Random().nextFloat() < (Float)_random);
    }

    /**
     *
     * @param map
     */
    private void addRandomCheck_1(AttributeMap<?> map)
    {
        Object _random = map.get(KeyWordsGeneral.CommonKeyWorlds.RANDOM_KEY_1);

        _arrayList.add((event, query) ->
                new Random().nextFloat() < (Float)_random);
    }

    /**
     *
     * @param map
     */
    private void addRandomCheck_2(AttributeMap<?> map)
    {
        Object _random = map.get(KeyWordsGeneral.CommonKeyWorlds.RANDOM_KEY_2);

        _arrayList.add((event, query) ->
                new Random().nextFloat() < (Float)_random);
    }

    /**
     *
     * @param map
     */
    private void addRandomCheck_3(AttributeMap<?> map)
    {
        Object _random = map.get(KeyWordsGeneral.CommonKeyWorlds.RANDOM_KEY_3);

        _arrayList.add((event, query) ->
                new Random().nextFloat() < (Float)_random);
    }

    /**
     *
     * @param map
     */
    private void addRandomCheck_4(AttributeMap<?> map)
    {
        Object _random = map.get(KeyWordsGeneral.CommonKeyWorlds.RANDOM_KEY_4);

        _arrayList.add((event, query) ->
                new Random().nextFloat() < (Float)_random);
    }
}
