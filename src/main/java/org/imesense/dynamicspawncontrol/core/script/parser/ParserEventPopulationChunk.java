package org.imesense.dynamicspawncontrol.core.script.parser;

import com.google.gson.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseparser.BaseParser;
import org.imesense.dynamicspawncontrol.core.script.storage.populationchunk.data.PopulationChunkStruct;
import org.imesense.dynamicspawncontrol.core.script.storage.populationchunk.storage.GeneralPopulationChunkSpawn;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@InitLog
public final class ParserEventPopulationChunk extends BaseParser
{
    public ParserEventPopulationChunk(final String NAME_FILE)
    {
        this.nameFile = NAME_FILE;
    }

    @Override
    public void loadConfig(boolean init)
    {
        File file = getConfigFile(init,
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_GAME_SCRIPTS, this.nameFile);

        if (!file.exists())
        {
            this.createNewConfigFile(file);
            return;
        }

        try (FileReader fileReader = new FileReader(file))
        {
            JsonArray jsonArray = new JsonParser().parse(fileReader).getAsJsonArray();

            List<Biome.SpawnListEntry> newSpawnEntries = new ArrayList<>();
            List<PopulationChunkStruct.Data> populationList = new ArrayList<>();

            for (JsonElement topLevelElement : jsonArray)
            {
                try
                {
                    JsonObject topLevelObject = topLevelElement.getAsJsonObject();

                    if (topLevelObject.has("mobs"))
                    {
                        JsonArray mobsArray = topLevelObject.getAsJsonArray("mobs");

                        for (JsonElement mobElement : mobsArray)
                        {
                            try
                            {
                                JsonObject mobMap = mobElement.getAsJsonObject();
                                String id = mobMap.get("mob").getAsString();

                                ResourceLocation mobResource = new ResourceLocation(id);
                                EntityEntry entityEntry = ForgeRegistries.ENTITIES.getValue(mobResource);

                                if (entityEntry == null)
                                {
                                    continue;
                                }

                                Class<? extends Entity> _class = entityEntry.getEntityClass();

                                if (_class == null)
                                {
                                    continue;
                                }

                                Integer weight = mobMap.has("weight") ?
                                        mobMap.get("weight").getAsInt() : 1;

                                Integer idDimension = mobMap.has("id_dimension") ?
                                        mobMap.get("id_dimension").getAsInt() : null;

                                Integer groupCountMin = mobMap.has("groupcountmin") ?
                                        mobMap.get("groupcountmin").getAsInt() : 1;

                                Integer groupCountMax = mobMap.has("groupcountmax") ?
                                        mobMap.get("groupcountmax").getAsInt() : Math.max(groupCountMin, 1);

                                String spawnChancePriority =
                                        mobMap.has("spawn_chance_priority") ?
                                                mobMap.get("spawn_chance_priority").getAsString() : "medium";

                                Integer maxEntitiesPerChunk =
                                        mobMap.has("max_entities_per_chunk") ?
                                                mobMap.get("max_entities_per_chunk").getAsInt() : 1;

                                List<String> biomes = new ArrayList<>();

                                if (mobMap.has("biome"))
                                {
                                    String biomeString = mobMap.get("biome").getAsString();

                                    if (biomeString.contains(","))
                                    {
                                        String[] biomeArray = biomeString.split(",");

                                        for (String biome : biomeArray)
                                        {
                                            String trimmedBiome = biome.trim();
                                            biomes.add(trimmedBiome);
                                        }
                                    }
                                    else
                                    {
                                        String trimmedBiome = biomeString.trim();
                                        biomes.add(trimmedBiome);
                                    }
                                }

                                boolean isWater = mobMap.has("isWater") ? mobMap.get("isWater").getAsBoolean() : false;

                                PopulationChunkStruct.Data data = new PopulationChunkStruct.Data();

                                data.entity = mobResource;
                                data.weight = weight;
                                data.groupCountMin = groupCountMin;
                                data.groupCountMax = groupCountMax;
                                data.biomes = biomes;
                                data.isWater = isWater;
                                data.idDimension = idDimension;
                                data.spawnChancePriority = spawnChancePriority;
                                data.maxEntitiesPerChunk = maxEntitiesPerChunk;

                                populationList.add(data);

                                Biome.SpawnListEntry entry = new Biome.SpawnListEntry(
                                        (Class<? extends EntityLiving>) _class,
                                        weight, groupCountMin, groupCountMax
                                );

                                newSpawnEntries.add(entry);
                            }
                            catch (Exception exception)
                            {

                            }
                        }
                    }
                }
                catch (Exception exception)
                {

                }
            }

            GeneralPopulationChunkSpawn.getInstance().populationChunkStruct = populationList;
        }
        catch (IOException exception)
        {

        }
        catch (JsonSyntaxException exception)
        {

        }
        catch (Exception exception)
        {

        }
    }

    @Override
    public void eraseData()
    {
        GeneralPopulationChunkSpawn.getInstance().populationChunkStruct.clear();
    }
}
