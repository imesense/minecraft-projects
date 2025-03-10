package org.imesense.dynamicspawncontrol.ai.spider.event;

import net.minecraft.entity.monster.EntitySpider;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.ai.spider.auxaction.AuxEntityAISpiderAvoidLight;
import org.imesense.dynamicspawncontrol.ai.spider.action.EntityAISpiderAvoidLight;
import org.imesense.dynamicspawncontrol.ai.zombie.event.OnEventBreakTorch;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
//import org.imesense.dynamicspawncontrol.ai.spider.action.EntityAISpiderFearLight;


/**
 *
 */
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventAvoidLight
{
    private static volatile OnEventAvoidLight _INSTANCE;

    public static OnEventAvoidLight getInstance()
    {
        return CodeGeneric.getInstance(OnEventAvoidLight.class);
        //if (_INSTANCE == null)
        //{
        //    synchronized (OnEventAvoidLight.class)
        //    {
        //        if (_INSTANCE == null)
        //        {
        //            _INSTANCE = new OnEventAvoidLight();
        //        }
        //    }
        //}
//
        //return _INSTANCE;
    }

    public void handleSpiderSpawn(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof EntitySpider)
        {
            EntitySpider spider = (EntitySpider) event.getEntity();

            spider.tasks.addTask(5, new AuxEntityAISpiderAvoidLight(spider,
                    new EntityAISpiderAvoidLight(spider, 1.2, 7), 10));
        }
    }
}
