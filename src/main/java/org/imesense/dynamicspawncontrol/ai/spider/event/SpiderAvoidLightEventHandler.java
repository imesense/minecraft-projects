package org.imesense.dynamicspawncontrol.ai.spider.event;

import net.minecraft.entity.monster.EntitySpider;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.ai.spider.task.SpiderAvoidLightEntityAIWrapper;
import org.imesense.dynamicspawncontrol.ai.spider.task.SpiderAvoidLightEntityAI;

import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

@InitLog
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class SpiderAvoidLightEventHandler
{
    private static volatile SpiderAvoidLightEventHandler _INSTANCE;

    public static SpiderAvoidLightEventHandler getInstance()
    {
        return CodeGeneric.getInstance(SpiderAvoidLightEventHandler.class);
    }

    public SpiderAvoidLightEventHandler()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    public void handleSpiderSpawn(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof EntitySpider)
        {
            EntitySpider spider = (EntitySpider) event.getEntity();

            spider.tasks.addTask(5, new SpiderAvoidLightEntityAIWrapper(spider,
                    new SpiderAvoidLightEntityAI(spider, 1.2, 7), 10));
        }
    }
}
