package org.imesense.dynamicspawncontrol.ai.spider.event;

import net.minecraft.entity.monster.EntitySpider;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.ai.spider.auxaction.AuxEntityAISpiderAvoidLight;
import org.imesense.dynamicspawncontrol.ai.spider.action.EntityAISpiderAvoidLight;
//import org.imesense.dynamicspawncontrol.ai.spider.action.EntityAISpiderFearLight;


/**
 *
 */
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventAvoidLight
{
    /**
     *
     * @param entityJoinWorldEvent
     */
    @SubscribeEvent
    public static void onSpiderSpawn(EntityJoinWorldEvent entityJoinWorldEvent)
    {
        if (entityJoinWorldEvent.getEntity() instanceof EntitySpider)
        {
            EntitySpider spider = (EntitySpider) entityJoinWorldEvent.getEntity();

            spider.tasks.addTask(5, new AuxEntityAISpiderAvoidLight(spider,
                    new EntityAISpiderAvoidLight(spider, 1.2, 7), 10));
        }
    }
}
