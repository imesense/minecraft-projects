package org.imesense.dynamicspawncontrol.technical.eventprocessor.script.multiple;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.ProjectStructure;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.technical.config.gamedebugger.DataGameDebugger;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.generic.GenericMapEffectAction;
import org.imesense.dynamicspawncontrol.technical.parser.ParserGenericJsonScript;

/**
 *
 */
@Mod.EventBusSubscriber(modid = ProjectStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnPlayerTick
{
    /**
     *
     */
    private final static Map<Integer, Integer> TICK_COUNTERS = new HashMap<>();

    /**
     *
     */
    public OnPlayerTick()
    {
        CodeGenericUtil.printInitClassToLog(this.getClass());
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent
    public synchronized void onUpdatePlayerTick_0(TickEvent.PlayerTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END || event.side != Side.SERVER)
        {
            return;
        }

        int id = event.player.getEntityId();

        TICK_COUNTERS.putIfAbsent(id, 0);

        int tickCounter = TICK_COUNTERS.get(id) + 1;
        TICK_COUNTERS.put(id, tickCounter);

        AtomicInteger i = new AtomicInteger();

        for (GenericMapEffectAction rule : ParserGenericJsonScript.GENERIC_MAP_EFFECTS_ACTIONS_LIST)
        {
            if (tickCounter % rule.getTimeout() == 0 && rule.match(event))
            {
                if (DataGameDebugger.ConfigDataEvent.instance.getDebugSetting("debug_on_player_tick"))
                {
                    Log.writeDataToLogFile(0, "ConfigsParser._GenericMapEffectsActions. ID Rule: " + i
                            + " entity: " + event.player.getName()
                            + " y: " + event.player.getPosition().getY());
                }

                rule.action(event);

                return;
            }

            i.getAndIncrement();
        }
    }
}

