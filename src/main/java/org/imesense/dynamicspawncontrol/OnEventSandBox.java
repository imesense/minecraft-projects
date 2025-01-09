package org.imesense.dynamicspawncontrol;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.api.IDebug;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.worldmemoryregistry.Option;

import java.util.*;

/**
 *
 */
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventSandBox implements IDebug
{
    private static boolean instanceExists = false;

    private static final Map<Integer, BlockPos> trackedEntities = new HashMap<>();

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

    private static String getEntityNameById(World world, int entityId)
    {
        Entity entity = world.getEntityByID(entityId);
        return entity != null ? EntityList.getKey(entity).toString() : "unknown";
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END)
            return;

        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

        if (server == null || server.getWorld(0) == null)
            return;

        World world = server.getWorld(0);
        Set<Entity> currentEntities = new HashSet<>(world.loadedEntityList);

        Iterator<Map.Entry<Integer, BlockPos>> iterator = trackedEntities.entrySet().iterator();

        while (iterator.hasNext())
        {
            Map.Entry<Integer, BlockPos> entry = iterator.next();
            int entityId = entry.getKey();
            BlockPos lastPosition = entry.getValue();

            boolean exists = currentEntities.stream().anyMatch(e -> e.getEntityId() == entityId);

            if (!exists)
            {
                Log.writeDataToLogFile(0, String.format("Сущность пропала с радиуса игрока: %s, Последняя позиция: %s",
                        getEntityNameById(world, entityId), lastPosition));

                iterator.remove();
            }
        }

        for (Entity entity : currentEntities)
        {
            if (!(entity instanceof EntityLivingBase))
                continue;

            if (Option.isOutsideRenderDistance(entity))
                continue;

            int entityId = entity.getEntityId();

            if (!trackedEntities.containsKey(entityId))
            {
                trackedEntities.put(entityId, entity.getPosition());
                ResourceLocation entityName = EntityList.getKey(entity);

                Log.writeDataToLogFile(0, String.format("Сущность в радиусе игрока: %s, Позиция: %s",
                        entityName != null ? entityName.toString() : "unknown", entity.getPosition()));
            }
        }
    }
}
