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
import org.imesense.dynamicspawncontrol.technical.eventprocessor.generic.GenericBlockPlaceAction;
import org.imesense.dynamicspawncontrol.technical.parser.ParserGenericJsonScript;

/**
 *
 */
@Mod.EventBusSubscriber(modid = ProjectStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnBlockPlaceEvent
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public OnBlockPlaceEvent()
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
     * @param placeEvent
     */
    @Deprecated
    @SubscribeEvent
    public synchronized void onUpdateBlockPaceEvent_0(BlockEvent.PlaceEvent placeEvent)
    {
        if (placeEvent.getWorld().isRemote)
        {
            return;
        }

        AtomicInteger atomicInteger = new AtomicInteger();

        for (GenericBlockPlaceAction rule : ParserGenericJsonScript.GENERIC_BLOCK_PLACE_ACTIONS_LIST)
        {
            if (rule.match(placeEvent))
            {
                Event.Result result = rule.getResult();

                if (DataGameDebugger.ConfigDataEvent.Instance.getDebugSetting("debug_on_block_place"))
                {
                    Log.writeDataToLogFile(0, "ConfigsParser._GenericBlockPlaceActions. ID Rule "
                            + atomicInteger + ": "
                            + result + " entity: "
                            + placeEvent.getPlayer().getName()
                            + " y: " + placeEvent.getPos().getY()
                            + " biomes: "
                            + placeEvent.getWorld().getBiome(placeEvent.getPos()).getBiomeName());
                }

                rule.action(placeEvent);

                if (result == Event.Result.DENY)
                {
                    placeEvent.setCanceled(true);
                }
            }

            atomicInteger.getAndIncrement();
        }
    }
}

