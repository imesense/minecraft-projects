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
     * @param livingDropsEvent
     */
    @SubscribeEvent
    public synchronized void onUpdateLivingDrops_0(LivingDropsEvent livingDropsEvent)
    {
        AtomicInteger atomicInteger = new AtomicInteger();

        for (GenericDropLoot rule : ParserGenericJsonScript.GENERIC_DROP_LOOT_LIST)
        {
            if (rule.match(livingDropsEvent))
            {
                if (rule.isRemoveAll())
                {
                    livingDropsEvent.getDrops().clear();
                }
                else
                {
                    for (Predicate<ItemStack> stackTest : rule.getToRemoveItems())
                    {
                        for (int idx = livingDropsEvent.getDrops().size() - 1; idx >= 0; idx--)
                        {
                            ItemStack itemStack = livingDropsEvent.getDrops().get(idx).getItem();

                            if (stackTest.test(itemStack))
                            {
                                livingDropsEvent.getDrops().remove(idx);
                            }
                        }
                    }
                }

                for (Pair<ItemStack, Function<Integer, Integer>> pair : rule.getToAddItems())
                {
                    ItemStack itemStack = pair.getLeft();

                    int fortune = livingDropsEvent.getLootingLevel();
                    int amount = pair.getValue().apply(fortune);

                    BlockPos blockPos = livingDropsEvent.getEntity().getPosition();

                    if (DataGameDebugger.ConfigDataEvent.Instance.getDebugSetting("debug_on_living_drops"))
                    {
                        Log.writeDataToLogFile(0, "ConfigsParser._GenericDropLoot. ID Rule: " + atomicInteger
                                + " entity: " + livingDropsEvent.getEntity().getName() + " new drop @item: " + itemStack);
                    }

                    while (amount > itemStack.getMaxStackSize())
                    {
                        ItemStack itemStack1 = itemStack.copy();

                        itemStack1.setCount(itemStack.getMaxStackSize());
                        amount -= itemStack.getMaxStackSize();

                        livingDropsEvent.getDrops().add(new EntityItem(
                                livingDropsEvent.getEntity().getEntityWorld(), blockPos.getX(), blockPos.getY(), blockPos.getZ(), itemStack1));
                    }

                    if (amount > 0)
                    {
                        ItemStack itemStack2 = itemStack.copy();

                        itemStack2.setCount(amount);

                        livingDropsEvent.getDrops().add(new EntityItem(
                                livingDropsEvent.getEntity().getEntityWorld(), blockPos.getX(), blockPos.getY(), blockPos.getZ(), itemStack2));
                    }
                }
            }

            atomicInteger.getAndIncrement();
        }
    }
}
