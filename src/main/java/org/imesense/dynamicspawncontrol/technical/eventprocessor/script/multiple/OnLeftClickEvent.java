package org.imesense.dynamicspawncontrol.technical.eventprocessor.script.multiple;

import java.util.concurrent.atomic.AtomicInteger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.imesense.dynamicspawncontrol.ProjectStructure;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.technical.config.gamedebugger.DataGameDebugger;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.generic.GenericLeftClickAction;
import org.imesense.dynamicspawncontrol.technical.parser.ParserGenericJsonScript;

/**
 *
 */
@Mod.EventBusSubscriber(modid = ProjectStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnLeftClickEvent
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public OnLeftClickEvent()
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
     * @param leftClickBlock
     */
    @SubscribeEvent
    public synchronized void onUpdateLeftClickEvent_0(PlayerInteractEvent.LeftClickBlock leftClickBlock)
    {
        if (leftClickBlock.getWorld().isRemote)
        {
            return;
        }

        AtomicInteger atomicInteger = new AtomicInteger();

        for (GenericLeftClickAction rule : ParserGenericJsonScript.GENERIC_LEFT_CLICK_ACTIONS_LIST)
        {
            if (rule.match(leftClickBlock))
            {
                Event.Result result = rule.getResult();

                if (DataGameDebugger.ConfigDataEvent.Instance.getDebugSetting("debug_on_left_click"))
                {
                    Log.writeDataToLogFile(0, "ConfigsParser._GenericLeftClickActions. ID Rule: " + atomicInteger + ": "
                            + result
                            + " entity: " + leftClickBlock.getEntityPlayer().getName()
                            + " y: " + leftClickBlock.getPos().getY()
                            + " biomes: " + leftClickBlock.getWorld().getBiome(leftClickBlock.getPos()).getBiomeName());
                }

                rule.action(leftClickBlock);

                leftClickBlock.setUseBlock(result);

                if (result == Event.Result.DENY)
                {
                    leftClickBlock.setCanceled(true);
                }

                return;
            }

            atomicInteger.getAndIncrement();
        }
    }
}

