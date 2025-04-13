package org.imesense.dynamicspawncontrol.develop;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.interfaces.IDebug;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

@InitLog
@TODO(
        value = "Есть баг, который тащит общую память по файлам в новый мир, даже если файла там еще не было",
        priority = TODO.TodoPriority.HIGH,
        showOnce = false
)
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventSandBox implements IDebug
{
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final List<EntityData> ENTITY_LIST = new ArrayList<>();
    private static final double TRACK_RADIUS = 64.0;

    private static File getEntityFile(World world)
    {
        File worldDir = world.getSaveHandler().getWorldDirectory();
        return new File(worldDir, "entity_registry.json");
    }

    @Mod.EventHandler
    public void onServerStart(FMLServerStartingEvent event)
    {
        World world = event.getServer().getEntityWorld();
        //Log.writeDataToLogFile(2, "[OnEventSandBox] Server started, loading entities from file.");
        loadEntitiesFromFile(world);
    }

    @SubscribeEvent
    public void onEntityJoin(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof EntityPlayer)
        {
            //Log.writeDataToLogFile(2, "[OnEventSandBox] Player joined, checking and respawning entities.");
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

    private void trackAndSaveEntities(World world, EntityPlayer player)
    {
        Iterator<EntityData> iterator = ENTITY_LIST.iterator();

        while (iterator.hasNext())
        {
            EntityData data = iterator.next();

            if (player.getDistance(data.x, data.y, data.z) > TRACK_RADIUS)
            {
                //Log.writeDataToLogFile(2, String.format("[OnEventSandBox] Entity left visible range, saving to file: %s at [%f, %f, %f]", data.entityId, data.x, data.y, data.z));
                saveEntitiesToFile(world);
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
                        //Log.writeDataToLogFile(2, String.format("[OnEventSandBox] Added entity to registry: %s at [%f, %f, %f]", entityKey, entity.posX, entity.posY, entity.posZ));
                    }
                }
            }
        }

        saveEntitiesToFile(world);
    }

    private void checkAndRespawnEntities(World world, EntityPlayer player)
    {
        //Log.writeDataToLogFile(2, "[OnEventSandBox] Checking for entities to respawn.");
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

                    //Log.writeDataToLogFile(2, String.format("[OnEventSandBox] Respawned entity: %s at [%f, %f, %f]", data.entityId, data.x, data.y, data.z));
                    iterator.remove();
                    saveEntitiesToFile(world);
                }
                else
                {
                    //Log.writeDataToLogFile(2, String.format("[OnEventSandBox] Failed to respawn entity: %s at [%f, %f, %f]", data.entityId, data.x, data.y, data.z));
                }
            }
        }
    }

    private void saveEntitiesToFile(World world)
    {
        File file = getEntityFile(world);
        try (FileWriter writer = new FileWriter(file))
        {
            GSON.toJson(ENTITY_LIST, writer);
            //Log.writeDataToLogFile(2, "[OnEventSandBox] Saved entity data to: " + file.getAbsolutePath());
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    private void loadEntitiesFromFile(World world)
    {
        File file = getEntityFile(world);
        if (file.exists())
        {
            try (FileReader fileReader = new FileReader(file))
            {
                Type listType = new TypeToken<List<EntityData>>() {}.getType();
                List<EntityData> loadedList = GSON.fromJson(fileReader, listType);
                if (loadedList != null)
                {
                    ENTITY_LIST.clear();
                    ENTITY_LIST.addAll(loadedList);
                }
                //Log.writeDataToLogFile(2, "[OnEventSandBox] Loaded entities from: " + file.getAbsolutePath());
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
        //CodeGeneric.printInitClassToLog(this.getClass());
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
        if (instanceExists)
        {
            //Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }
        instanceExists = true;
    }

    private static final int RADIUS = 25;
    private static final int PARTICLE_INTERVAL = 10;
    private static final int MAX_PARTICLES_PER_BLOCK = 3;

    private static int tickCounter = 0;

    //@SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null || mc.player == null) return;

        World world = mc.world;
        BlockPos playerPos = mc.player.getPosition();
        Random rand = new Random();

        tickCounter++;

        if (tickCounter % PARTICLE_INTERVAL != 0)
            return;

        for (int i = 0; i < RADIUS * 2; i++)
        {
            int dx = rand.nextInt(RADIUS * 2) - RADIUS;
            int dz = rand.nextInt(RADIUS * 2) - RADIUS;
            BlockPos pos = playerPos.add(dx, -1, dz);

            if (world.getBlockState(pos).getBlock() == net.minecraft.init.Blocks.GRASS)
            {
                for (int j = 0; j < MAX_PARTICLES_PER_BLOCK; j++)
                {
                    double x = pos.getX() + 0.5 + (rand.nextDouble() - 0.5);
                    double y = pos.getY() + 1.5 + rand.nextDouble() * 0.5;
                    double z = pos.getZ() + 0.5 + (rand.nextDouble() - 0.5);

                    mc.world.spawnParticle(
                            EnumParticleTypes.END_ROD,
                            x, y, z,
                            0.0, 0.002, 0.0
                    );
                }
            }
        }
    }
}
