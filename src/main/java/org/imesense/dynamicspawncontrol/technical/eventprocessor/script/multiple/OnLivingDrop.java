package org.imesense.dynamicspawncontrol.technical.eventprocessor.script.multiple;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.ProjectStructure;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.technical.config.gamedebugger.DataGameDebugger;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.generic.GenericDropLoot;
import org.imesense.dynamicspawncontrol.technical.parser.ParserGenericJsonScript;

/**
 *
 */
@Mod.EventBusSubscriber(modid = ProjectStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnLivingDrop
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public OnLivingDrop()
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
    public synchronized void onUpdateLivingDrops_0(LivingDropsEvent event)
    {
        AtomicInteger i = new AtomicInteger();

        for (GenericDropLoot rule : ParserGenericJsonScript.GENERIC_DROP_LOOT_LIST)
        {
            if (rule.match(event))
            {
                if (rule.isRemoveAll())
                {
                    event.getDrops().clear();
                }
                else
                {
                    for (Predicate<ItemStack> stackTest : rule.getToRemoveItems())
                    {
                        for (int idx = event.getDrops().size() - 1; idx >= 0; idx--)
                        {
                            ItemStack stack = event.getDrops().get(idx).getItem();

                            if (stackTest.test(stack))
                            {
                                event.getDrops().remove(idx);
                            }
                        }
                    }
                }

                for (Pair<ItemStack, Function<Integer, Integer>> pair : rule.getToAddItems())
                {
                    ItemStack item = pair.getLeft();
                    int fortune = event.getLootingLevel();
                    int amount = pair.getValue().apply(fortune);

                    BlockPos pos = event.getEntity().getPosition();

                    if (DataGameDebugger.ConfigDataEvent.instance.getDebugSetting("debug_on_living_drops"))
                    {
                        Log.writeDataToLogFile(0, "ConfigsParser._GenericDropLoot. ID Rule: " + i
                                + " entity: " + event.getEntity().getName() + " new drop @item: " + item);
                    }

                    while (amount > item.getMaxStackSize())
                    {
                        ItemStack copy = item.copy();
                        copy.setCount(item.getMaxStackSize());
                        amount -= item.getMaxStackSize();

                        event.getDrops().add(new EntityItem(event.getEntity().getEntityWorld(), pos.getX(), pos.getY(), pos.getZ(), copy));
                    }

                    if (amount > 0)
                    {
                        ItemStack copy = item.copy();
                        copy.setCount(amount);
                        event.getDrops().add(new EntityItem(event.getEntity().getEntityWorld(), pos.getX(), pos.getY(), pos.getZ(), copy));
                    }
                }
            }

            i.getAndIncrement();
        }
    }
}
