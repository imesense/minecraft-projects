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
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
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
    //-' TODO: одновременное удаление и запись в файл, ломает основной поток программы и вызывает исключение
    /**
     * java.util.ConcurrentModificationException
     * 	at java.util.HashMap$HashIterator.nextNode(HashMap.java:1469)
     * 	at java.util.HashMap$KeyIterator.next(HashMap.java:1493)
     * 	at net.minecraft.entity.EntityTracker.tick(EntityTracker.java:309)
     * 	at net.minecraft.server.MinecraftServer.updateTimeLightAndEntities(MinecraftServer.java:779)
     * 	at net.minecraft.server.MinecraftServer.tick(MinecraftServer.java:668)
     * 	at net.minecraft.server.integrated.IntegratedServer.tick(IntegratedServer.java:185)
     * 	at net.minecraft.server.MinecraftServer.run(MinecraftServer.java:526)
     * 	at java.lang.Thread.run(Thread.java:750)
     */
    
    private static final File SAVE_FILE = new File(
            DimensionManager.getCurrentSaveRootDirectory(), "entity_registry.json");

    private static final Map<String, EntityData> ENTITY_DATA_MAP = new HashMap<>();

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event)
    {
        loadEntityData();
    }

    @SubscribeEvent
    public void onWorldSave(WorldEvent.Save event)
    {
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
                    String key = getEntityKey(entity);
                    ENTITY_DATA_MAP.remove(key);
                }
            }

            DimensionType dimensionType = DimensionManager.getProviderType(world.provider.getDimension());
            for (String key : ENTITY_DATA_MAP.keySet())
            {
                if (key.startsWith(dimensionType.getName()))
                {
                    Entity recreatedEntity = recreateEntity(world, key);
                    if (recreatedEntity != null)
                    {
                        Log.writeDataToLogFile(0,"Spawning entity: " + recreatedEntity.getName() + " at " + recreatedEntity.getPosition());
                        world.spawnEntity(recreatedEntity);

                        ENTITY_DATA_MAP.remove(key);
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
                        if (isOutsideRenderDistance(entity))
                        {
                            entity.setDead();
                        }
                    }
                }
            }
        }
    }

    private boolean isOutsideRenderDistance(Entity entity)
    {
        MinecraftServer server = entity.getServer();
        if (server == null) return false;

        PlayerList playerList = server.getPlayerList();
        int renderDistance = playerList.getViewDistance();

        BlockPos pos = entity.getPosition();

        for (EntityPlayer player : entity.world.playerEntities)
        {
            BlockPos playerPos = player.getPosition();

            if (Math.abs(playerPos.getX() - pos.getX()) <= renderDistance * 16 &&
                    Math.abs(playerPos.getZ() - pos.getZ()) <= renderDistance * 16)
            {
                return false;
            }
        }
        return true;
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
                String key = getEntityKey(entity);
                Entity recreatedEntity = recreateEntity(world, key);
                if (recreatedEntity != null)
                {
                    Log.writeDataToLogFile(0,"Spawning entity: " + recreatedEntity.getName() + " at " + recreatedEntity.getPosition());
                    world.spawnEntity(recreatedEntity);

                    ENTITY_DATA_MAP.remove(key);
                }
                else
                {
                    Log.writeDataToLogFile(0,"Entity not found in saved data: " + key);
                }
            }
        }
    }

    private void saveEntityState(Entity entity)
    {
        String key = getEntityKey(entity);
        if (key.equals("unknown")) return;

        BlockPos pos = entity.getPosition();
        NBTTagCompound nbt = new NBTTagCompound();
        entity.writeToNBT(nbt);
        ENTITY_DATA_MAP.put(key, new EntityData(pos, nbt));
    }

    private Entity recreateEntity(World world, String key)
    {
        EntityData data = ENTITY_DATA_MAP.get(key);
        if (data != null)
        {
            String entityTypeKey = key.split("_")[0];
            Entity entity = EntityList.createEntityByIDFromName(new ResourceLocation(entityTypeKey), world);
            if (entity != null)
            {
                entity.readFromNBT(data.nbt);
                entity.setPosition(data.pos.getX() + 0.5, data.pos.getY(), data.pos.getZ() + 0.5);
                return entity;
            }
            else
            {
                Log.writeDataToLogFile(0,"Failed to recreate entity of type: " + entityTypeKey);
            }
        }
        return null;
    }

    private String getEntityKey(Entity entity)
    {
        ResourceLocation entityType = EntityList.getKey(entity.getClass());
        if (entityType != null)
        {
            BlockPos pos = entity.getPosition();
            return entityType.toString();
        }
        else
        {
            Log.writeDataToLogFile(0,"Entity type not found for: " + entity);
            return "unknown";
        }
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
                Log.writeDataToLogFile(0,"Failed to recreate entity from NBT: " + entityId);
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

            for (Map.Entry<String, EntityData> entry : ENTITY_DATA_MAP.entrySet())
            {
                JsonObject jsonEntity = new JsonObject();
                jsonEntity.addProperty("key", entry.getKey());
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
                            String key = jsonEntity.get("key").getAsString();
                            BlockPos pos = new BlockPos(
                                    jsonEntity.get("x").getAsInt(),
                                    jsonEntity.get("y").getAsInt(),
                                    jsonEntity.get("z").getAsInt()
                            );
                            JsonObject nbtObject = jsonEntity.getAsJsonObject("nbt");
                            NBTTagCompound nbt = JsonUtils.convertJsonToNBT(nbtObject);
                            ENTITY_DATA_MAP.put(key, new EntityData(pos, nbt));
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
