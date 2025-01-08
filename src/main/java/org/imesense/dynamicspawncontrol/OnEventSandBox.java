package org.imesense.dynamicspawncontrol;

import akka.event.Logging;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.terraingen.BiomeEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.api.IDebug;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import net.minecraft.block.BlockTallGrass;
import org.imesense.dynamicspawncontrol.core.worldcache.CacheStorage;

/**
 *
 */
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventSandBox implements IDebug
{
    private static final File SAVE_FILE = new File(
            DimensionManager.getCurrentSaveRootDirectory(), "entity_registry.json");

    private static final Map<UUID, EntityData> ENTITY_DATA_MAP = new HashMap<>();

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        loadEntityData();
    }

    @SubscribeEvent
    public void onWorldSave(WorldEvent.Save event) {
        saveEntityData();
    }

    @SubscribeEvent
    public void onChunkLoad(ChunkEvent.Load event)
    {
        World world = event.getWorld();
        if (!world.isRemote)
        {
            Chunk chunk = event.getChunk();
            for (ClassInheritanceMultiMap<Entity> entityList : chunk.getEntityLists())
            {
                for (Entity entity : entityList)
                {
                    if (!(entity instanceof EntityPlayer))
                    {
                        UUID entityId = entity.getUniqueID();
                        if (!ENTITY_DATA_MAP.containsKey(entityId))
                        {
                            saveEntityState(entity);
                            entity.setDead();
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onChunkUnload(ChunkEvent.Unload event)
    {
        World world = event.getWorld();
        if (!world.isRemote)
        {
            Chunk chunk = event.getChunk();
            for (ClassInheritanceMultiMap<Entity> entityList : chunk.getEntityLists())
            {
                for (Entity entity : entityList)
                {
                    if (!(entity instanceof EntityPlayer))
                    {
                        saveEntityState(entity);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onChunkEnter(PlayerEvent.StartTracking event)
    {
        if (!(event.getTarget() instanceof EntityPlayer))
        {
            Entity entity = event.getTarget();
            World world = entity.world;
            if (!world.isRemote)
            {
                UUID entityId = entity.getUniqueID();
                Entity recreatedEntity = recreateEntity(world, entityId);
                if (recreatedEntity != null)
                {
                    LogHelper.logInfo("Spawning entity: " + recreatedEntity.getName() + " at " + recreatedEntity.getPosition());
                    world.spawnEntity(recreatedEntity);
                }
                else
                {
                    LogHelper.logInfo("Entity not found in saved data: " + entityId);
                }
            }
        }
    }

    private void saveEntityState(Entity entity)
    {
        UUID entityId = entity.getUniqueID();
        BlockPos pos = entity.getPosition();
        NBTTagCompound nbt = new NBTTagCompound();
        entity.writeToNBT(nbt);
        ENTITY_DATA_MAP.put(entityId, new EntityData(pos, nbt));
    }

    private Entity recreateEntity(World world, UUID entityId)
    {
        EntityData data = ENTITY_DATA_MAP.get(entityId);
        if (data != null)
        {
            Entity entity = EntityList.createEntityFromNBT(data.nbt, world);
            if (entity != null)
            {
                entity.setPosition(data.pos.getX() + 0.5, data.pos.getY(), data.pos.getZ() + 0.5);
                return entity;
            }
            else
            {
                LogHelper.logError("Failed to recreate entity from NBT: " + entityId);
            }
        }
        return null;
    }

    private void saveEntityData()
    {
        try (Writer writer = new FileWriter(SAVE_FILE))
        {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonArray jsonArray = new JsonArray();

            for (Map.Entry<UUID, EntityData> entry : ENTITY_DATA_MAP.entrySet())
            {
                JsonObject jsonEntity = new JsonObject();
                jsonEntity.addProperty("uuid", entry.getKey().toString());
                jsonEntity.addProperty("x", entry.getValue().pos.getX());
                jsonEntity.addProperty("y", entry.getValue().pos.getY());
                jsonEntity.addProperty("z", entry.getValue().pos.getZ());
                jsonEntity.add("nbt", JsonUtils.convertNBTToJson(entry.getValue().nbt));
                jsonArray.add(jsonEntity);
            }

            String jsonOutput = gson.toJson(jsonArray);

            writer.write(jsonOutput);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void loadEntityData()
    {
        if (SAVE_FILE.exists())
        {
            try (FileReader fileReader = new FileReader(SAVE_FILE);
                 JsonReader reader = new JsonReader(fileReader))
            {
                JsonElement jsonElement = new JsonParser().parse(reader);
                if (jsonElement.isJsonArray())
                {
                    JsonArray jsonArray = jsonElement.getAsJsonArray();
                    for (JsonElement element : jsonArray)
                    {
                        if (element.isJsonObject())
                        {
                            JsonObject jsonEntity = element.getAsJsonObject();
                            UUID entityId = UUID.fromString(jsonEntity.get("uuid").getAsString());
                            BlockPos pos = new BlockPos(
                                    jsonEntity.get("x").getAsInt(),
                                    jsonEntity.get("y").getAsInt(),
                                    jsonEntity.get("z").getAsInt()
                            );
                            JsonObject nbtObject = jsonEntity.getAsJsonObject("nbt");
                            NBTTagCompound nbt = JsonUtils.convertJsonToNBT(nbtObject);
                            ENTITY_DATA_MAP.put(entityId, new EntityData(pos, nbt));
                        }
                    }
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private static class EntityData
    {
        private final BlockPos pos;
        private final NBTTagCompound nbt;

        public EntityData(BlockPos pos, NBTTagCompound nbt)
        {
            this.pos = pos;
            this.nbt = nbt;
        }
    }

    private static class LogHelper
    {
        public static void logInfo(String message)
        {
            Log.writeDataToLogFile(1,"[INFO] " + message);
        }

        public static void logError(String message)
        {
            Log.writeDataToLogFile(2,"[ERROR] " + message);
        }
    }

    private static class JsonUtils
    {
        public static JsonObject convertNBTToJson(NBTTagCompound nbt)
        {
            return new JsonParser().parse(nbt.toString()).getAsJsonObject();
        }

        public static NBTTagCompound convertJsonToNBT(JsonObject json)
        {
            try
            {
                return JsonToNBT.getTagFromJson(json.toString());
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return new NBTTagCompound();
            }
        }
    }
}
