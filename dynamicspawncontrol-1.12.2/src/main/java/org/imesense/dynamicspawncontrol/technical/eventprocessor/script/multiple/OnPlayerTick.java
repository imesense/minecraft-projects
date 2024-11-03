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
     * @param playerTickEvent
     */
    @SubscribeEvent
    public synchronized void onUpdatePlayerTick_0(TickEvent.PlayerTickEvent playerTickEvent)
    {
        if (playerTickEvent.phase != TickEvent.Phase.END || playerTickEvent.side != Side.SERVER)
        {
            return;
        }

        int id = playerTickEvent.player.getEntityId();

        TICK_COUNTERS.putIfAbsent(id, 0);

        int tickCounter = TICK_COUNTERS.get(id) + 1;
        TICK_COUNTERS.put(id, tickCounter);

        AtomicInteger atomicInteger = new AtomicInteger();

        for (GenericMapEffectAction rule : ParserGenericJsonScript.GENERIC_MAP_EFFECTS_ACTIONS_LIST)
        {
            if (tickCounter % rule.getTimeout() == 0 && rule.match(playerTickEvent))
            {
                if (DataGameDebugger.ConfigDataEvent.Instance.getDebugSetting("debug_on_player_tick"))
                {
                    Log.writeDataToLogFile(0, "ConfigsParser._GenericMapEffectsActions. ID Rule: " + atomicInteger
                            + " entity: " + playerTickEvent.player.getName()
                            + " y: " + playerTickEvent.player.getPosition().getY());
                }

                rule.action(playerTickEvent);

                return;
            }

            atomicInteger.getAndIncrement();
        }
    }
}

