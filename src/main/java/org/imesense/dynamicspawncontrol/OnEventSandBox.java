package org.imesense.dynamicspawncontrol;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.api.IDebug;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.worldmemoryregistry.Option;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

/**
 *
 */
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventSandBox implements IDebug
{
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File ENTITY_FILE = new File("./entity_registry.json");
    private static final List<EntityData> ENTITY_LIST = new ArrayList<>();
    private static final double TRACK_RADIUS = 64.0;

    @Mod.EventHandler
    public void onServerStart(FMLServerStartingEvent event)
    {
        Log.writeDataToLogFile(2, "[OnEventSandBox] Server started, loading entities from file.");
        loadEntitiesFromFile();
    }

    @SubscribeEvent
    public void onEntityJoin(EntityJoinWorldEvent event) {

        if (event.getEntity() instanceof EntityPlayer)
        {
            Log.writeDataToLogFile(2, "[OnEventSandBox] Player joined, checking and respawning entities.");
            checkAndRespawnEntities(event.getWorld(), (EntityPlayer) event.getEntity());
        }
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            for (EntityPlayer player : event.world.playerEntities)
            {
                trackAndSaveEntities(event.world, player);
                checkAndRespawnEntities(event.world, player);
            }
        }
    }

    private void trackAndSaveEntities(World world, EntityPlayer player) {
        Iterator<EntityData> iterator = ENTITY_LIST.iterator();

        while (iterator.hasNext())
        {
            EntityData data = iterator.next();

            if (player.getDistance(data.x, data.y, data.z) > TRACK_RADIUS)
            {
                Log.writeDataToLogFile(2, String.format("[OnEventSandBox] Entity left visible range, saving to file: %s at [%f, %f, %f]", data.entityId, data.x, data.y, data.z));
                saveEntitiesToFile();
            }
        }

        for (Entity entity : world.loadedEntityList)
        {
            if (!(entity instanceof EntityPlayer) && player.getDistance(entity) > TRACK_RADIUS)
            {
                ResourceLocation entityKey = EntityList.getKey(entity);
                if (entityKey != null && entityKey.equals(new ResourceLocation("minecraft", "zombie_pigman")))
                {
                    boolean isTracked = ENTITY_LIST.stream().anyMatch(data -> data.matches(entity));

                    if (!isTracked)
                    {
                        ENTITY_LIST.add(new EntityData(entity));
                        Log.writeDataToLogFile(2, String.format("[OnEventSandBox] Added entity to registry: %s at [%f, %f, %f]", entityKey, entity.posX, entity.posY, entity.posZ));
                    }
                }
            }
        }

        saveEntitiesToFile();
    }

    private void checkAndRespawnEntities(World world, EntityPlayer player)
    {
        Log.writeDataToLogFile(2, "[OnEventSandBox] Checking for entities to respawn.");
        Iterator<EntityData> iterator = ENTITY_LIST.iterator();

        while (iterator.hasNext())
        {
            EntityData data = iterator.next();
            if (player.getDistance(data.x, data.y, data.z) <= TRACK_RADIUS)
            {
                Entity entity = EntityList.createEntityByIDFromName(new ResourceLocation(data.entityId), world);
                if (entity != null)
                {
                    entity.setPosition(data.x, data.y, data.z);
                    entity.readFromNBT(data.nbtData);
                    world.spawnEntity(entity);

                    Log.writeDataToLogFile(2, String.format("[OnEventSandBox] Respawned entity: %s at [%f, %f, %f]", data.entityId, data.x, data.y, data.z));
                    iterator.remove();
                    saveEntitiesToFile();
                }
                else
                {
                    Log.writeDataToLogFile(2, String.format("[OnEventSandBox] Failed to respawn entity: %s at [%f, %f, %f]", data.entityId, data.x, data.y, data.z));
                }
            }
        }
    }

    private void saveEntitiesToFile()
    {
        try (FileWriter writer = new FileWriter(ENTITY_FILE))
        {
            GSON.toJson(ENTITY_LIST, writer);
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    private void loadEntitiesFromFile()
    {
        if (ENTITY_FILE.exists())
        {
            try (FileReader reader = new FileReader(ENTITY_FILE))
            {
                Type listType = new TypeToken<List<EntityData>>() {}.getType();
                List<EntityData> loadedList = GSON.fromJson(reader, listType);
                if (loadedList != null)
                {
                    ENTITY_LIST.addAll(loadedList);
                }
                Log.writeDataToLogFile(2, "[OnEventSandBox] Loaded entities from file.");
            }
            catch (IOException exception)
            {
                exception.printStackTrace();
            }
        }
    }

    private static class EntityData
    {
        String entityId;
        double x, y, z;
        NBTTagCompound nbtData;

        EntityData(Entity entity)
        {
            this.entityId = EntityList.getKey(entity).toString();
            this.x = entity.posX;
            this.y = entity.posY;
            this.z = entity.posZ;
            this.nbtData = new NBTTagCompound();
            entity.writeToNBT(this.nbtData);
        }

        boolean matches(Entity entity)
        {
            return this.entityId.equals(EntityList.getKey(entity).toString()) && entity.getPosition().equals(new BlockPos(this.x, this.y, this.z));
        }
    }

    private static boolean instanceExists = false;

    public OnEventSandBox()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }
        instanceExists = true;
    }
}
