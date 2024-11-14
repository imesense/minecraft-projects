package org.imesense.dynamicspawncontrol.eventprocessor.listaction;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.attributefactory.AttributeKey;
import org.imesense.dynamicspawncontrol.core.attributefactory.AttributeMap;
import org.imesense.dynamicspawncontrol.core.api.SignalDataAccessor;
import org.imesense.dynamicspawncontrol.core.api.SignalDataGetter;
import org.imesense.dynamicspawncontrol.eventprocessor.generic.GenericPotentialSpawn;
import org.imesense.dynamicspawncontrol.technical.customlibrary.AuxFunction;
import org.imesense.dynamicspawncontrol.core.worldstructure.Structure;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

import static org.imesense.dynamicspawncontrol.core.auxsolid.Player.isFakePlayer;
import static org.imesense.dynamicspawncontrol.core.auxsolid.Player.isRealPlayer;
import static org.imesense.dynamicspawncontrol.eventprocessor.generic.keyword.CommonKeyWord.*;
import static org.imesense.dynamicspawncontrol.eventprocessor.generic.keyword.SpawnCondition.*;

/**
 *
 * @param <T>
 */
public final class ListActionBinary<T extends SignalDataGetter>
{
    /**
     *
     */
    public final List<BiFunction<Event, SignalDataAccessor, Boolean>> ARRAY_LIST = new ArrayList<>();

    /**
     *
     * @param attributeMap
     */
    public ListActionBinary(AttributeMap<?> attributeMap)
    {
        this.CreateListActions(attributeMap);
    }

    /**
     *
     * @param event
     * @param signalDataAccessor
     * @return
     */
    public boolean match(Event event, SignalDataAccessor<T> signalDataAccessor)
    {
        for (BiFunction<Event, SignalDataAccessor, Boolean> rule : this.ARRAY_LIST)
        {
            if (!rule.apply(event, signalDataAccessor))
            {
                return false;
            }
        }

        return true;
    }

    /**
     *
     * @param attributeMap
     */
    public void CreateListActions(AttributeMap<?> attributeMap)
    {
        if (attributeMap.has(SEE_SKY))
        {
            this.addSeeSkyCheck(attributeMap);
        }

        if (attributeMap.has(CAN_SPAWN_HERE))
        {
            this.addCanSpawnHereCheck(attributeMap);
        }

        if (attributeMap.has(NOT_COLLIDING))
        {
            this.addNotCollidingCheck(attributeMap);
        }

        if (attributeMap.has(SPAWNER))
        {
            this.addSpawnerCheck(attributeMap);
        }

        if (attributeMap.has(WEATHER))
        {
            this.addWeatherCheck(attributeMap);
        }

        if (attributeMap.has(BIOMES))
        {
            addBiomesCheck(attributeMap);
        }

        if (attributeMap.has(BIOMES_TYPE))
        {
            this.addBiomesTypesCheck(attributeMap);
        }

        if (attributeMap.has(STRUCTURE))
        {
            this.addStructureCheck(attributeMap);
        }

        if (attributeMap.has(DIMENSION))
        {
            this.addDimensionCheck(attributeMap);
        }

        if (attributeMap.has(HELMET))
        {
            this.addHelmetCheck(attributeMap);
        }

        if (attributeMap.has(CHEST_PLATE))
        {
            this.addChestPlateCheck(attributeMap);
        }

        if (attributeMap.has(LEGGINGS))
        {
            this.addLeggingsCheck(attributeMap);
        }

        if (attributeMap.has(BOOTS))
        {
            this.addBootsCheck(attributeMap);
        }

        if (attributeMap.has(MIN_TIME))
        {
            this.addMinTimeCheck(attributeMap);
        }

        if (attributeMap.has(MAX_TIME))
        {
            this.addMaxTimeCheck(attributeMap);
        }

        if (attributeMap.has(MIN_LIGHT))
        {
            this.addMinLightCheck(attributeMap);
        }

        if (attributeMap.has(MAX_LIGHT))
        {
            this.addMaxLightCheck(attributeMap);
        }

        if (attributeMap.has(MIN_HEIGHT))
        {
            this.addMinHeightCheck(attributeMap);
        }

        if (attributeMap.has(MAX_HEIGHT))
        {
            this.addMaxHeightCheck(attributeMap);
        }

        if (attributeMap.has(DIFFICULTY))
        {
            this.addDifficultyCheck(attributeMap);
        }

        if (attributeMap.has(MIN_DIFFICULTY))
        {
            this.addMinAdditionalDifficultyCheck(attributeMap);
        }

        if (attributeMap.has(MAX_DIFFICULTY))
        {
            this.addMaxAdditionalDifficultyCheck(attributeMap);
        }

        if (attributeMap.has(MIN_SPAWN_DIST))
        {
            this.addMinSpawnDistCheck(attributeMap);
        }

        if (attributeMap.has(MAX_SPAWN_DIST))
        {
            this.addMaxSpawnDistCheck(attributeMap);
        }

        if (attributeMap.has(BLOCK))
        {
            this.addBlocksCheck(attributeMap);
        }

        if (attributeMap.has(GET_MOON_PHASE))
        {
            this.addCheckMoonPhase(attributeMap);
        }

        if (attributeMap.has(GET_CURRENT_GAME_DAY_EQUAL))
        {
            this.addCheckCurrentGameDayEqual(attributeMap);
        }

        if (attributeMap.has(GET_CURRENT_GAME_DAY_GREATER))
        {
            this.addCheckCurrentGameDayGreater(attributeMap);
        }

        if (attributeMap.has(GET_CURRENT_GAME_DAY_LESS))
        {
            this.addCheckCurrentGameDayLess(attributeMap);
        }

        if (attributeMap.has(GET_CURRENT_GAME_DAY_GREATER_OR_EQUAL))
        {
            this.addCheckCurrentGameDayGreaterOrEqual(attributeMap);
        }

        if (attributeMap.has(GET_CURRENT_GAME_DAY_LESS_OR_EQUAL))
        {
            this.addCheckCurrentGameDayLessOrEqual(attributeMap);
        }

        if (attributeMap.has(GET_CURRENT_GAME_DAY_INTERVAL))
        {
            this.addCheckCurrentGameDayInterval(attributeMap);
        }

        if (attributeMap.has(MOB))
        {
            this.addMobsCheck(attributeMap);
        }

        if (attributeMap.has(ANIMALS))
        {
            this.addInterfaceAnimalsCheck(attributeMap);
        }

        if (attributeMap.has(MONSTERS))
        {
            this.addInterfaceMonstersCheck(attributeMap);
        }

        if (attributeMap.has(PLAYER))
        {
            this.addPlayerCheck(attributeMap);
        }

        if (attributeMap.has(FAKE_PLAYER))
        {
            this.addFakePlayerCheck(attributeMap);
        }

        if (attributeMap.has(REAL_PLAYER))
        {
            this.addRealPlayerCheck(attributeMap);
        }

        if (attributeMap.has(HELD_ITEM))
        {
            this.addHeldItemCheck(attributeMap, HELD_ITEM);
        }

        if (attributeMap.has(PLAYER_HELD_ITEM))
        {
            this.addHeldItemCheck(attributeMap, PLAYER_HELD_ITEM);
        }

        if (attributeMap.has(OFF_HAND_ITEM))
        {
            this.addOffHandItemCheck(attributeMap);
        }

        if (attributeMap.has(BOTH_HANDS_ITEM))
        {
            this.addBothHandsItemCheck(attributeMap);
        }

        if (attributeMap.has(EXPLOSION))
        {
            this.addExplosionCheck(attributeMap);
        }

        if (attributeMap.has(PROJECTILE))
        {
            this.addProjectileCheck(attributeMap);
        }

        if (attributeMap.has(FIRE))
        {
            this.addFireCheck(attributeMap);
        }

        if (attributeMap.has(MAGIC))
        {
            this.addMagicCheck(attributeMap);
        }

        if (attributeMap.has(SOURCE))
        {
            this.addSourceCheck(attributeMap);
        }

        if (attributeMap.has(RANDOM_KEY_0))
        {
            this.addRandomCheck_0(attributeMap);
        }

        if (attributeMap.has(RANDOM_KEY_1))
        {
            this.addRandomCheck_1(attributeMap);
        }

        if (attributeMap.has(RANDOM_KEY_2))
        {
            this.addRandomCheck_2(attributeMap);
        }

        if (attributeMap.has(RANDOM_KEY_3))
        {
            this.addRandomCheck_3(attributeMap);
        }

        if (attributeMap.has(RANDOM_KEY_4))
        {
            this.addRandomCheck_4(attributeMap);
        }
    }

    /**
     *
     * @param attributeMap
     */
    private void addSeeSkyCheck(AttributeMap<?> attributeMap)
    {
        Object seeSky = attributeMap.get(SEE_SKY);

        if ((Boolean)seeSky)
        {
            this.ARRAY_LIST.add((event,query) ->
                    query.getWorld(event).canBlockSeeSky(query.getPos(event)));
        }
        else
        {
            this.ARRAY_LIST.add((event,query) ->
                    !query.getWorld(event).canBlockSeeSky(query.getPos(event)));
        }
    }

    /**
     *
     * @param attributeMap
     */
    private void addCanSpawnHereCheck(AttributeMap<?> attributeMap)
    {
        Object canSpawn = attributeMap.get(CAN_SPAWN_HERE);

        if ((Boolean)canSpawn)
        {
            this.ARRAY_LIST.add((event, query) ->
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
            this.ARRAY_LIST.add((event, query) ->
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
     * @param attributeMap
     */
    private void addNotCollidingCheck(AttributeMap<?> attributeMap)
    {
        Object notCollidingCheck = attributeMap.get(NOT_COLLIDING);

        if ((Boolean)notCollidingCheck)
        {
            this.ARRAY_LIST.add((event, query) ->
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
            this.ARRAY_LIST.add((event, query) ->
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
     * @param attributeMap
     */
    private void addSpawnerCheck(AttributeMap<?> attributeMap)
    {
        Object spawner = attributeMap.get(SPAWNER);

        if ((Boolean)spawner)
        {
            this.ARRAY_LIST.add((event, query) ->
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
            this.ARRAY_LIST.add((event, query) ->
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
     * @param attributeMap
     */
    private void addBiomesCheck(AttributeMap<?> attributeMap)
    {
        List<String> biomes = attributeMap.getList(BIOMES);

        if (biomes.size() == 1)
        {
            String biomesName = biomes.get(0);

            this.ARRAY_LIST.add((event,query) ->
            {
                Biome biome = query.getWorld(event).getBiome(query.getPos(event));
                return biomesName.equals(biome.getBiomeName());
            });
        }
        else
        {
            Set<String> biomesName = new HashSet<>(biomes);

            this.ARRAY_LIST.add((event,query) ->
            {
                Biome biome = query.getWorld(event).getBiome(query.getPos(event));
                return biomesName.contains(biome.getBiomeName());
            });
        }
    }

    /**
     *
     * @param attributeMap
     */
    private void addBiomesTypesCheck(AttributeMap<?> attributeMap)
    {
        List<String> biomesTypes = attributeMap.getList(BIOMES_TYPE);

        if (biomesTypes.size() == 1)
        {
            String biomesType = biomesTypes.get(0);

            BiomeDictionary.Type type = BiomeDictionary.Type.getType(biomesType);

            this.ARRAY_LIST.add((event,query) ->
            {
                Biome biome = query.getWorld(event).getBiome(query.getPos(event));
                return BiomeDictionary.getTypes(biome).contains(type);
            });
        }
        else
        {
            Set<BiomeDictionary.Type> types = new HashSet<>();

            for (String biomes : biomesTypes)
            {
                types.add(BiomeDictionary.Type.getType(biomes));
            }

            this.ARRAY_LIST.add((event,query) ->
            {
                Biome biome = query.getWorld(event).getBiome(query.getPos(event));
                return BiomeDictionary.getTypes(biome).stream().anyMatch(s -> types.contains(s));
            });
        }
    }

    /**
     *
     * @param attributeMap
     */
    private void addWeatherCheck(AttributeMap<?> attributeMap)
    {
        Object weatherObject = attributeMap.get(WEATHER);

        if (weatherObject instanceof String)
        {
            String weather = (String) weatherObject;

            boolean raining = weather.toLowerCase().startsWith("rain");
            boolean thunder = weather.toLowerCase().startsWith("thunder");

            if (raining)
            {
                this.ARRAY_LIST.add((event, query) ->
                        query.getWorld(event).isRaining());
            }
            else if (thunder)
            {
                this.ARRAY_LIST.add((event, query) ->
                        query.getWorld(event).isThundering());
            }
            else
            {
                Log.writeDataToLogFile(2, "Unknown weather '" + weather + "'! Use 'rain' or 'thunder'");
            }
        }
        else
        {
            Log.writeDataToLogFile(2, "Weather is not a string object!");
        }
    }

    /**
     *
     * @param attributeMap
     */
    private void addStructureCheck(AttributeMap<?> attributeMap)
    {
        Object structure = attributeMap.get(STRUCTURE);

        this.ARRAY_LIST.add((event,query) ->
                Structure.STRUCTURES_CACHE.isInStructure(query.getWorld(event), (String) structure, query.getPos(event)));
    }

    /**
     *
     * @param attributeMap
     */
    private void addDimensionCheck(AttributeMap<?> attributeMap)
    {
        List<Integer> dimensions = attributeMap.getListI(DIMENSION);

        if (dimensions.size() == 1)
        {
            Integer dimension = dimensions.get(0);

            this.ARRAY_LIST.add((event, query) ->
                    query.getWorld(event).provider.getDimension() == dimension);
        }
        else
        {
            Set<Integer> dimensions1 = new HashSet<>(dimensions);

            this.ARRAY_LIST.add((event, query) ->
                    dimensions1.contains(query.getWorld(event).provider.getDimension()));
        }
    }

    /**
     *
     * @param attributeMap
     */
    public void addHelmetCheck(AttributeMap<?> attributeMap)
    {
        List<Predicate<ItemStack>> predicateList = AuxFunction.getItems(attributeMap.getList(HELMET));

        this.ARRAY_LIST.add((event, query) ->
        {
            EntityPlayerMP entityPlayerMP = query.getPlayer(event);

            if (entityPlayerMP != null)
            {
                ItemStack itemStack =
                        entityPlayerMP.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

                if (!itemStack.isEmpty())
                {
                    return predicateList.stream().anyMatch(item -> item.test(itemStack));
                }
            }

            return false;
        });
    }

    /**
     *
     * @param attributeMap
     */
    public void addChestPlateCheck(AttributeMap<?> attributeMap)
    {
        List<Predicate<ItemStack>> predicateList =
                AuxFunction.getItems(attributeMap.getList(CHEST_PLATE));

        this.ARRAY_LIST.add((event, query) ->
        {
            EntityPlayerMP entityPlayerMP = query.getPlayer(event);

            if (entityPlayerMP != null)
            {
                ItemStack itemStack =
                        entityPlayerMP.getItemStackFromSlot(EntityEquipmentSlot.CHEST);

                if (!itemStack.isEmpty())
                {
                    return predicateList.stream().anyMatch(item -> item.test(itemStack));
                }
            }

            return false;
        });
    }

    /**
     *
     * @param attributeMap
     */
    public void addLeggingsCheck(AttributeMap<?> attributeMap)
    {
        List<Predicate<ItemStack>> predicateList = AuxFunction.getItems(attributeMap.getList(LEGGINGS));

        this.ARRAY_LIST.add((event, query) ->
        {
            EntityPlayerMP entityPlayerMP = query.getPlayer(event);

            if (entityPlayerMP != null)
            {
                ItemStack itemStack =
                        entityPlayerMP.getItemStackFromSlot(EntityEquipmentSlot.LEGS);

                if (!itemStack.isEmpty())
                {
                    return predicateList.stream().anyMatch(item -> item.test(itemStack));
                }
            }

            return false;
        });
    }

    /**
     *
     * @param attributeMap
     */
    public void addBootsCheck(AttributeMap<?> attributeMap)
    {
        List<Predicate<ItemStack>> predicateList = AuxFunction.getItems(attributeMap.getList(BOOTS));

        this.ARRAY_LIST.add((event, query) ->
        {
            EntityPlayerMP entityPlayerMP = query.getPlayer(event);

            if (entityPlayerMP != null)
            {
                ItemStack itemStack =
                        entityPlayerMP.getItemStackFromSlot(EntityEquipmentSlot.FEET);

                if (!itemStack.isEmpty())
                {
                    return predicateList.stream().anyMatch(item -> item.test(itemStack));
                }
            }

            return false;
        });
    }

    /**
     *
     * @param attributeMap
     */
    private void addMinTimeCheck(AttributeMap<?> attributeMap)
    {
        Object minTime = attributeMap.get(MIN_TIME);

        this.ARRAY_LIST.add((event, query) ->
        {
            long time = query.getWorld(event).getWorldTime();
            return (time % 24000L) >= (Long) minTime;
        });
    }

    /**
     *
     * @param attributeMap
     */
    private void addMaxTimeCheck(AttributeMap<?> attributeMap)
    {
        Object maxTime = attributeMap.get(MAX_TIME);

        this.ARRAY_LIST.add((event, query) ->
        {
            long time = query.getWorld(event).getWorldTime();
            return (time % 24000) <= (Long) maxTime;
        });
    }

    /**
     *
     * @param attributeMap
     */
    private void addMinLightCheck(AttributeMap<?> attributeMap)
    {
        Object minLight = attributeMap.get(MIN_LIGHT);

        this.ARRAY_LIST.add((event,query) ->
        {
            BlockPos blockPos = query.getPos(event);
            return query.getWorld(event).getLight(blockPos, true) >= (Integer) minLight;
        });
    }

    /**
     *
     * @param attributeMap
     */
    private void addMaxLightCheck(AttributeMap<?> attributeMap)
    {
        Object maxLight = attributeMap.get(MAX_LIGHT);

        this.ARRAY_LIST.add((event,query) ->
        {
            BlockPos blockPos = query.getPos(event);
            return query.getWorld(event).getLight(blockPos, true) <= (Integer) maxLight;
        });
    }

    /**
     *
     * @param attributeMap
     */
    private void addMinHeightCheck(AttributeMap<?> attributeMap)
    {
        Object minHeight = attributeMap.get(MIN_HEIGHT);

        this.ARRAY_LIST.add((event,query) ->
                query.getY(event) >= (Integer) minHeight);
    }

    /**
     *
     * @param attributeMap
     */
    private void addMaxHeightCheck(AttributeMap<?> attributeMap)
    {
        Object maxHeight = attributeMap.get(MAX_HEIGHT);

        this.ARRAY_LIST.add((event,query) ->
                query.getY(event) <= (Integer) maxHeight);
    }

    /**
     *
     * @param attributeMap
     */
    private void addMinAdditionalDifficultyCheck(AttributeMap<?> attributeMap)
    {
        Object minDifficulty = attributeMap.get(MIN_DIFFICULTY);

        this.ARRAY_LIST.add((event,query) ->
                query.getWorld(event).getDifficultyForLocation(
                        query.getPos(event)).getAdditionalDifficulty() >= (Float) minDifficulty);
    }

    /**
     *
     * @param attributeMap
     */
    private void addDifficultyCheck(AttributeMap<?> attributeMap)
    {
        EnumDifficulty enumDifficulty = null;

        Object difficulty = attributeMap.get(DIFFICULTY);

        for (EnumDifficulty enumDifficulty1 : EnumDifficulty.values())
        {
            if (enumDifficulty1.getDifficultyResourceKey().endsWith("." + difficulty))
            {
                enumDifficulty = enumDifficulty1;
                break;
            }
        }

        if (enumDifficulty != null)
        {
            EnumDifficulty enumDifficulty1 = enumDifficulty;

            this.ARRAY_LIST.add((event,query) ->
                    query.getWorld(event).getDifficulty() == enumDifficulty1);
        }
        else
        {
            Log.writeDataToLogFile(2, "Unknown difficulty '" +
                    difficulty + "'! Use one of 'easy', 'normal', 'hard',  or 'peaceful'");

            throw new RuntimeException();
        }
    }

    /**
     *
     * @param attributeMap
     */
    private void addMaxAdditionalDifficultyCheck(AttributeMap<?> attributeMap)
    {
        Object maxDifficulty = attributeMap.get(MAX_DIFFICULTY);

        this.ARRAY_LIST.add((event,query) ->
                query.getWorld(event).getDifficultyForLocation(
                        query.getPos(event)).getAdditionalDifficulty() <= (Float) maxDifficulty);
    }

    /**
     *
     * @param attributeMap
     */
    private void addMinSpawnDistCheck(AttributeMap<?> attributeMap)
    {
        Object degree = attributeMap.get(MIN_SPAWN_DIST);

        this.ARRAY_LIST.add((event,query) ->
        {
            BlockPos blockPos = query.getPos(event);
            double sqDist = blockPos.distanceSq(query.getWorld(event).getSpawnPoint());

            return sqDist >= (Float) degree * (Float) degree;
        });
    }

    /**
     *
     * @param attributeMap
     */
    private void addMaxSpawnDistCheck(AttributeMap<?> attributeMap)
    {
        Object degree = attributeMap.get(MAX_SPAWN_DIST);

        this.ARRAY_LIST.add((event, query) ->
        {
            BlockPos blockPos = query.getPos(event);
            double sqDist = blockPos.distanceSq(query.getWorld(event).getSpawnPoint());

            return sqDist <= (Float) degree * (Float) degree;
        });
    }

    /**
     *
     * @param attributeMap
     */
    private void addBlocksCheck(AttributeMap<?> attributeMap)
    {
        BiFunction<Event, SignalDataAccessor, BlockPos> posFunction;

        if (attributeMap.has(BLOCK_OFFSET))
        {
            posFunction =
                    AuxFunction.parseOffset((String)attributeMap.get(BLOCK_OFFSET));
        }
        else
        {
            posFunction = (event, query) ->
                    query.getPos(event);
        }

        List<String> blocks = attributeMap.getList(BLOCK);

        if (blocks.size() == 1)
        {
            String json = blocks.get(0);
            BiPredicate<World, BlockPos> blockPosBiPredicate = AuxFunction.parseBlock(json);

            if (blockPosBiPredicate != null)
            {
                this.ARRAY_LIST.add((event, query) ->
                {
                    BlockPos blockPos = posFunction.apply(event, query);

                    return blockPos != null && blockPosBiPredicate.test(query.getWorld(event), blockPos);
                });
            }
        }
        else
        {
            List<BiPredicate<World, BlockPos>> blockMatchers = new ArrayList<>();

            for (String block : blocks)
            {
                BiPredicate<World, BlockPos> blockMatcher = AuxFunction.parseBlock(block);

                if (blockMatcher == null)
                {
                    return;
                }

                blockMatchers.add(blockMatcher);
            }

            this.ARRAY_LIST.add((event,query) ->
            {
                BlockPos blockPos = posFunction.apply(event, query);

                if (blockPos != null)
                {
                    World world = query.getWorld(event);

                    for (BiPredicate<World, BlockPos> matcher : blockMatchers)
                    {
                        if (matcher.test(world, blockPos))
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
     * @param attributeMap
     */
    private void addCheckMoonPhase(AttributeMap<?> attributeMap)
    {
        Object moon = attributeMap.get(GET_MOON_PHASE);

        this.ARRAY_LIST.add((event,query) ->
                query.getWorld(event).getMoonPhase() == (Integer)moon);
    }

    /**
     *
     * @param attributeMap
     */
    private void addCheckCurrentGameDayEqual(AttributeMap<?> attributeMap)
    {
        Object targetDay = attributeMap.get(GET_CURRENT_GAME_DAY_EQUAL);

        this.ARRAY_LIST.add((event, query) ->
        {
            World world = query.getWorld(event);

            if (world != null)
            {
                long currentDay = world.getWorldTime() / 24000L;
                return currentDay == (Long) targetDay;
            }

            return false;
        });
    }

    /**
     *
     * @param attributeMap
     */
    private void addCheckCurrentGameDayGreater(AttributeMap<?> attributeMap)
    {
        Object targetDay = attributeMap.get(GET_CURRENT_GAME_DAY_GREATER);

        this.ARRAY_LIST.add((event, query) ->
        {
            World world = query.getWorld(event);

            if (world != null)
            {
                long currentDay = world.getWorldTime() / 24000L;
                return currentDay > (Long) targetDay;
            }

            return false;
        });
    }

    /**
     *
     * @param attributeMap
     */
    private void addCheckCurrentGameDayLess(AttributeMap<?> attributeMap)
    {
        Object targetDay = attributeMap.get(GET_CURRENT_GAME_DAY_LESS);

        this.ARRAY_LIST.add((event, query) ->
        {
            World world = query.getWorld(event);

            if (world != null)
            {
                long currentDay = world.getWorldTime() / 24000L;
                return currentDay < (Long) targetDay;
            }

            return false;
        });
    }

    /**
     *
     * @param attributeMap
     */
    private void addCheckCurrentGameDayGreaterOrEqual(AttributeMap<?> attributeMap)
    {
        Object targetDay = attributeMap.get(GET_CURRENT_GAME_DAY_GREATER_OR_EQUAL);

        this.ARRAY_LIST.add((event, query) ->
        {
            World world = query.getWorld(event);

            if (world != null)
            {
                long currentDay = world.getWorldTime() / 24000L;
                return currentDay >= (Long) targetDay;
            }

            return false;
        });
    }

    /**
     *
     * @param attributeMap
     */
    private void addCheckCurrentGameDayLessOrEqual(AttributeMap<?> attributeMap)
    {
        Object targetDay = attributeMap.get(GET_CURRENT_GAME_DAY_LESS_OR_EQUAL);

        this.ARRAY_LIST.add((event, query) ->
        {
            World world = query.getWorld(event);

            if (world != null)
            {
                long currentDay = world.getWorldTime() / 24000L;
                return currentDay <= (Long) targetDay;
            }

            return false;
        });
    }

    /**
     *
     * @param attributeMap
     */
    private void addCheckCurrentGameDayInterval(AttributeMap<?> attributeMap)
    {
        Object intervalObj = attributeMap.get(GET_CURRENT_GAME_DAY_INTERVAL);

        long interval = (Long) intervalObj;

        this.ARRAY_LIST.add((event, query) ->
        {
            World world = query.getWorld(event);

            if (world != null)
            {
                long currentDay = world.getWorldTime() / 24000L;
                return currentDay % interval == 0L;
            }

            return false;
        });
    }

    /**
     *
     * @param attributeMap
     */
    private void addMobsCheck(AttributeMap<?> attributeMap)
    {
        List<String> listMobs = attributeMap.getList(MOB);

        if (listMobs.size() == 1)
        {
            String name = listMobs.get(0);
            String id = GenericPotentialSpawn.fixEntityId(name);

            EntityEntry entityEntry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(id));
            Class<? extends Entity> typeClass = entityEntry == null ? null : entityEntry.getEntityClass();

            if (typeClass != null)
            {
                this.ARRAY_LIST.add((event, query) ->
                        typeClass.equals(query.getEntity(event).getClass()));
            }
            else
            {
                Log.writeDataToLogFile(2, "Unknown mob '" + name + "'!");
                throw new RuntimeException();
            }
        }
        else
        {
            Set<Class<?>> classes = new HashSet<>();

            for (String name : listMobs)
            {
                String id = GenericPotentialSpawn.fixEntityId(name);
                EntityEntry entityEntry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(id));

                Class<? extends Entity> typeClass = entityEntry == null ? null : entityEntry.getEntityClass();

                if (typeClass != null)
                {
                    classes.add(typeClass);
                }
                else
                {
                    Log.writeDataToLogFile(2, "Unknown mob '" + name + "'!");
                    throw new RuntimeException();
                }
            }

            if (!classes.isEmpty())
            {
                this.ARRAY_LIST.add((event, query) ->
                        classes.contains(query.getEntity(event).getClass()));
            }
        }
    }

    /**
     *
     * @param attributeMap
     */
    private void addInterfaceAnimalsCheck(AttributeMap<?> attributeMap)
    {
        Object animalsObj = attributeMap.get(ANIMALS);

        if ((Boolean)animalsObj)
        {
            this.ARRAY_LIST.add((event, query) ->
                    (query.getEntity(event) instanceof IAnimals
                            && !(query.getEntity(event) instanceof IMob)));
        }
        else
        {
            this.ARRAY_LIST.add((event, query) ->
                    !(query.getEntity(event) instanceof IAnimals
                            && !(query.getEntity(event) instanceof IMob)));
        }
    }

    /**
     *
     * @param attributeMap
     */
    private void addInterfaceMonstersCheck(AttributeMap<?> attributeMap)
    {
        Object monstersObj = attributeMap.get(MONSTERS);

        if ((Boolean)monstersObj)
        {
            this.ARRAY_LIST.add((event, query) ->
                    query.getEntity(event) instanceof IMob);
        }
        else
        {
            this.ARRAY_LIST.add((event, query) ->
                    !(query.getEntity(event) instanceof IMob));
        }
    }

    /**
     *
     * @param attributeMap
     */
    private void addPlayerCheck(AttributeMap<?> attributeMap)
    {
        Object asPlayer = attributeMap.get(PLAYER);

        if ((Boolean)asPlayer)
        {
            this.ARRAY_LIST.add((event, query) ->
                    query.getAttacker(event) instanceof EntityPlayerMP);
        }
        //else
        //{
        //    _arrayList.add((event, query) -> query.getAttacker(event) instanceof EntityPlayerMP);
        //}
    }

    /**
     *
     * @param attributeMap
     */
    private void addFakePlayerCheck(AttributeMap<?> attributeMap)
    {
        Object asPlayer = attributeMap.get(FAKE_PLAYER);

        if ((Boolean)asPlayer)
        {
            this.ARRAY_LIST.add((event, query) ->
                    query.getAttacker(event) == null ? false :
                        isFakePlayer(query.getAttacker(event)));
        }
        else
        {
            this.ARRAY_LIST.add((event, query) ->
                    query.getAttacker(event) == null ? true :
                        !isFakePlayer(query.getAttacker(event)));
        }
    }

    /**
     *
     * @param attributeMap
     */
    private void addRealPlayerCheck(AttributeMap<?> attributeMap)
    {
        Object asPlayer = attributeMap.get(REAL_PLAYER);

        if ((Boolean)asPlayer)
        {
            this.ARRAY_LIST.add((event, query) ->
                    query.getAttacker(event) == null ? false :
                            isRealPlayer(query.getAttacker(event)));
        }
        else
        {
            this.ARRAY_LIST.add((event, query) ->
                    query.getAttacker(event) == null ? true :
                            !isRealPlayer(query.getAttacker(event)));
        }
    }

    /**
     *
     * @param attributeMap
     * @param attributeKey
     */
    public void addHeldItemCheck(AttributeMap<?> attributeMap, AttributeKey<String> attributeKey)
    {
        List<Predicate<ItemStack>> items = AuxFunction.getItems(attributeMap.getList(attributeKey));

        this.ARRAY_LIST.add((event,query) ->
        {
            EntityPlayerMP entityPlayerMP = query.getPlayer(event);

            if (entityPlayerMP != null)
            {
                ItemStack itemStack = entityPlayerMP.getHeldItemMainhand();

                if (!itemStack.isEmpty())
                {
                    for (Predicate<ItemStack> item : items)
                    {
                        if (item.test(itemStack))
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
     * @param attributeMap
     */
    public void addOffHandItemCheck(AttributeMap<?> attributeMap)
    {
        List<Predicate<ItemStack>> items = AuxFunction.getItems(attributeMap.getList(OFF_HAND_ITEM));

        this.ARRAY_LIST.add((event,query) ->
        {
            EntityPlayerMP entityPlayerMP = query.getPlayer(event);

            if (entityPlayerMP != null)
            {
                ItemStack offhand = entityPlayerMP.getHeldItemOffhand();

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
     * @param attributeMap
     */
    private void addExplosionCheck(AttributeMap<?> attributeMap)
    {
        Object explosion = attributeMap.get(EXPLOSION);

        if ((Boolean)explosion)
        {
            this.ARRAY_LIST.add((event, query) ->
                    query.getSource(event) == null ? false :
                            query.getSource(event).isExplosion());
        }
        else
        {
            this.ARRAY_LIST.add((event, query) ->
                    query.getSource(event) == null ? true :
                            !query.getSource(event).isExplosion());
        }
    }

    /**
     *
     * @param attributeMap
     */
    private void addProjectileCheck(AttributeMap<?> attributeMap)
    {
        Object projectile = attributeMap.get(PROJECTILE);

        if ((Boolean)projectile)
        {
            this.ARRAY_LIST.add((event, query) ->
                    query.getSource(event) == null ? false :
                            query.getSource(event).isProjectile());
        }
        else
        {
            this.ARRAY_LIST.add((event, query) ->
                    query.getSource(event) == null ? true :
                            !query.getSource(event).isProjectile());
        }
    }

    /**
     *
     * @param attributeMap
     */
    private void addFireCheck(AttributeMap<?> attributeMap)
    {
        Object fire = attributeMap.get(FIRE);

        if ((Boolean)fire)
        {
            this.ARRAY_LIST.add((event, query) ->
                    query.getSource(event) == null ? false :
                            query.getSource(event).isFireDamage());
        }
        else
        {
            this.ARRAY_LIST.add((event, query) ->
                    query.getSource(event) == null ? true :
                            !query.getSource(event).isFireDamage());
        }
    }

    /**
     *
     * @param attributeMap
     */
    private void addMagicCheck(AttributeMap<?> attributeMap)
    {
        Object magic = attributeMap.get(MAGIC);

        if ((Boolean)magic)
        {
            this.ARRAY_LIST.add((event, query) ->
                    query.getSource(event) == null ? false :
                            query.getSource(event).isMagicDamage());
        }
        else
        {
            this.ARRAY_LIST.add((event, query) ->
                    query.getSource(event) == null ? true :
                            !query.getSource(event).isMagicDamage());
        }
    }

    /**
     *
     * @param attributeMap
     */
    private void addSourceCheck(AttributeMap<?> attributeMap)
    {
        List<String> sources = attributeMap.getList(SOURCE);
        Set<String> sourceSet = new HashSet<>(sources);

        this.ARRAY_LIST.add((event, query) ->
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
     * @param attributeMap
     */
    public void addBothHandsItemCheck(AttributeMap<?> attributeMap)
    {
        List<Predicate<ItemStack>> items = AuxFunction.getItems(attributeMap.getList(BOTH_HANDS_ITEM));

        this.ARRAY_LIST.add((event,query) ->
        {
            EntityPlayerMP entityPlayerMP = query.getPlayer(event);

            if (entityPlayerMP != null)
            {
                ItemStack itemStack = entityPlayerMP.getHeldItemOffhand();

                if (!itemStack.isEmpty())
                {
                    for (Predicate<ItemStack> item : items)
                    {
                        if (item.test(itemStack))
                        {
                            return true;
                        }
                    }
                }

                ItemStack itemStack1 = entityPlayerMP.getHeldItemMainhand();

                if (!itemStack1.isEmpty())
                {
                    for (Predicate<ItemStack> item : items)
                    {
                        if (item.test(itemStack1))
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
     * @param attributeMap
     */
    private void addRandomCheck_0(AttributeMap<?> attributeMap)
    {
        Object random = attributeMap.get(RANDOM_KEY_0);

        this.ARRAY_LIST.add((event, query) ->
                UniqueField.RANDOM.nextFloat() < (Float) random);
    }

    /**
     *
     * @param attributeMap
     */
    private void addRandomCheck_1(AttributeMap<?> attributeMap)
    {
        Object random = attributeMap.get(RANDOM_KEY_1);

        this.ARRAY_LIST.add((event, query) ->
                UniqueField.RANDOM.nextFloat() < (Float) random);
    }

    /**
     *
     * @param attributeMap
     */
    private void addRandomCheck_2(AttributeMap<?> attributeMap)
    {
        Object random = attributeMap.get(RANDOM_KEY_2);

        this.ARRAY_LIST.add((event, query) ->
                UniqueField.RANDOM.nextFloat() < (Float) random);
    }

    /**
     *
     * @param attributeMap
     */
    private void addRandomCheck_3(AttributeMap<?> attributeMap)
    {
        Object random = attributeMap.get(RANDOM_KEY_3);

        this.ARRAY_LIST.add((event, query) ->
                UniqueField.RANDOM.nextFloat() < (Float) random);
    }

    /**
     *
     * @param attributeMap
     */
    private void addRandomCheck_4(AttributeMap<?> attributeMap)
    {
        Object random = attributeMap.get(RANDOM_KEY_4);

        this.ARRAY_LIST.add((event, query) ->
                UniqueField.RANDOM.nextFloat() < (Float) random);
    }
}
