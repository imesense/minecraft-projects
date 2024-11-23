package org.imesense.dynamicspawncontrol.ai.spider.event;

import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.ai.spider.action.DelayedEntityAITask;
import org.imesense.dynamicspawncontrol.ai.spider.action.EntityAISpiderAvoidLight;
//import org.imesense.dynamicspawncontrol.ai.spider.action.EntityAISpiderFearLight;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnSearchEnemyAttack
{
    @SubscribeEvent
    public static void onSpiderSpawn(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof EntitySpider)
        {
            EntitySpider spider = (EntitySpider) event.getEntity();

            spider.tasks.addTask(5, new DelayedEntityAITask(spider,
                    new EntityAISpiderAvoidLight(spider, 1.2, 7), 10));
        }
    }
}
