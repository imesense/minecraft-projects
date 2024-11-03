package org.imesense.dynamicspawncontrol.technical.eventprocessor.script.multiple;

import java.util.concurrent.atomic.AtomicInteger;

import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.ProjectStructure;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;

import org.imesense.dynamicspawncontrol.technical.config.gamedebugger.DataGameDebugger;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.generic.GenericBlockBreakAction;
import org.imesense.dynamicspawncontrol.technical.parser.ParserGenericJsonScript;

/**
 *
 */
@Mod.EventBusSubscriber(modid = ProjectStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnBlockBreakEvent
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public OnBlockBreakEvent()
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
     * @param breakEvent
     */
    @SubscribeEvent
    public synchronized void onUpdateBlockBreakEvent_0(BlockEvent.BreakEvent breakEvent)
    {
        if (breakEvent.getWorld().isRemote)
        {
            return;
        }

        AtomicInteger atomicInteger = new AtomicInteger();

        for (GenericBlockBreakAction rule : ParserGenericJsonScript.GENERIC_BLOCK_BREAK_ACTIONS_LIST)
        {
            if (rule.match(breakEvent))
            {
                Event.Result result = rule.getResult();

                if (DataGameDebugger.ConfigDataEvent.Instance.getDebugSetting("debug_on_block_break"))
                {
                    Log.writeDataToLogFile(0, "ConfigsParser._GenericBlockBreakActions. ID Rule "
                            + atomicInteger + ": "
                            + result + " entity: "
                            + breakEvent.getPlayer().getName()
                            + " y: " + breakEvent.getPos().getY()
                            + " biomes: "
                            + breakEvent.getWorld().getBiome(breakEvent.getPos()).getBiomeName());
                }

                rule.action(breakEvent);

                if (result == Event.Result.DENY)
                {
                    breakEvent.setCanceled(true);
                }
            }

            atomicInteger.getAndIncrement();
        }
    }
}

