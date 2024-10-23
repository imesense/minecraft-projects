package org.imesense.dynamicspawncontrol.technical.eventprocessor.script.multiple;

import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.ProjectStructure;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.technical.config.gamedebugger.DataGameDebugger;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.generic.GenericSpawnCondition;
import org.imesense.dynamicspawncontrol.technical.parser.ParserGenericJsonScript;

/**
 *
 */
@Mod.EventBusSubscriber(modid = ProjectStructure.STRUCT_INFO_MOD.MOD_ID)
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
		CodeGenericUtil.printInitClassToLog(this.getClass());
		
        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent
    public synchronized void onUpdateEntitySpawnEvent_0(LivingSpawnEvent.CheckSpawn event)
    {
        if (event.getWorld().isRemote)
        {
            return;
        }

        AtomicInteger i = new AtomicInteger();

        for (GenericSpawnCondition rule : ParserGenericJsonScript.GENERIC_SPAWN_CONDITIONS_LIST)
        {
            if (rule.match(event))
            {
                if (DataGameDebugger.ConfigDataEvent.instance.getDebugSetting("debug_on_entity_spawn"))
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

