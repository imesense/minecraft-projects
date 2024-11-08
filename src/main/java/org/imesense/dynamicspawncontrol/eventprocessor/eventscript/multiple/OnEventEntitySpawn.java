package org.imesense.dynamicspawncontrol.eventprocessor.eventscript.multiple;

import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.config.data.GameDebuggerData;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.eventprocessor.generic.GenericSpawnCondition;
import org.imesense.dynamicspawncontrol.technical.parser.ParserGenericJsonScript;

/**
 *
 */
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventEntitySpawn
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public OnEventEntitySpawn()
    {
		CodeGeneric.printInitClassToLog(this.getClass());
		
        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;
    }

    /**
     *
     * @param checkSpawn
     */
    @SubscribeEvent
    public synchronized void onUpdateEntitySpawnEvent_0(LivingSpawnEvent.CheckSpawn checkSpawn)
    {
        if (checkSpawn.getWorld().isRemote)
        {
            return;
        }

        AtomicInteger atomicInteger = new AtomicInteger();

        for (GenericSpawnCondition rule : ParserGenericJsonScript.GENERIC_SPAWN_CONDITIONS_LIST)
        {
            if (rule.match(checkSpawn))
            {
                if (GameDebuggerData.ConfigDataEvent.Instance.getDebugSetting("debug_on_entity_spawn"))
                {
                    Log.writeDataToLogFile(0, "ConfigsParser._GenericSpawnConditions. ID Rule: " + atomicInteger + ": "
                            + "entity: " + checkSpawn.getEntity().getName()
                            + " y: " + checkSpawn.getY()
                            + " biomes: " + checkSpawn.getWorld().getBiome(new BlockPos(
                                    checkSpawn.getX(), checkSpawn.getY(), checkSpawn.getZ())).getBiomeName());
                }

                rule.action(checkSpawn);
            }

            atomicInteger.getAndIncrement();
        }
    }
}

