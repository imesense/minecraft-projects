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
import org.imesense.dynamicspawncontrol.technical.eventprocessor.generic.GenericRightClickAction;
import org.imesense.dynamicspawncontrol.technical.parser.ParserGenericJsonScript;

/**
 *
 */
@Mod.EventBusSubscriber(modid = ProjectStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnRightClickEvent
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public OnRightClickEvent()
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
     * @param rightClickBlock
     */
    @SubscribeEvent
    public synchronized void onUpdateRightClickEvent_0(PlayerInteractEvent.RightClickBlock rightClickBlock)
    {
        if (rightClickBlock.getWorld().isRemote)
        {
            return;
        }

        AtomicInteger atomicInteger = new AtomicInteger();

        for (GenericRightClickAction rule : ParserGenericJsonScript.GENERIC_RIGHT_CLICK_ACTIONS_LIST)
        {
            if (rule.match(rightClickBlock))
            {
                Event.Result result = rule.getResult();

                if (DataGameDebugger.ConfigDataEvent.Instance.getDebugSetting("debug_on_right_click"))
                {
                    Log.writeDataToLogFile(0, "ConfigsParser._GenericRightClickActions. ID Rule: " + atomicInteger + ": " + result
                            + " entity: " + rightClickBlock.getEntityPlayer().getName()
                            + " y: " + rightClickBlock.getPos().getY()
                            + " biomes: " + rightClickBlock.getWorld().getBiome(rightClickBlock.getPos()).getBiomeName());
                }

                rule.action(rightClickBlock);

                rightClickBlock.setUseBlock(result);

                if (result == Event.Result.DENY)
                {
                    rightClickBlock.setCanceled(true);
                }

                return;
            }

            atomicInteger.getAndIncrement();
        }
    }
}

