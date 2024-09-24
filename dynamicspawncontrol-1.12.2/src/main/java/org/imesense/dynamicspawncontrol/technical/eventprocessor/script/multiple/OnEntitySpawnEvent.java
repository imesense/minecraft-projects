package org.imesense.dynamicspawncontrol.technical.eventprocessor.script.multiple;

import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.configs.ConfigGameDebugger;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.generic.GenericSpawnConditions;
import org.imesense.dynamicspawncontrol.technical.parsers.ParserGenericJsonScripts;

/**
 *
 */
@Mod.EventBusSubscriber
public final class OnEntitySpawnEvent
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public OnEntitySpawnEvent()
    {
        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;

        CodeGenericUtils.printInitClassToLog(OnEntitySpawnEvent.class);
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public synchronized void onUpdateEntitySpawnEvent_0(LivingSpawnEvent.CheckSpawn event)
    {
        if (event.getWorld().isRemote)
        {
            return;
        }

        AtomicInteger i = new AtomicInteger();

        for (GenericSpawnConditions rule : ParserGenericJsonScripts.GENERIC_SPAWN_CONDITIONS_LIST)
        {
            if (rule.match(event))
            {
                if (ConfigGameDebugger.DebugGenericEntitySpawnEvent)
                {
                    Log.writeDataToLogFile(0, "ConfigsParser._GenericSpawnConditions. ID Rule: " + i + ": "
                            + "entity: " + event.getEntity().getName()
                            + " y: " + event.getY()
                            + " biomes: " + event.getWorld().getBiome(new BlockPos(event.getX(), event.getY(), event.getZ())).getBiomeName());
                }

                rule.action(event);
            }

            i.getAndIncrement();
        }
    }
}

